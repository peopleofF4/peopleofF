package com.sparta.peopleoff.domain.admin.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.admin.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.admin.dto.StoreApproveRequestDto;
import com.sparta.peopleoff.domain.admin.dto.UserResponseDto;
import com.sparta.peopleoff.domain.admin.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.entity.enums.StoreStatus;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.RegistrationStatus;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;

  /**
   * 회원 전체 조회
   *
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDto> getUsers(String userName, Pageable pageable) {

    int pageSize = pageable.getPageSize();

    pageSize = (pageSize == 30 || pageSize == 50) ? pageSize : 10;

    Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageSize,
        pageable.getSort());

    if (userName == null || userName.isEmpty()) {
      return userRepository.findAll(pageable).stream()
          .map(this::convertToDto)
          .collect(Collectors.toList())
          ;
    }

    return userRepository.findByUserNameContaining(userName, pageable).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  /**
   * 매니저 등록 승인
   *
   * @param userId
   * @param managerApproveRequestDto
   * @return
   */
  @Override
  @Transactional
  public void managerApprove(Long userId, ManagerApproveRequestDto managerApproveRequestDto) {

    // [예외2] - 없는 아이디
    UserEntity user = userRepository.findById(userId).orElseThrow(() ->
        new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 아이디입니다."));

    // [예외3] - 이미 manager 권한일 경우
    checkManagerRole(user);

    user.setManagerRegistrationStatus(managerApproveRequestDto.getRegistrationStatus());

    if (user.getManagerRegistrationStatus() == RegistrationStatus.ACCEPTED) {
      user.setRole(UserRole.MANAGER);
    }
  }

  /**
   * 유저 검색
   *
   * @param userName
   * @return
   */
//  @Override
//  @Transactional(readOnly = true)
//  public List<UserResponseDto> searchUser(String userName) {
//
//    List<UserEntity> searchUsers = userRepository.findByUserNameContaining(userName);
//
//    // [예외2] - 검색결과가 없음
//    if (searchUsers.isEmpty()) {
//      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
//    }
//
//    return searchUsers.stream()
//        .map(this::convertToDto)
//        .collect(Collectors.toList());
//  }

  /**
   * 유저 권한 수정
   *
   * @param userId
   * @param userRoleRequestDto
   * @return
   */
  @Override
  @Transactional
  public void updateUserRole(Long userId, UserRoleRequestDto userRoleRequestDto) {

    // [예외2] - 없는 사용자
    UserEntity user = userRepository.findById(userId).orElseThrow(()
        -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

    user.setRole(userRoleRequestDto.getUserRole());
  }

  /**
   * 가게 등록 승인 / 거부
   *
   * @param user
   * @param storeId
   * @param storeApproveRequestDto
   * @return
   */
  @Override
  @Transactional
  public void updateStoreRegist(UserEntity user, UUID storeId,
      StoreApproveRequestDto storeApproveRequestDto) {

    // [예외2] - 존재하지 않는 가게
    StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
        new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

    // [예외3] - 이전과 같은 상태
    checkApproveStatusSame(store, storeApproveRequestDto);

    // 가게 등록 승인시 Customer 권한이면 OWNER 권한으로 변경
    if (storeApproveRequestDto.getStoreStatus() == StoreStatus.REGISTRATION_ACCEPTED
        && user.getRole() == UserRole.CUSTOMER) {

      user.setRole(UserRole.OWNER);
      userRepository.save(user);
    }

    store.setStoreStatus(storeApproveRequestDto.getStoreStatus());
  }


  /**
   * 가게 삭제 승인 / 거부
   *
   * @param storeId
   * @param storeApproveRequestDto
   * @return
   */
  @Override
  @Transactional
  public void updateStoreDelete(UUID storeId, StoreApproveRequestDto storeApproveRequestDto) {

    // [예외2] - 존재하지 않는 가게
    StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
        new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

    // [예외3] - 이전과 같은 상태
    checkApproveStatusSame(store, storeApproveRequestDto);

    store.setStoreStatus(storeApproveRequestDto.getStoreStatus());

    // 삭제 승인 일때만 DeletionStatus를 바꾼다.
    if (storeApproveRequestDto.getStoreStatus() == StoreStatus.DELETED_ACCEPTED) {
      store.setDeletionStatus(DeletionStatus.DELETED);
    }

  }

//  private void checkDeleteStatusSame(StoreEntity store,
//      StoreApproveRequestDto storeApproveRequestDto) {
//    if (store.getStoreStatus().equals(storeApproveRequestDto.getStoreStatus())) {
//      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "변경할 상태값을 입력해주세요.");
//    }
//  }

  private void checkApproveStatusSame(StoreEntity store,
      StoreApproveRequestDto storeApproveRequestDto) {
    if (store.getStoreStatus().equals(storeApproveRequestDto.getStoreStatus())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "변경할 상태값을 입력해주세요.");
    }
  }

  private void checkManagerRole(UserEntity user) {
    if (UserRole.MANAGER == user.getRole()) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "이미 매니저 권한입니다.");
    }
  }

  /**
   * UserEntity -> UserResponseDto
   *
   * @param userEntity
   * @return
   */
  private UserResponseDto convertToDto(UserEntity userEntity) {
    // UserResponseDto 객체 생성 및 UserEntity의 값을 이용해 초기화
    return new UserResponseDto(
        userEntity.getId(),
        userEntity.getNickName(),
        userEntity.getUserName(),
        userEntity.getEmail(),
        userEntity.getPhoneNumber(),
        userEntity.getAddress(),
        userEntity.getRole());
  }
}

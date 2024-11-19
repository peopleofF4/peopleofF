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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
   * 전체 회원 페이지네이션 조회 & 유저 이름을 검색
   *
   * @param userName
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public Page<UserResponseDto> getUsers(String userName, Pageable pageable) {

    int pageSize = pageable.getPageSize();

    pageSize = (pageSize == 30 || pageSize == 50) ? pageSize : 10;

    Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageSize,
        pageable.getSort());

    if (userName == null || userName.isEmpty()) {
      return userRepository.findAll(updatedPageable)
          .map(this::convertToDto)
          ;
    }

    return userRepository.findByUserNameContaining(userName, updatedPageable)
        .map(this::convertToDto)
        ;
  }

  /**
   * 매니저 등록 승인 API
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

    if (managerApproveRequestDto.getRegistrationStatus() == RegistrationStatus.ACCEPTED) {
      user.setRole(UserRole.MANAGER);
    }

    user.setManagerRegistrationStatus(managerApproveRequestDto.getRegistrationStatus());
  }


  /**
   * 유저 권한 수정 API
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

    // 현재 권한 체크
    if (userRoleRequestDto.getUserRole() == user.getRole()) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "현재 같은 권한 입니다.");
    }

    // Manager 권한으로 수정하면 ACCEPTED로 수정
    if (userRoleRequestDto.getUserRole() == UserRole.MANAGER) {
      user.setManagerRegistrationStatus(RegistrationStatus.ACCEPTED);
    }

    user.setRole(userRoleRequestDto.getUserRole());
  }

  /**
   * 가게 등록 승인 / 거부 API
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

    UserEntity owner = store.getUser();
    // 가게 등록 승인시 Customer 권한이면 OWNER 권한으로 변경
    if (!(storeApproveRequestDto.getStoreStatus() == StoreStatus.REGISTRATION_ACCEPTED
        && owner.getRole() == UserRole.CUSTOMER)) {

    }

    owner.setRole(UserRole.OWNER);
    userRepository.save(owner);

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

    // 잘못 눌렀을시 다시 가게 생성해야된다.
    if (store.getStoreStatus() == StoreStatus.DELETED_ACCEPTED) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "이미 삭제된 가게입니다");
    }

    store.setStoreStatus(storeApproveRequestDto.getStoreStatus());

    // 삭제 승인 일때만 DeletionStatus를 바꾼다.
    if (storeApproveRequestDto.getStoreStatus() == StoreStatus.DELETED_ACCEPTED) {
      store.setDeletionStatus(DeletionStatus.DELETED);
    }

  }

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

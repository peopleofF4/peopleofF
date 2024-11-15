package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.enums.RegistrationStatus;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * 회원 전체 조회
     * @return
     */ //page //readonly
    public List<UserResponseDto> getUsers() {

        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 매니저 등록 승인
     * @param userId
     * @param managerApproveRequestDto
     * @return
     */
    @Transactional
    public void managerApprove(Long userId, ManagerApproveRequestDto managerApproveRequestDto) {

        // [예외2] - 없는 아이디
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 아이디입니다."));

        // [예외3] - 이미 manager 권한일 경우
        checkManagerRole(user);

        user.setManagerRegistrationStatus(managerApproveRequestDto.getRegistrationStatus());

        if(user.getManagerRegistrationStatus()==(RegistrationStatus.ACCEPTED)) {
            user.setRole(UserRole.MANAGER);
        }

        userRepository.save(user);
    }


    /**
     * UserEntity -> UserResponseDto
     * @param userEntity
     * @return
     */
    private UserResponseDto convertToDto(UserEntity userEntity) {
        // UserResponseDto 객체 생성 및 UserEntity의 값을 이용해 초기화
        return new UserResponseDto(
                userEntity.getId(),
                userEntity.getNickName(),
                userEntity.getEmail(),
                userEntity.getPhoneNumber(),
                userEntity.getAddress(),
                userEntity.getRole());
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteUser(Long userIdToDelete) {
        // [예외1] - 존재하지 않는 사용자
        UserEntity userToDelete= userRepository.findById(userIdToDelete).orElseThrow(()
                -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        userToDelete.setDeletionStatus(DeletionStatus.DELETED);
    }

    /**
     * 유저 검색
     *
     * @param userName
     * @return
     */
    public List<UserResponseDto> searchUser(String userName) {

        List<UserEntity> searchUsers = userRepository.findByUserNameContaining(userName);

        // [예외2] - 검색결과가 없음
        if (searchUsers.isEmpty()) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
        }

        return searchUsers.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUserName(),
                        user.getNickName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        user.getRole()
                ))
                .collect(Collectors.toList());

    }

    /**
     * 유저 권한 수정
     *
     * @param userId
     * @param userRoleRequestDto
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    public void updateUserRole(Long userId, UserRoleRequestDto userRoleRequestDto) {

        // [예외2] - 없는 사용자
        UserEntity user = userRepository.findById(userId).orElseThrow(()
                -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        user.setRole(userRoleRequestDto.getUserRole());

        userRepository.save(user);
    }

    /**
     * 가게 등록 승인 / 거부
     *
     * @param user
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    public void updateStoreRegist(UserEntity user, UUID storeId, ManagerApproveRequestDto managerApproveRequestDto) {

        // [예외2] - 존재하지 않는 가게
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

        // [예외3] - 이전과 같은 상태
        checkApproveStatusSame(store, managerApproveRequestDto);

        store.setRegistrationStatus(managerApproveRequestDto.getRegistrationStatus());

        storeRepository.save(store);
    }


    /**
     * 가게 삭제 승인 / 거부
     *
     * @param user
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    public void updateStoreDelete(UserEntity user, UUID storeId, ManagerApproveRequestDto managerApproveRequestDto) {

        // [예외2] - 존재하지 않는 가게
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

        // [예외3] - 이전과 같은 상태
        checkDeleteStatusSame(store, managerApproveRequestDto);

        store.setDeletionStatus(managerApproveRequestDto.getDeletionStatus());

        storeRepository.save(store);
    }

    private void checkDeleteStatusSame(StoreEntity store, ManagerApproveRequestDto managerApproveRequestDto) {
        if(!store.getDeletionStatus().equals(managerApproveRequestDto.getDeletionStatus())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "변경할 상태값을 입력해주세요.");
        }
    }

    private void checkApproveStatusSame(StoreEntity store, ManagerApproveRequestDto managerApproveRequestDto) {
        if(!store.getRegistrationStatus().equals(managerApproveRequestDto.getRegistrationStatus())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "변경할 상태값을 입력해주세요.");
        }
    }

    private void checkManagerRole(UserEntity user) {
        if(UserRole.MANAGER.equals(user.getRole())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "이미 매니저 권한입니다.");
        }
    }
}

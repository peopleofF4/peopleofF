package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UpdateResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;


    @Transactional
    public void deleteUser(Long userIdToDelete, UserEntity user) {
        // [예외1] - 존재하지 않는 사용자
        UserEntity userToDelete= userRepository.findById(userIdToDelete).orElseThrow(()
                -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        // [예외2] - Admin 권한 체크
        checkManagerOrMasterAuthority(user);

        userToDelete.setDeletionStatus(DeletionStatus.DELETED);

        userRepository.save(userToDelete);
    }

    /**
     * 유저 검색
     *
     * @param userName
     * @param loginUser
     * @return
     */
    public List<UserResponseDto> searchUser(String userName, UserEntity loginUser) {
        // [예외1] - Admin 권한 체크
        checkManagerOrMasterAuthority(loginUser);

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
     * @param loginUser
     * @param userRoleRequestDto
     * @return
     */
    @Transactional
    public UpdateResponseDto updateUserRole(Long userId, UserEntity loginUser, UserRoleRequestDto userRoleRequestDto) {
        // [예외1] - Admin 권한 체크
        checkManagerOrMasterAuthority(loginUser);

        // [예외2] - 없는 사용자
        UserEntity user = userRepository.findById(userId).orElseThrow(()
                -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        user.setRole(userRoleRequestDto.getUserRole());

        userRepository.save(user);

        return new UpdateResponseDto(user);
    }

    /**
     * 가게 등록 승인 / 거부
     *
     * @param user
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @Transactional
    public UpdateResponseDto updateStoreRegist(UserEntity user, UUID storeId, ManagerApproveRequestDto managerApproveRequestDto) {
        // [예외1] - Admin 권한 체크
        checkManagerOrMasterAuthority(user);

        // [예외2] - 존재하지 않는 가게
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

        // [예외3] - 이전과 같은 상태
        checkApproveStatusSame(store, managerApproveRequestDto);

        store.setRegistrationStatus(managerApproveRequestDto.getRegistrationStatus());

        storeRepository.save(store);
        return new UpdateResponseDto(user);
    }


    /**
     * 가게 삭제 승인 / 거부
     *
     * @param user
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    public UpdateResponseDto updateStoreDelete(UserEntity user, UUID storeId, ManagerApproveRequestDto managerApproveRequestDto) {
        // [예외1] - Admin 권한 체크
        checkManagerOrMasterAuthority(user);

        // [예외2] - 존재하지 않는 가게
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

        // [예외3] - 이전과 같은 상태
        checkDeleteStatusSame(store, managerApproveRequestDto);

        store.setDeletionStatus(managerApproveRequestDto.getDeletionStatus());

        storeRepository.save(store);


        return new UpdateResponseDto(user);
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

    private void checkManagerOrMasterAuthority(UserEntity user) {
        if (!(UserRole.MASTER).equals(user.getRole()) || (UserRole.MANAGER).equals(user.getRole())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Admin 권한으로 접근할 수 있읍니다.");
        }
    }

}

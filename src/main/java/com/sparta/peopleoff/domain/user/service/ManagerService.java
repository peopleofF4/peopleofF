package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.enums.RegistrationStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
    public void deleteUser(Long userIdToDelete, User user) {
        // [예외1] - 존재하지 않는 사용자
        UserEntity userToDelete= userRepository.findById(userIdToDelete).orElseThrow(()
                -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        // [예외2] - Admin 권한 체크
//        checkAdminAuthority(user);

        // Todo: 삭제처리 하려고 userEntity에 @Setter추가 했는데 논의하기
        userToDelete.setDeletionStatus(DeletionStatus.DELETED);

        userRepository.save(userToDelete);
    }

    /**
     * 유저 검색
     * @param userName
     * @return
     */
    public List<UserResponseDto> searchUser(String userName) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

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
     * @param userId
     */
    @Transactional
    public void updateUserRole(Long userId/*, User user*/, UserRoleRequestDto userRoleRequestDto) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        // [예외2] - 없는 사용자
        UserEntity user = userRepository.findById(userId).orElseThrow(()
                -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        user.setRole(userRoleRequestDto.getRole());

        userRepository.save(user);
    }

    /**
     * 가게 등록 승인 / 거부
     *
     * @param storeId
     * @param registrationStatus
     */
    @Transactional
    public void updateStoreRegist(UUID storeId, RegistrationStatus registrationStatus) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        // [예외2] - 이전과 같은 상태

        // [예외3] - 존재하지 않는 가게
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

        store.setRegistrationStatus(registrationStatus);

        storeRepository.save(store);
    }

    /**
     * 가게 삭제 승인 / 거부
     * @param storeId
     * @param deletionStatus
     */
    public void updateStoreDelete(UUID storeId, DeletionStatus deletionStatus) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        // [예외2] - 이전과 같은 상태

        // [예외3] - 존재하지 않는 가게
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 가게입니다."));

        store.setDeletionStatus(deletionStatus);

        storeRepository.save(store);
    }

//    private void checkAdminAuthority(User user) {
//        private void checkAdminAuthority(User user) {
//            if (!"ADMIN".equals(user.getUserRole())) { // Todo: ADMIN은 나중에 Enum으로 유저role 생기면 바꾸기
//                throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Admin권한으로 접근할 수 있읍니다.");
//            }
//        }
//    }
}

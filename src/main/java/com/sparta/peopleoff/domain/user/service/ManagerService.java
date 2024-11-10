package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;


    @Transactional
    public void deleteUser(Long userIdToDelete, User user) {
        // [예외1] - 없는 사용자
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

        List<UserEntity> searchUsers = userRepository.findByUsernameContaining(userName);

        // [예외2] - 검색결과가 없음
        if (searchUsers.isEmpty()) {
            throw new CustomApiException(ResBasicCode.NOT_FOUND, "사용자를 찾을 수 없습니다.");
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

//    private void checkAdminAuthority(User user) {
//        private void checkAdminAuthority(User user) {
//            if (!"ADMIN".equals(user.getUserRole())) { // Todo: ADMIN은 나중에 Enum으로 유저role 생기면 바꾸기
//                throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Admin권한으로 접근할 수 있읍니다.");
//            }
//        }
//    }
}

package com.sparta.peopleoff.domain.user.service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveResponseDto;
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
public class AdminService {

    private final UserRepository userRepository;

    /**
     * 회원 전체 조회
     * @param user
     * @return
     */
    @Transactional
    public List<UserResponseDto> getUsers(User user) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
                userEntity.getRole()
        );
    }

//    public ManagerApproveResponseDto managerApprove() {
//
//    }

//    private void checkAdminAuthority(User user) {
//        private void checkAdminAuthority(User user) {
//            if (!"ADMIN".equals(user.getUserRole())) { // Todo: ADMIN은 나중에 Enum으로 유저role 생기면 바꾸기
//                throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Admin권한으로 접근할 수 있읍니다.");
//            }
//        }
//    }
}

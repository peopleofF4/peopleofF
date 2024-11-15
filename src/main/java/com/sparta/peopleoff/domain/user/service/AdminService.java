package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.enums.RegistrationStatus;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    /**
     * 회원 전체 조회
     * @return
     */
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

        if(user.getManagerRegistrationStatus().equals(RegistrationStatus.ACCEPTED)) {
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

    private void checkManagerOrMasterAuthority(UserEntity user) {
        if (!(UserRole.MASTER).equals(user.getRole()) || (UserRole.MANAGER).equals(user.getRole())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Admin 권한으로 접근할 수 있읍니다.");
        }
    }


    private void checkManagerRole(UserEntity user) {
        if(UserRole.MANAGER.equals(user.getRole())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "이미 매니저 권한입니다.");
        }
    }
}

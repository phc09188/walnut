package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.conponents.MailComponents;
import com.shopper.walnut.walnut.exception.UserRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.UserReceptionStatus;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.repository.UserReceptionStatusRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserSignUpService {
    private final UserRepository userRepository;
    private final UserReceptionStatusRepository userReceptionStatusRepository;
    private final MailComponents mailComponents;

    public boolean register(UserInput parameter){
        //해당 아이디의 유저가 존재하는지 확인
        Optional<User> optionalUserA = userRepository.findById(parameter.getUserId());
        Optional<User> optionalUserB = userRepository.findByUserEmail(parameter.getUserEmail());
        if(optionalUserA.isPresent()){
            throw new UserRegisterException(ErrorCode.USERID_ALREADY_EXIST);
        }
        if(optionalUserB.isPresent()){
            throw new UserRegisterException(ErrorCode.USER_EMAIL_ALREADY_EXIST);
        }
        String encPassword = BCrypt.hashpw(parameter.getUserPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();
        User user = User.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .userEmail(parameter.getUserEmail())
                .userPassword(encPassword)
                .userRegistration(parameter.getUserRegistration())
                .userPhone(parameter.getUserPhone())
                .userRegDt(LocalDate.now())
                .emailAuthKey(uuid)
                .userStatus(User.MEMBER_STATUS_REQ)
                .build();
        userRepository.save(user);
        UserReceptionStatus userReceptionStatus =
                UserReceptionStatus.builder()
                        .userId(parameter.getUserId())
                        .userName(parameter.getUserName())
                        .userEmail(parameter.getUserEmail())
                        .marketingYn(parameter.isMarketingYn())
                        .payYn(parameter.isPayYn())
                        .privateYn(parameter.isPrivateYn())
                        .build();
        userReceptionStatusRepository.save(userReceptionStatus);
        String email = parameter.getUserId();
        String subject = "프리미엄 쇼핑몰 호두에 가입하신 것을 축하드립니다. ";
        String text = "<p>호두에 관심을 가져 주신 것에 감사드립니다.<p><p>아래 링크를 클릭하시고 이메일 인증을 완료해주세요./p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'> 이메일 인증 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;

    }
    public boolean emailAuth(String uuid) {

        Optional<User> optionalUser = userRepository.findByEmailAuthKey(uuid);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (user.isEmailAuthYn()) {
            return false;
        }
        user.setUserStatus(user.MEMBER_STATUS_ING);
        user.setEmailAuthYn(true);
        userRepository.save(user);
        return true;
    }
}

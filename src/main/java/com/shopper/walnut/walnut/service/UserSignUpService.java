package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.conponents.MailComponents;
import com.shopper.walnut.walnut.exception.UserRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserSignUpService implements UserDetailsService {
    private final UserRepository userRepository;
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
                .address(new Address(parameter.getZipCode(),parameter.getStreetAdr(),parameter.getDetailAdr()))
                .userPhone(parameter.getUserPhone())
                .userRegDt(LocalDate.now())
                .emailAuthKey(uuid)
                .userStatus(User.MEMBER_STATUS_REQ)
                .marketingYn(parameter.isMarketingYn())
                .privateYn(parameter.isPrivateYn())
                .payYn(parameter.isPayYn())
                .build();
        userRepository.save(user);

        String email = parameter.getUserEmail();
        String subject = "프리미엄 쇼핑몰 호두에 가입하신 것을 축하드립니다. ";
        String text = "<p>호두에 관심을 가져 주신 것에 감사드립니다.<p><p>아래 링크를 클릭하시고 이메일 인증을 완료해주세요./p>"
                + "<div><a target='_blank' href='http://localhost:8080/user/email-auth?id=" + uuid + "'> 이메일 인증 </a></div>";
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalMember = userRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        User user = optionalMember.get();

//        if (User.MEMBER_STATUS_REQ.equals(user.getUserStatus())) {
//            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
//        }

//        if (User.MEMBER_STATUS_STOP.equals(user.getUserStatus())) {
//            throw new MemberStopUserException("정지된 회원 입니다.");
//        }
//
//        if (User.MEMBER_STATUS_WITHDRAW.equals(user.getUserStatus())) {
//            throw new MemberStopUserException("탈퇴된 회원 입니다.");
//        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

//        if (user.isAdminYn()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPassword(), grantedAuthorities);
    }
}

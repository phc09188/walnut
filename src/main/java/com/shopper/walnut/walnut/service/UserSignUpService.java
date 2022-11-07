package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.conponents.MailComponents;
import com.shopper.walnut.walnut.exception.UserException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.input.UserClassification;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserSignUpService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final MailComponents mailComponents;

    /**회원가입**/
    public void register(UserInput parameter){
        //해당 아이디의 유저가 존재하는지 확인
        Optional<User> optionalUser = userRepository.findByUserIdAndUserEmail(parameter.getUserId(),parameter.getUserEmail());
        if(optionalUser.isPresent()){
            throw new UserException(ErrorCode.USERID_ALREADY_EXIST);
        }
        String encPassword = BCrypt.hashpw(parameter.getUserPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();
        User user = User.of(parameter,encPassword,uuid);
        userRepository.save(user);

        /* test를 위해 잠시 메일은 중단
        String email = parameter.getUserEmail();
        String subject = "프리미엄 쇼핑몰 호두에 가입하신 것을 축하드립니다. ";
        String text = "<p>호두에 관심을 가져 주신 것에 감사드립니다.<p><p>아래 링크를 클릭하시고 이메일 인증을 완료해주세요./p>"
                + "<div><a target='_blank' href='http://localhost:8080/user/email-auth?id=" + uuid + "'> 이메일 인증 </a></div>";
        mailComponents.sendMail(email, subject, text);*/

    }
    /** 이메일 인증 여부 확인**/
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

    /** 권한 별 로그인 **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalMember = userRepository.findById(username);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        User user = optionalMember.get();
        if(user.getUserClassification() == UserClassification.USER){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if(user.getUserClassification() == UserClassification.ADMIN){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if(user.getUserClassification() == UserClassification.SELLER){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BRAND"));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPassword(), grantedAuthorities);
    }
}

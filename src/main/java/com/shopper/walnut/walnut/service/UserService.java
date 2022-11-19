package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.conponents.MailComponents;
import com.shopper.walnut.walnut.exception.error.UserIDAlreadyExist;
import com.shopper.walnut.walnut.exception.error.UserNotFound;
import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.entity.EventListener;
import com.shopper.walnut.walnut.model.input.UserClassification;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.status.MemberShip;
import com.shopper.walnut.walnut.model.status.UserStatus;
import com.shopper.walnut.walnut.repository.EventListenerRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EventListenerRepository eventListenerRepository;
    private final MailComponents mailComponents;

    /**
     * 회원가입
     **/
    public void register(UserInput parameter) {
        //해당 아이디의 유저가 존재하는지 확인
        Optional<User> optionalUser = userRepository.findByUserIdOrUserEmail(parameter.getUserId(), parameter.getUserEmail());
        if (optionalUser.isPresent()) {
            throw new UserIDAlreadyExist();
        }
        String encPassword = BCrypt.hashpw(parameter.getUserPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();
        User user = User.of(parameter, encPassword, uuid);
        EventListener eventListener = new EventListener(parameter.getUserEmail(), parameter.isMarketingYn());
        eventListener.setUser(user);
        eventListenerRepository.save(eventListener);
        userRepository.save(user);

        String email = parameter.getUserEmail();
        String subject = "프리미엄 쇼핑몰 호두에 가입하신 것을 축하드립니다. ";
        String text = "<p>호두에 관심을 가져 주신 것에 감사드립니다.<p><p>아래 링크를 클릭하시고 이메일 인증을 완료해주세요./p>"
                + "<div><a target='_blank' href='http://localhost:8080/user/email-auth?id=" + uuid + "'> 이메일 인증 </a></div>";
        mailComponents.sendMail(email, subject, text);

    }

    /**
     * 이메일 인증 여부 확인
     **/
    public boolean emailAuth(String uuid) {

        Optional<User> optionalUser = userRepository.findByEmailAuthKey(uuid);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (user.isEmailAuthYn()) {
            return false;
        }
        user.setUserStatus(UserStatus.ING);
        user.setEmailAuthYn(true);
        userRepository.save(user);
        return true;
    }

    /**
     * 맴버쉽 업데이트
     **/
    public void userMemberShipUpdate(User user, long payAmount) {
        long amount = user.getPayAmount() + payAmount;
        String memberShip = user.getMemberShip().getValue();
        if (amount >= 0 && amount < 50000) {
            if (!memberShip.equals("BRONZE")) {
                user.setMemberShip(MemberShip.BRONZE);
            }
        } else if (amount >= 50000 && amount < 200000) {
            if (!memberShip.equals("SILVER")) {
                user.setMemberShip(MemberShip.SILVER);
            }
        } else if (amount >= 200000 && amount < 500000) {
            if (!memberShip.equals("GOLD")) {
                user.setMemberShip(MemberShip.GOLD);
            }
        } else if (amount >= 500000 && amount < 1000000) {
            if (!memberShip.equals("PLATINUM")) {
                user.setMemberShip(MemberShip.PLATINUM);
            }
        } else {
            user.setMemberShip(MemberShip.DIAMOND);
        }
        userRepository.save(user);

    }

    /**
     * 맴버쉽 상승에 필요한 금액
     **/
    public String memberShip(User user) {
        long amount = user.getPayAmount();
        if (amount >= 0 && amount < 50000) {
            return String.valueOf(50000 - amount);
        } else if (amount >= 50000 && amount < 200000) {
            return String.valueOf(200000 - amount);
        } else if (amount >= 200000 && amount < 500000) {
            return String.valueOf(500000 - amount);
        } else if (amount >= 500000 && amount < 1000000) {
            return String.valueOf(1000000 - amount);
        } else {
            return "최고 등급입니다.";
        }
    }


    /**
     * 권한 별 로그인
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalMember = userRepository.findById(username);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        User user = optionalMember.get();
        if (user.getUserClassification() == UserClassification.USER) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (user.getUserClassification() == UserClassification.ADMIN) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (user.getUserClassification() == UserClassification.SELLER) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BRAND"));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPassword(), grantedAuthorities);
    }

    /**
     * 캐쉬 충전
     **/
    public void cacheUp(String userId, long cache) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        user.setUserCache(user.getUserCache() + cache);
        userRepository.save(user);
    }

    /**
     * 유저 정보 수정
     **/
    public void userUpdate(User user, UserInput input) {
        if (input.getUserPhone() != null && !user.getUserPhone().equals(input.getUserPhone())) {
            user.setUserPhone(input.getUserPhone());
        }
        if (input.getDetailAdr() != null && !user.getAddress().getDetailAdr().equals(input.getDetailAdr())) {
            Address address = new Address(input.getZipCode(), input.getStreetAdr(), input.getDetailAdr());
            user.setAddress(address);
        }
        userRepository.save(user);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void userStatusUpdate(String userId, UserStatus userStatus) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        user.setUserStatus(userStatus);
        userRepository.save(user);
    }
}

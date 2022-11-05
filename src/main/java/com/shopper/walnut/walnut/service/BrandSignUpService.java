package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.BrandRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BrandSignUpService {
    private final BrandRepository brandRepository;
    /**
     * userRepository는 user 계정과 brand계정끼리 충돌을 방지하기 위해 체크하는 역할로 사용
     */
    private final UserRepository userRepository;

    public void register(BrandInput parameter){
        Optional<User> optionalUser = userRepository.findById(parameter.getBrandLoginId());
        Optional<Brand> optionalBrand = brandRepository.findByBrandName(parameter.getBrandName());
        if(optionalBrand.isPresent() && optionalUser.isPresent()){
            throw new BrandRegisterException(ErrorCode.BRAND_ALREADY_EXIST);
        }
        String encPassword = BCrypt.hashpw(parameter.getBrandPassword(), BCrypt.gensalt());
        Address address = new Address(parameter.getZipCode(),parameter.getStreetAdr(),parameter.getDetailAdr());
        Brand brand = Brand.of(parameter,encPassword, address);
        brandRepository.save(brand);
        userRepository.save(User.toSign(brand));
    }

}

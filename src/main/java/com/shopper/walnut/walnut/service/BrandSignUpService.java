package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.error.BrandAlreadyExist;
import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.*;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandSignUpService {
    private final BrandRepository brandRepository;

    private final UserRepository userRepository;


    /** 브랜드 입점**/
    public void register(BrandInput parameter){
        Optional<User> optionalUser = userRepository.findById(parameter.getBrandLoginId());
        Optional<Brand> optionalBrand = brandRepository.findByBrandName(parameter.getBrandName());
        if(optionalBrand.isPresent() && optionalUser.isPresent()){
            throw new BrandAlreadyExist();
        }
        String encPassword = BCrypt.hashpw(parameter.getBrandPassword(), BCrypt.gensalt());
        Address address = new Address(parameter.getZipCode(),parameter.getStreetAdr(),parameter.getDetailAdr());
        Brand brand = Brand.of(parameter,encPassword, address);
        brandRepository.save(brand);
        userRepository.save(User.toSign(brand));
    }

}

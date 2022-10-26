package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.BrandRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.input.BrandStatus;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.input.UserStatus;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandSignUpService {
    private final BrandRepository brandRepository;
    private final UserSignUpService userSignUpService;

    public boolean register(BrandInput parameter){
        Optional<Brand> optionalBrand = brandRepository.findByBrandName(parameter.getBrandName());
        if(optionalBrand.isPresent()){
            throw new BrandRegisterException(ErrorCode.BRAND_ALREADY_EXIST);
        }
        Address address = new Address(parameter.getZipCode(),parameter.getStreetAdr(),parameter.getDetailAdr());
        Brand brand = Brand.builder()
                .brandName(parameter.getBrandName())
                .brandPhone(parameter.getBrandPhone())
                .brandLoginId(parameter.getBrandLoginId())
                .brandPassword(parameter.getBrandPassword())
                .address(address)
                .brandRegDt(LocalDateTime.now())
                .brandStatus(BrandStatus.MEMBER_STATUS_REQ)
                .fileName(parameter.getFileName())
                .urlFileName(parameter.getUrlFileName())
                .build();
        brandRepository.save(brand);
        UserInput user = UserInput.builder()
                .userId(parameter.getBrandLoginId())
                .userPassword(parameter.getBrandPassword())
                .userName("브랜드명: " + parameter.getBrandName())
                .zipCode(parameter.getZipCode())
                .streetAdr(parameter.getStreetAdr())
                .detailAdr(parameter.getDetailAdr())
                .userPhone(parameter.getBrandPhone())
                .userEmail(parameter.getBrandName()+"@walnut.com")
                .build();
        userSignUpService.register(user, false);
        return true;
    }

    public BrandDto getById(int id) {
        return brandRepository.findById(id).map(BrandDto::of).orElse(null);
    }


}

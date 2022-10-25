package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.BrandRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.input.BrandStatus;
import com.shopper.walnut.walnut.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandSignUpService {
    private final BrandRepository brandRepository;

    public boolean register(BrandInput parameter){
        Optional<Brand> optionalBrand = brandRepository.findByBrandName(parameter.getBrandName());
        if(optionalBrand.isPresent()){
            throw new BrandRegisterException(ErrorCode.BRAND_ALREADY_EXIST);
        }
        Brand brand = Brand.builder()
                .brandName(parameter.getBrandName())
                .brandPhone(parameter.getBrandPhone())
                .brandRegDt(LocalDateTime.now())
                .brandStatus(BrandStatus.MEMBER_STATUS_REQ)
                .fileName(parameter.getFileName())
                .urlFileName(parameter.getUrlFileName())
                .build();
        brandRepository.save(brand);
        return true;
    }

    public BrandDto getById(int id) {
        return brandRepository.findById(id).map(BrandDto::of).orElse(null);
    }


}

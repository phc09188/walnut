package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository repository;

    public boolean set(BrandInput parameter) {
        Optional<Brand> optionalBrand = repository.findById(parameter.getBrandId());
        if(!optionalBrand.isPresent()){
            return false;
        }
        Brand brand = optionalBrand.get();
        if(parameter.getUrlFileName() != null){
            brand.setUrlFileName(parameter.getUrlFileName());
        }
        brand.setBrandName(parameter.getBrandName());
        brand.setBrandPhone(parameter.getBrandPhone());
        brand.setFileName(parameter.getFileName());
        brand.setUrlFileName(parameter.getUrlFileName());

        repository.save(brand);
        return true;
    }

}

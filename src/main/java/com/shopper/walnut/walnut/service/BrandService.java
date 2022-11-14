package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.conponents.MailComponents;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository repository;
    private final MailComponents mailComponents;

    /** 브랜드 정보 수정 **/
    public boolean set(BrandInput parameter) {
        Optional<Brand> optionalBrand = repository.findById(parameter.getBrandId());
        if(optionalBrand.isEmpty()){
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
    /** 제고소진 및 상품 상태 변경시 이메일 보내기 **/
    public void sendEmail(BrandItem brandItem) {
        if(brandItem.getSaleStatus().equals(ItemStatus.ITEM_STATUS_ING)){
            return;
        }
        String email = brandItem.getBrand().getBrandEmail();
        String subject = brandItem.getBrand().getBrandName()+" 상품 상태 알림";
        String text = "<h3>" + brandItem.getItemName() +  " 상품이 "+ brandItem.getSaleStatus() +"상태에 있습니다.</h3>" +
                " <p> 물품 상태를 확인해주세요. </p>" +
                "<a href='http://localhost:8080/'>호두로 이동하기</a>";
        mailComponents.sendMail(email, subject, text);
    }
}

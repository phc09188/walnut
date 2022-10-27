package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.input.BrandStatus;
import com.shopper.walnut.walnut.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AdminController {
    private final BrandRepository brandRepository;

    @GetMapping("/admin/main.do")
    public String index(){
        return "admin/main";
    }
    @GetMapping("/admin/user/list.do")
    public String userList(){
        return "admin/user/list";
    }
    @GetMapping("/admin/category/list.do")
    public String categoryList(){
        return "admin/category/list";
    }
    @GetMapping("/admin/brand/list.do")
    public String brandList(Model model){
        List<Brand> allBrand = brandRepository.findAll();
        List<BrandDto> list = BrandDto.of(allBrand);
        model.addAttribute("brandList", list);
        return "admin/brand/list";
    }
    @GetMapping("/admin/brand/approve.do")
    public String brandApprove(Model model){
        List<Brand> notOkBrand = brandRepository.findAllByBrandStatus(BrandStatus.MEMBER_STATUS_REQ);
        List<BrandDto> list = BrandDto.of(notOkBrand);
        System.out.println(list.size());
        model.addAttribute("list", list);

        return "admin/brand/approve";
    }
    @PostMapping("/admin/approve/start.do")
    public String approve(BrandDto dto){
        Optional<Brand> optionalBrand = brandRepository.findByBrandName(dto.getBrandName());
        if(!optionalBrand.isPresent()){
            throw new RuntimeException("브랜드가 없습니다.");
        }
        Brand brand = optionalBrand.get();
        brand.setBrandStatus(BrandStatus.MEMBER_STATUS_ING);
        brand.setBrandOkDt(LocalDateTime.now());
        brandRepository.save(brand);
        return "redirect:/admin/approve.do";
    }
}

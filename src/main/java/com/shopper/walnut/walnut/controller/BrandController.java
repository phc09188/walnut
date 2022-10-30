package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.BrandItemException;
import com.shopper.walnut.walnut.exception.BrandRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.dto.BrandItemDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.service.BrandItemService;
import com.shopper.walnut.walnut.service.BrandSignUpService;
import com.shopper.walnut.walnut.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BrandController {
    private final BrandSignUpService signUpService;
    private final BrandRepository brandRepository;
    private final BrandItemRepository brandItemRepository;
    private final BrandItemService brandItemService;
    private final CategoryService categoryService;

    @GetMapping("/brand/register")
    public String add(Model model) {
        model.addAttribute("brandForm",new BrandDto());
        return "brand/add";
    }

    @PostMapping("/brand/register.do")
    public String addSubmit(Model model, HttpServletRequest request
            , MultipartFile file
            , @Validated BrandInput parameter) {

        String saveFilename = "";
        String urlFilename = "";

        if (file != null) {
            String originalFilename = file.getOriginalFilename();

            String baseLocalPath = "/Users/chandle/Downloads/walnut/src/main/resources/static/brand/businessRegistration";
            String baseUrlPath = "/brand";

            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
                log.info("############################ - 1");
                log.info(e.getMessage());
            }
        }

        parameter.setFileName(saveFilename);
        parameter.setUrlFileName(urlFilename);


        signUpService.register(parameter);
        return "redirect:/";
    }
    @GetMapping("/brand/main/detail.do")
    public String brandMain(Model model, @AuthenticationPrincipal User user){
        String brandLogInId = user.getUsername();
        Optional<Brand> optionalBrand = brandRepository.findByBrandLoginId(brandLogInId);
        if(!optionalBrand.isPresent()){
            throw new BrandRegisterException(ErrorCode.BRAND_NOT_FOUND);
        }
        Brand brand = optionalBrand.get();
        model.addAttribute("brandInfo", brand);

        return "brand/main/detail";

    }

    @GetMapping("/brand/main/item.do")
    public String brandItemList(Model model, @AuthenticationPrincipal User user){
        Optional<Brand> brand = brandRepository.findByBrandLoginId(user.getUsername());
        List<BrandItem> brandItemList = brandItemRepository.findAllByBrand(brand.get());
        model.addAttribute("brandItemList",brandItemList);

        return "brand/main/item";
    }

    private String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFilename) {

        LocalDate now = LocalDate.now();

        String[] dirs = {
                String.format("%s/%d/", baseLocalPath,now.getYear()),
                String.format("%s/%d/%02d/", baseLocalPath, now.getYear(),now.getMonthValue()),
                String.format("%s/%d/%02d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())};

        String urlDir = String.format("%s/%d/%02d/%02d/", baseUrlPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        for(String dir : dirs) {
            File file = new File(dir);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }

        String fileExtension = "";
        if (originalFilename != null) {
            int dotPos = originalFilename.lastIndexOf(".");
            if (dotPos > -1) {
                fileExtension = originalFilename.substring(dotPos + 1);
            }
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String newFilename = String.format("%s%s", dirs[2], uuid);
        String newUrlFilename = String.format("%s%s", urlDir, uuid);
        if (fileExtension.length() > 0) {
            newFilename += "." + fileExtension;
            newUrlFilename += "." + fileExtension;
        }

        return new String[]{newFilename, newUrlFilename};
    }

    @GetMapping("/brand/main/item-add.do")
    public String itemAddForm(Model model){
        model.addAttribute("category",categoryService.list());
        model.addAttribute("detail", new BrandItemDto());

        return "brand/main/item-add";
    }

    @PostMapping("/brand/main/item/add.do")
    public String itemAdd(@AuthenticationPrincipal User user,
                          MultipartFile file,
                          BrandItemInput parameter){
        String saveFilename = "";
        String urlFilename = "";

        if (file != null) {
            String originalFilename = file.getOriginalFilename();

            String baseLocalPath = "/Users/chandle/walnut/src/main/resources/static/brand/itemImg";
            String baseUrlPath = "/brand";

            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
                log.info("############################ - 1");
                log.info(e.getMessage());
            }
        }
        parameter.setFileName(saveFilename);
        parameter.setFileUrl(urlFilename);
        Optional<Brand> optionalBrand = brandRepository.findByBrandLoginId(user.getUsername());
        brandItemService.add(parameter, optionalBrand.get());
        return "redirect:/brand/main/item.do";

    }


    @GetMapping("/brand/item/itemDetail")
    public String itemDetail(@RequestParam("id")String id,Model model){
        Integer brandItemId = Integer.parseInt(id);
        Optional<BrandItem> optionalBrandItem=brandItemRepository.findById(brandItemId);
        if(optionalBrandItem.isEmpty()){
            throw new BrandItemException(ErrorCode.BRANDITEM_NOT_EXIST);
        }
        BrandItem brandItem = optionalBrandItem.get();
        model.addAttribute("brandItem",brandItem);

        return "brand/main/itemDetail";
    }
}

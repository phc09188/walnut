package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.service.BrandService;
import com.shopper.walnut.walnut.service.BrandSignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BrandController {
    private final BrandSignUpService signUpService;
    private final BrandService service;
//    @GetMapping(value = {"/brand/add.do", "/brand/edit.do"})
//    public String add(Model model, HttpServletRequest request, BrandInput parameter) {
//        BrandDto details = new BrandDto();
//        model.addAttribute("detail",details );
//        return "brand/add";
//    }

    @GetMapping(value = {"/brand/add.do", "/brand/edit.do"})
    public String add(Model model, HttpServletRequest request
            , BrandInput parameter) {


        boolean editMode = request.getRequestURI().contains("/edit.do");
        BrandDto detail = new BrandDto();

        if (editMode) {
            int id = parameter.getBrandId();
            BrandDto existCourse = signUpService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existCourse;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);

        return "brand/add";
    }

    @PostMapping(value = {"/brand/add.do", "/brand/edit.do"})
    public String addSubmit(Model model, HttpServletRequest request
            , MultipartFile file
            , BrandInput parameter) {

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

        boolean editMode = request.getRequestURI().contains("/edit.do");

        if (editMode) {
            int id = parameter.getBrandId();
            BrandDto existCourse = signUpService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "브랜드 정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = service.set(parameter);
        } else {
            boolean result = signUpService.register(parameter);
        }
        return "redirect:/";
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
}

package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.BrandRegisterException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.dto.BrandItemDto;
import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.model.input.OrderInput;
import com.shopper.walnut.walnut.model.status.DeliveryStatus;
import com.shopper.walnut.walnut.model.status.OrderStatus;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/brand")
public class BrandController {
    private final OrderRepository orderRepository;
    private final BrandRepository brandRepository;
    private final BrandItemRepository brandItemRepository;

    private final BrandSignUpService signUpService;
    private final BrandItemService brandItemService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;

    /** 메인페이지 **/
    @GetMapping("/main/detail.do")
    public String brandMain(Model model, @AuthenticationPrincipal User user){
        String brandLogInId = user.getUsername();
        Optional<Brand> optionalBrand = brandRepository.findByBrandLoginId(brandLogInId);
        if(optionalBrand.isEmpty()){
            throw new BrandRegisterException(ErrorCode.BRAND_NOT_FOUND);
        }
        Brand brand = optionalBrand.get();
        model.addAttribute("brandInfo", brand);

        return "brand/main/detail";

    }
    /** 입점 신청 폼 **/
    @GetMapping("/register")
    public String add(Model model) {
        model.addAttribute("brandForm",new BrandDto());
        return "brand/add";
    }
    /** 입점 신청 **/
    @PostMapping("/register.do")
    public String addSubmit(MultipartFile file
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

    /** 파일 저장 유틸 **/
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
    /** 아이템 리스트 출력 **/
    @GetMapping("/main/item.do")
    public String brandItemList(Model model, @AuthenticationPrincipal User user){
        Optional<Brand> brand = brandRepository.findByBrandLoginId(user.getUsername());
        List<BrandItem> brandItemList = brandItemRepository.findAllByBrand(brand.get());
        model.addAttribute("brandItemList",brandItemList);

        return "brand/main/item";
    }

    /** 아이템 추가 or Edit **/
    @GetMapping(value = {"/main/item-add.do", "/main/item-edit.do"})
    public String itemAddForm(HttpServletRequest req,Model model, BrandItemDto dto){
        boolean isEdit = req.getRequestURI().contains("/item-edit");
        BrandItemDto dto2 = new BrandItemDto();
        if(isEdit){
            long id = dto.getBrandItemId();
            BrandItemDto item = brandItemService.getById(id);
            if(item == null){
                model.addAttribute("message", "상품정보가 없습니다.");
                return "common/error";
            }
            dto2 = item;
        }
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("category",categoryService.list());
        model.addAttribute("detail", dto2);

        return "brand/main/item-add";
    }
    /**상품 등록**/
    @PostMapping("/main/item/add.do")
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
        Category category = categoryService.findCategoryName(parameter.getSubCategoryName());
        parameter.setCategoryName(category.getCategoryName());
        Optional<Brand> optionalBrand = brandRepository.findByBrandLoginId(user.getUsername());
        brandItemService.add(parameter, optionalBrand.get());
        return "redirect:/brand/main/item.do";
    }
    /**주문리스트**/
    @GetMapping("/main/orderList")
    public String orderList(@AuthenticationPrincipal User logInUser
            ,@ModelAttribute("orderInput") OrderInput orderInput, Model model){
        Brand brand = brandRepository.findByBrandLoginId(logInUser.getUsername()).get();
        List<Order> orders = orderService.findAllByString(orderInput, brand);
        model.addAttribute("orders", orders);
        return "/brand/main/orderList";
    }

    /** 매출 확인 **/
    @GetMapping("/main/account.do")
    public String account(@AuthenticationPrincipal User logInUser, Model model){
        Brand brand = brandRepository.findByBrandLoginId(logInUser.getUsername()).get();
        List<BrandItem> items = brandItemRepository.findAllByBrand(brand);
        long amount =  brandItemService.findTotalAmount(items);
        model.addAttribute("items", items);
        model.addAttribute("total", amount);
        return "/brand/main/account";
    }

    /** 배송 상태 변경 **/
    @PostMapping("/main/order/edit.do")
    public String deliveryEdit(@RequestParam Long orderId, @RequestParam DeliveryStatus status){
        Order order = orderRepository.findById(orderId).get();
//        DeliveryStatus deliveryStatus =  orderService.findDeliveryStatus(status);
        order.getDelivery().setStatus(status);
        if(status == DeliveryStatus.COMPLETE){
            order.setStatus(OrderStatus.COMPLETE);
            order.getDelivery().setDeliverySuccessDt(LocalDateTime.now());
        }
        orderRepository.save(order);

        return "redirect:/brand/main/orderList";
    }





}

package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.BrandNotFound;
import com.shopper.walnut.walnut.exception.error.OrderNotFound;
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
import com.shopper.walnut.walnut.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;


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
    private final FileUtil fileUtil;

    /**
     * 메인페이지
     **/
    @GetMapping("/main/detail.do")
    public String brandMain(Model model, @AuthenticationPrincipal User user) {
        Brand brand = brandRepository.findByBrandLoginId(user.getUsername())
                .orElseThrow(BrandNotFound::new);
        model.addAttribute("brandInfo", brand);

        return "brand/main/detail";

    }

    /**
     * 입점 신청 폼
     **/
    @GetMapping("/register")
    public String add(Model model) {
        model.addAttribute("brandForm", new BrandDto());
        return "brand/add";
    }

    /**
     * 입점 신청
     **/
    @PostMapping("/register.do")
    public String addSubmit(MultipartFile file
            , @Validated BrandInput parameter) {

        String[] utils = fileUtil.FileUrl(file, "brand").split(",");

        parameter.setFileName(utils[0]);
        parameter.setUrlFileName(utils[1]);
        signUpService.register(parameter);
        return "redirect:/";
    }


    /**
     * 아이템 리스트 출력
     **/
    @GetMapping("/main/item.do")
    public String brandItemList(Model model, @AuthenticationPrincipal User user) {
        Brand brand = brandRepository.findByBrandLoginId(user.getUsername())
                .orElseThrow(BrandNotFound::new);
        List<BrandItem> brandItemList = brandItemRepository.findAllByBrand(brand);
        model.addAttribute("brandItemList", brandItemList);

        return "brand/main/item";
    }

    /**
     * 아이템 추가 or Edit
     **/
    @GetMapping(value = {"/main/item-add.do", "/main/item-edit.do"})
    public String itemAddForm(HttpServletRequest req, Model model, BrandItemDto dto) {
        boolean isEdit = req.getRequestURI().contains("/item-edit");
        BrandItemDto dto2 = new BrandItemDto();
        if (isEdit) {
            long id = dto.getBrandItemId();
            BrandItemDto item = brandItemService.getById(id);
            if (item == null) {
                model.addAttribute("message", "상품정보가 없습니다.");
                return "common/error";
            }
            dto2 = item;
        }
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("category", categoryService.list());
        model.addAttribute("detail", dto2);

        return "brand/main/item-add";
    }

    /**
     * 상품 등록
     **/
    @PostMapping("/main/item/add.do")
    public String itemAdd(@AuthenticationPrincipal User user,
                          MultipartFile file,
                          BrandItemInput parameter) {
        String[] utils = fileUtil.FileUrl(file, "item").split(",");
        parameter.setFileName(utils[0]);
        parameter.setFileUrl(utils[1]);
        Category category = categoryService.findCategoryName(parameter.getSubCategoryName());
        parameter.setCategoryName(category.getCategoryName());
        Brand brand = brandRepository.findByBrandLoginId(user.getUsername())
                .orElseThrow(BrandNotFound::new);
        brandItemService.add(parameter, brand);
        return "redirect:/brand/main/item.do";
    }

    /**
     * 주문리스트
     **/
    @GetMapping("/main/orderList")
    public String orderList(@AuthenticationPrincipal User logInUser
            , @ModelAttribute("orderInput") OrderInput orderInput, Model model) {
        Brand brand = brandRepository.findByBrandLoginId(logInUser.getUsername())
                .orElseThrow(BrandNotFound::new);
        List<Order> orders = orderService.findAllByString(orderInput, brand);
        model.addAttribute("orders", orders);
        return "/brand/main/orderList";
    }

    /**
     * 매출 확인
     **/
    @GetMapping("/main/account.do")
    public String account(@AuthenticationPrincipal User logInUser, Model model) {
        Brand brand = brandRepository.findByBrandLoginId(logInUser.getUsername())
                .orElseThrow(BrandNotFound::new);
        List<BrandItem> items = brandItemRepository.findAllByBrand(brand);
        long amount = brandItemService.findTotalAmount(items);
        model.addAttribute("items", items);
        model.addAttribute("total", amount);
        return "/brand/main/account";
    }

    /**
     * 배송 상태 변경
     **/
    @PostMapping("/main/order/edit.do")
    public String deliveryEdit(@RequestParam Long orderId, @RequestParam DeliveryStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        order.getDelivery().setStatus(status);
        if (status == DeliveryStatus.COMPLETE) {
            order.setStatus(OrderStatus.COMPLETE);
            order.getDelivery().setDeliverySuccessDt(LocalDateTime.now());
        }
        orderRepository.save(order);

        return "redirect:/brand/main/orderList";
    }
}

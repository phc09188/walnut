package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.*;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.dto.CategoryDto;
import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.model.status.BrandStatus;
import com.shopper.walnut.walnut.model.input.CategoryInput;
import com.shopper.walnut.walnut.model.status.EventStatus;
import com.shopper.walnut.walnut.model.status.UserStatus;
import com.shopper.walnut.walnut.repository.*;
import com.shopper.walnut.walnut.service.QnAService;
import com.shopper.walnut.walnut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final UserService userService;
    private final QnAService qnAService;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final QnARepository qnARepository;
    private final EventRepository eventRepository;

    /**
     * 관리자 메인
     **/
    @GetMapping("/main.do")
    public String index() {
        return "admin/main";
    }

    /**
     * 관리자 유저 관리 페이지
     **/
    @GetMapping("/user/list.do")
    public String userList() {
        return "admin/user/list";
    }

    /**
     * 관리자 브랜드 관리 페이지
     **/
    @GetMapping("/brand/list.do")
    public String brandList(Model model) {
        List<Brand> allBrand = brandRepository.findAll();
        List<BrandDto> list = BrandDto.of(allBrand);
        model.addAttribute("brandList", list);
        return "admin/brand/list";
    }

    /**
     * 관리자 브랜드 입점 승인 페이지
     **/
    @GetMapping("/brand/approve.do")
    public String brandApprove(Model model) {
        List<Brand> notOkBrand = brandRepository.findAllByBrandStatus(BrandStatus.MEMBER_STATUS_REQ);
        List<BrandDto> list = BrandDto.of(notOkBrand);
        System.out.println(list.size());
        model.addAttribute("list", list);

        return "admin/brand/approve";
    }

    /**
     * 브랜드 입점 승인
     **/
    @PostMapping("/approve/start.do")
    public String approve(BrandDto dto) {
        Brand brand = brandRepository.findByBrandName(dto.getBrandName())
                .orElseThrow(BrandNotFound::new);
        brand.setBrandStatus(BrandStatus.MEMBER_STATUS_ING);
        brand.setBrandOkDt(LocalDateTime.now());
        brandRepository.save(brand);
        return "redirect:/admin/approve.do";
    }

    /**
     * 카테고리 리스트 출력
     **/
    @GetMapping("/category/list.do")
    public String categoryList(Model model) {
        List<Category> list = categoryRepository.findAll();
        model.addAttribute("list", list);
        return "admin/category/list";
    }

    /**
     * 카테고리 삭제
     **/
    @PostMapping("/category/delete.do")
    public String deleteCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getSubCategoryName())
                .orElseThrow(CategoryNotExist::new);
        categoryRepository.delete(category);
        return "redirect:/admin/category/list";
    }

    /**
     * 카테고리 추가 폼
     **/
    @GetMapping("/category/add")
    public String categoryAddForm(Model model) {
        model.addAttribute("categoryForm", new CategoryDto());
        return "admin/category/add";
    }

    /**
     * 카테고리 추가
     **/
    @PostMapping("/category/addCategory.do")
    public String categoryAdd(@Validated CategoryInput input) {
        Optional<Category> categoryOptional = categoryRepository.findById(input.getSubCategoryName());
        if (categoryOptional.isPresent()) {
            throw new CategoryAlreadyExist();
        }
        Category category = Category.of(input);
        categoryRepository.save(category);
        return "redirect:/admin/category/list.do";
    }

    /**
     * 회원 관리
     **/
    @GetMapping("/member/list.do")
    public String memberList(Model model) {
        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);

        return "/admin/user/list";
    }

    @PostMapping("/member/stateUpdate")
    public String memberStatusUpdate(@RequestParam String userId, @RequestParam UserStatus userStatus) {
        userService.userStatusUpdate(userId,userStatus);

        return "redirect:/admin/user/list.do";
    }

    /**
     * 미승인 qna 리스트
     **/
    @GetMapping("/qna/list.do")
    public String qnaList(Model model) {
        List<QnA> list = qnAService.getNotAnswer();
        model.addAttribute("list", list);

        return "/admin/qna/list";
    }

    /**
     * 답변 폼
     **/
    @GetMapping("/qna/detail")
    public String qnaList(@RequestParam long qnaId, Model model) {
        QnA qna = qnARepository.findById(qnaId).orElseThrow(QnaNotFound::new);
        model.addAttribute(qna);
        return "/admin/qna/detail";
    }

    /**
     * qna 승인
     **/
    @PostMapping("/qna/submit")
    public String qnaSubmit(@RequestParam String answer, @RequestParam long qnaId) {
        QnA qna = qnARepository.findById(qnaId).orElseThrow(QnaNotFound::new);

        qna.setAnswer(answer);
        qnARepository.save(qna);

        return "redirect:/admin/qna/list.do";
    }

    /**
     * 승인완료가 끝나지 않은 eventList
     **/
    @GetMapping("/event/eventList")
    public String eventList(Model model) {
        List<Event> list = eventRepository.findAllByStatus(EventStatus.REQ);

        model.addAttribute("list", list);
        return "/admin/event/eventList";
    }

    /**
     * 이벤트 정보
     **/
    @GetMapping("/event/info")
    public String eventInfo(Model model, @RequestParam Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(EventException::new);

        model.addAttribute("event", event);

        return "/admin/event/info";
    }

    /**
     * 이벤트 승인
     **/
    @Cacheable(key = "#eventId", value = CacheKey.EVENT_SUBMIT)
    @PostMapping("/event/submit.do")
    public String eventSubmit(@RequestParam Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(EventException::new);
        event.setStatus(EventStatus.COMPLETE);

        eventRepository.save(event);
        return "redirect:/admin/event/eventList";
    }
}

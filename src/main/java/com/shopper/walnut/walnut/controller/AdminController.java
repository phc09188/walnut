package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.CategoryException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.dto.BrandDto;
import com.shopper.walnut.walnut.model.dto.CategoryDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.status.BrandStatus;
import com.shopper.walnut.walnut.model.input.CategoryInput;
import com.shopper.walnut.walnut.model.status.UserStatus;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /**관리자 메인**/
    @GetMapping("/main.do")
    public String index(){
        return "admin/main";
    }

    /**관리자 유저 관리 페이지 **/
    @GetMapping("/user/list.do")
    public String userList(){
        return "admin/user/list";
    }

    /**관리자 브랜드 관리 페이지**/
    @GetMapping("/brand/list.do")
    public String brandList(Model model){
        List<Brand> allBrand = brandRepository.findAll();
        List<BrandDto> list = BrandDto.of(allBrand);
        model.addAttribute("brandList", list);
        return "admin/brand/list";
    }

    /**관리자 브랜드 입점 승인 페이지**/
    @GetMapping("/brand/approve.do")
    public String brandApprove(Model model){
        List<Brand> notOkBrand = brandRepository.findAllByBrandStatus(BrandStatus.MEMBER_STATUS_REQ);
        List<BrandDto> list = BrandDto.of(notOkBrand);
        System.out.println(list.size());
        model.addAttribute("list", list);

        return "admin/brand/approve";
    }

    /** 브랜드 입점 승인 **/
    @PostMapping("/approve/start.do")
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
    /** 카테고리 리스트 출력 **/
    @GetMapping("/category/list.do")
    public String categoryList(Model model){
        List<Category> list =  categoryRepository.findAll();
        model.addAttribute("list", list);
        return "admin/category/list";
    }
    /**카테고리 삭제**/
    @PostMapping("/category/delete.do")
    public String deleteCategory(CategoryDto categoryDto){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getSubCategoryName());
        if(!optionalCategory.isPresent()){
            throw new CategoryException(ErrorCode.CATEGORY_NOT_EXIST);
        }
        Category category = optionalCategory.get();
        categoryRepository.delete(category);
        return "redirect:/admin/category/list";
    }
    /** 카테고리 추가 폼 **/
    @GetMapping("/category/add")
    public String categoryAddForm(Model model){
        model.addAttribute("categoryForm", new CategoryDto());
        return "admin/category/add";
    }

    /**카테고리 추가**/
    @PostMapping("/category/addCategory.do")
    public String categoryAdd(Model model, HttpServletRequest request, @Validated CategoryInput categoryDto){
        Optional<Category> categoryOptional = categoryRepository.findById(categoryDto.getSubCategoryName());
        if(categoryOptional.isPresent()){
            throw new CategoryException(ErrorCode.CATEGORY_ALREADY_EXIST);
        }
        Category category = Category.builder()
                .subCategoryName(categoryDto.getSubCategoryName())
                .categoryName(categoryDto.getCategoryName())
                .build();
        categoryRepository.save(category);
        return "redirect:/admin/category/list.do";
    }

    /**회원 관리**/
    @GetMapping("/member/list.do")
    public String memberList(Model model){
        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);

        return "/admin/user/list";
    }

    @PostMapping("/member/stateUpdate")
    public String memberStatusUpdate(@RequestParam String userId, @RequestParam UserStatus userStatus){
        User user= userRepository.findById(userId).get();
        user.setUserStatus(userStatus);
        userRepository.save(user);

        return "redirect:/admin/user/list.do";
    }
}

package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.QnaNotFound;
import com.shopper.walnut.walnut.exception.error.UserNotFound;
import com.shopper.walnut.walnut.model.entity.QnA;
import com.shopper.walnut.walnut.model.input.QnaInput;
import com.shopper.walnut.walnut.model.type.QnaType;
import com.shopper.walnut.walnut.repository.QnARepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import com.shopper.walnut.walnut.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnAController {
    private final QnAService qnAService;
    private final QnARepository qnARepository;
    private final UserRepository userRepository;

    /** QNA리스트 & 메인페이지 **/
    @GetMapping("/main/info")
    public String qnaMain(Model model, @RequestParam QnaType type){
        List<QnA> qnas;
        if(type == null) {
            qnas = qnAService.searchMain();
        }else{
            qnas = qnAService.typeMain(type);
        }
        model.addAttribute("types", qnAService.getList());
        model.addAttribute("qnas", qnas);
        return "/qna/main";
    }

    /** QNA리스트 & 메인페이지 **/
    @GetMapping("/main")
    public String qnaMain(Model model){
        List<QnA> qnas = qnAService.searchMain();

        model.addAttribute("types", qnAService.getList());
        model.addAttribute("qnas", qnas);
        return "/qna/main";
    }

    /** qna 정보 **/
    @GetMapping("/detail")
    public String qnaDetail(Model model, @RequestParam long qnaId){
        Optional<QnA> optionalQnA =  qnARepository.findById(qnaId);
        if(optionalQnA.isEmpty()){
            throw new QnaNotFound();
        }

        model.addAttribute("qna",optionalQnA.get());
        return "/qna/detail";
    }
    /** qna 추가 폼 **/
    @GetMapping("/add")
    public String qnaAddForm(@ModelAttribute("qnaInput") QnaInput qnaInput, Model model){
        model.addAttribute("types", qnAService.getList());
        return "/qna/addForm";
    }

    /** qna 추가 **/
    @PostMapping("/add.do")
    public String qnaAdd(QnaInput input, @AuthenticationPrincipal User logInUser){
        Optional<com.shopper.walnut.walnut.model.entity.User> optionalUser = userRepository.findById(logInUser.getUsername());
        if(optionalUser.isEmpty()){
            throw new UserNotFound();
        }
        qnAService.add(optionalUser.get(),input);

        return "redirect:/qna/main";
    }

    /**  qna 삭제 **/
    @PostMapping("/delete.do")
    public String qnaDelete(@RequestParam long qnaId){
        qnAService.delete(qnaId);

        return "redirect:/qna/main";
    }

}

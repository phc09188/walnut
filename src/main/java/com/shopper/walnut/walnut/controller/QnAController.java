package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.QnAException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.QnA;
import com.shopper.walnut.walnut.model.input.QnaInput;
import com.shopper.walnut.walnut.repository.QnARepository;
import com.shopper.walnut.walnut.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnAController {
    private final QnAService qnAService;
    private final QnARepository qnARepository;

    /** QNA리스트 & 메잍페이지 **/
    @GetMapping("/main")
    public String qnaMain(Model model){
        List<QnA> qnas = qnAService.searchMain();

        model.addAttribute("qnas", qnas);
        return "/qna/main";
    }
    /** qna 정보 **/
    @GetMapping("/detail")
    public String qnaDetail(Model model, @RequestParam long qnaId){
        Optional<QnA> optionalQnA =  qnARepository.findById(qnaId);
        if(optionalQnA.isEmpty()){
            throw new QnAException(ErrorCode.QNA_NOT_FOUND);
        }

        model.addAttribute("qna",optionalQnA.get());
        return "/qna/detail";
    }
    /** qna 추가 폼 **/
    @GetMapping("/add")
    public String qnaAddForm(){

        return "/qna/addForm";
    }
    /** qna 추가 **/
    @PostMapping("/add.do")
    public String qnaAdd(QnaInput input){
        qnAService.add(input);

        return "redirect:/qna/main";
    }

    /**  qna 삭제 **/
    @PostMapping("/delete.do")
    public String qnaDelete(@RequestParam long qnaId){
        qnAService.delete(qnaId);

        return "redirect:/qna/main";
    }
}

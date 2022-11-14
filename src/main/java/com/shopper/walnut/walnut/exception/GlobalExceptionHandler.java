package com.shopper.walnut.walnut.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    protected ModelAndView handle(BasicException e){
        ModelAndView model = new ModelAndView("common/error");
        model.addObject("msg", e.getMessage());
        return model;
    }
}

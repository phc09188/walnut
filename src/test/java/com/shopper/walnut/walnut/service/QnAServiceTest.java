package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.QnA;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.QnaInput;
import com.shopper.walnut.walnut.model.type.QnaType;
import com.shopper.walnut.walnut.repository.QnARepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QnAServiceTest {
    @Autowired
    private QnAService service;
    @Autowired
    private QnARepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void qnaAdd(){
        //given
        User user = userRepository.findById("1111").get();
        QnaInput input = new QnaInput();
        input.setContent("adfe"); input.setSubject("sefae");
        //when
        service.add(user, input);
        //then
        Optional<QnA> optional =  repository.findBySubject(input.getSubject());
        assertNotNull(optional);
    }

    @Test
    void searchMainQna(){
        //when
        List<QnA> list =  service.searchMain();
        //then
        assertEquals(list.size(), 1);
    }

    @Test
    @Transactional
    void deleteQnA(){
        //given
        long qnaId =5;
        //when
        service.delete(qnaId);
        //then
        Optional<QnA> optional = repository.findById(Long.valueOf(5));
        boolean result = false;
        if(!optional.isPresent()) result=true;
        assertTrue(result);
    }
    @Test
    void typeList(){
        //given
        QnaType type = QnaType.PAY;
        //when
        List<QnA> list =  service.typeMain(type);
        //then
        assertEquals(1, list.size());
    }

    @Test
    void getQnaTypeList(){
        //when
        List<QnaType> list = service.getList();
        //then
        assertEquals(list.size(),4);
    }
    @Test
    void getNotAnswerList(){
        //when
        List<QnA> list =  service.getNotAnswer();
        //then
        if(list.size() >=1){
            assertEquals(list.get(0).getAnswer(), "");
        }else{
            assertEquals(list.size(),0);
        }
    }
}
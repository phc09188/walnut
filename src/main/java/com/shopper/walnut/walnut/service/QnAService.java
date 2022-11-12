package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.QnAException;
import com.shopper.walnut.walnut.exception.UserException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.QnA;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.QnaInput;
import com.shopper.walnut.walnut.model.status.QnaStatus;
import com.shopper.walnut.walnut.repository.QnARepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnAService {
    private final QnARepository qnARepository;
    private final UserRepository userRepository;

    /** 추가 **/
    public void add(QnaInput input) {
        Optional<User> optionalUser = userRepository.findById(input.getUserId());
        if(optionalUser.isEmpty()){
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        Optional<QnA> optional= qnARepository.findBySubject(input.getSubject());
        if(optional.isEmpty()){
            throw new QnAException(ErrorCode.QNA_ALREADY_EXIST);
        }
        QnA qna = new QnA(optionalUser.get(), input.getSubject(), input.getContent());
        qna.setType(input.getType());
        qnARepository.save(qna);
    }

    /** 메인 페이지에 띄울 qna만 반환**/
    public List<QnA> searchMain() {
        List<QnA> qnas = qnARepository.findAllByStatus(QnaStatus.MAIN);
        if(qnas.size() ==0 || qnas.isEmpty()){
            return new ArrayList<>();
        }
        return qnas;
    }

    /** 질문 삭제**/
    public void delete(long qnaId) {
        Optional<QnA> optional = qnARepository.findById(qnaId);
        if(optional.isEmpty()){
            throw new QnAException(ErrorCode.QNA_NOT_FOUND);
        }
        qnARepository.delete(optional.get());
    }
}

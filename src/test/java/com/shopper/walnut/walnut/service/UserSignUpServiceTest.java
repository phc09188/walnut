package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.repository.UserReceptionStatusRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserSignUpServiceTest {
    @Autowired
    private UserSignUpService service;
    private UserRepository userRepository;
    private UserReceptionStatusRepository userReceptionStatusRepository;

    @Test
    void register(){
        //given
        UserInput userInput = UserInput.builder()
                .userId("phc09188")
                .userName("박해찬")
                .userEmail("gocks0918@gmail.com")
                .userPassword("phc70766")
                .userRegistration("초당로 7번길 19")
                .userPhone("01083897076")
                .marketingYn(true)
                .privateYn(true)
                .payYn(true)
                .build();
        //when
        boolean result =service.register(userInput);
        //then
        assertTrue(result);
    }

}
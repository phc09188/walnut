package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.input.UserInput;
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

    @Test
    void register(){
        Address address = new Address("a","a","a");
        //given
        UserInput userInput = UserInput.builder()
                .userId("phc09188")
                .userName("박해찬")
                .userEmail("gocks0918@gmail.com")
                .userPassword("phc70766")
                .userPhone("01083897076")
                .zipCode("a")
                .streetAdr("a")
                .detailAdr("a")
                .marketingYn(true)
                .privateYn(true)
                .payYn(true)
                .build();
        //when
        service.register(userInput);
    }

}
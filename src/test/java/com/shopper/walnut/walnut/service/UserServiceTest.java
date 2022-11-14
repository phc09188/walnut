package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Address;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.status.MemberShip;
import com.shopper.walnut.walnut.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService service;
    @Autowired
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
    @Test
    @Transactional
    void emailAuth (){
        //given
        String uuid = "5debf94a-74ce-492f-bf23-a5a8d55db3dc";
        //when
        boolean result = service.emailAuth(uuid);

        //then
        assertTrue(result);
    }

    @Test
    @Transactional
    void memberShipUpdate(){
        //given
        User user = userRepository.findById("1111").get();
        long payAmount = 10000000;
        //when
        service.userMemberShipUpdate(user,payAmount);
        //then
        user = userRepository.findById("1111").get();
        assertEquals(user.getMemberShip(), MemberShip.DIAMOND);
    }

    @Test
    void setMemberShip(){
        //given
        User user = userRepository.findById("1111").get();
        //when
        String membership =  service.memberShip(user);
        //then
        assertEquals(membership,50000);
    }

    @Test
    @Transactional
    void cacheUp (){
        //given
        String userId ="1111"; long cacheAmount=10000;
        //when
        service.cacheUp(userId,cacheAmount);
        //then
        User user = userRepository.findById(userId).get();
        assertEquals(user.getUserCache(),10000);
    }

    @Test
    @Transactional
    void userUpdate(){
        //given
        User user = userRepository.findById("1111").get();
        UserInput input = UserInput.builder()
                .userPhone("01011112222")
                .streetAdr("111")
                .detailAdr("111")
                .zipCode("111")
                .build();
        //when
        service.userUpdate(user, input);
        //then
        user = userRepository.findById("1111").get();
        assertEquals(user.getUserPhone(), input.getUserPhone());

    }

}
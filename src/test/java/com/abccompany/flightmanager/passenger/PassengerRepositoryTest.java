package com.abccompany.flightmanager.passenger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PassengerRepositoryTest {

    @Autowired
    PassengerRepository passengerRepository;

    @Test
    void findByNameTest(){
        assertEquals("John Smith", passengerRepository.findByName("John Smith").get().getName());
    }

}
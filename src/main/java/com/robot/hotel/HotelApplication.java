package com.robot.hotel;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.domain.Room;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class, args);
    }

}

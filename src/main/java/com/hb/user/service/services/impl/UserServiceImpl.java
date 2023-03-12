package com.hb.user.service.services.impl;

import com.hb.user.service.entities.Hotel;
import com.hb.user.service.entities.Rating;
import com.hb.user.service.entities.User;
import com.hb.user.service.exceptions.ResourceNotFoundException;
import com.hb.user.service.repositories.UserRepository;
import com.hb.user.service.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service  // or: @Log @CommonsLog @Log4j @Log4j2 @XSlf4j
@Slf4j
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public User saveUser(User user)
    {
        // generates unique userid
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }


    @Override
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }


    @Override
    public User getUser(String userId)
    {
        // Get user from the database with the help of user repository

        User user = userRepository.findById(userId)
                             .orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on the server " + "having userid as: " + userId));

        // Fetch rating of the above user from RATING SERVICE
        // http://localhost:8083/ratings/users/093981f3-94cd-4823-84ec-f5b03512f22b

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);

        log.info("{} ", ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).collect(Collectors.toList());

        List<Rating> ratingList = ratings.stream().map(rating -> {

            // Fetch hotel information based on hotelId from the Rating using HOTEL SERVICE
            // api call to hotel service to get the hotel

            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            Hotel hotel = forEntity.getBody();
            log.info("Response status code: {}", forEntity.getStatusCode());


            // set the hotel to rating
            rating.setHotel(hotel);


            // return the rating

            return rating;

        }).collect(Collectors.toList());



        user.setRatings(ratingList);

        return user;
    }

}

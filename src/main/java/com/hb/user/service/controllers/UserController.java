package com.hb.user.service.controllers;

import com.hb.user.service.entities.User;
import com.hb.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserService userService;


    // create user

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        // To receive user data in the form of json, we use @RequestBody annotation,
        // all the data which is coming will get deserialized and Json object will get converted into this User object here

        User user1 = userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user1);

    }



    // get single user

    @GetMapping("/{userId}")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId)
    {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }



    // get all users

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }


}

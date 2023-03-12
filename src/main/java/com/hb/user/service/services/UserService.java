package com.hb.user.service.services;

import com.hb.user.service.entities.User;

import java.util.List;

public interface UserService
{
    // User operations


    // Create User
    User saveUser(User user);


    // Get All Users
    List<User> getAllUsers();



    // Get single user for given userId
    User getUser(String userId);



    //TODO: DELETE USER

    //TODO: UPDATE USER

}

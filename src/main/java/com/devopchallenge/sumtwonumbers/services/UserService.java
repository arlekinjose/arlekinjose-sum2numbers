package com.devopchallenge.sumtwonumbers.services;

import com.devopchallenge.sumtwonumbers.domain.User;
import com.devopchallenge.sumtwonumbers.exceptions.s2nAuthException;

public interface UserService {

    User validateUser(String email, String password) throws s2nAuthException;

    User registerUser(String firstName, String lastName, String email, String password) throws s2nAuthException;

}

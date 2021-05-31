package com.devopchallenge.sumtwonumbers.repositories;

import com.devopchallenge.sumtwonumbers.domain.User;
import com.devopchallenge.sumtwonumbers.exceptions.s2nAuthException;

public interface UserRepository {

    Integer create(String firstName, String lastName, String email, String password) throws s2nAuthException;

    User findByEmailAndPassword(String email, String password) throws s2nAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);

}

package com.devopchallenge.sumtwonumbers.repositories;

import com.devopchallenge.sumtwonumbers.domain.Sum;
import com.devopchallenge.sumtwonumbers.exceptions.s2nBadRequestException;
import com.devopchallenge.sumtwonumbers.exceptions.s2nResourceNotFoundException;

import java.util.List;

public interface SumRepository {

    List<Sum> findAll(Integer userId) throws s2nResourceNotFoundException;

    Sum findById(Integer userId, Integer sumId) throws s2nResourceNotFoundException;

    Integer create(Integer userId, Integer sum1, Integer sum2) throws s2nBadRequestException;

}

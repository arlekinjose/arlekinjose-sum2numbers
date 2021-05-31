package com.devopchallenge.sumtwonumbers.services;

import com.devopchallenge.sumtwonumbers.domain.Sum;
import com.devopchallenge.sumtwonumbers.exceptions.s2nBadRequestException;
import com.devopchallenge.sumtwonumbers.exceptions.s2nResourceNotFoundException;

import java.util.List;

public interface SumService {

    List<Sum> fetchAllSums(Integer userId);

    Sum fetchSumById(Integer userId, Integer sumId) throws s2nResourceNotFoundException;

    Sum addSum(Integer userId, Integer sum1, Integer sum2) throws s2nBadRequestException;

}

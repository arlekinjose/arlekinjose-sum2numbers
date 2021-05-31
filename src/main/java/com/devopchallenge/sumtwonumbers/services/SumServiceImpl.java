package com.devopchallenge.sumtwonumbers.services;

import com.devopchallenge.sumtwonumbers.domain.Sum;
import com.devopchallenge.sumtwonumbers.exceptions.s2nBadRequestException;
import com.devopchallenge.sumtwonumbers.exceptions.s2nResourceNotFoundException;
import com.devopchallenge.sumtwonumbers.repositories.SumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SumServiceImpl implements SumService {

    @Autowired
    SumRepository sumRepository;

    @Override
    public List<Sum> fetchAllSums(Integer userId) {
        return sumRepository.findAll(userId);
    }

    @Override
    public Sum fetchSumById(Integer userId, Integer sumId) throws s2nResourceNotFoundException {
        return sumRepository.findById(userId, sumId);
    }

    @Override
    public Sum addSum(Integer userId, Integer sum1, Integer sum2) throws s2nBadRequestException {
        int sumId = sumRepository.create(userId, sum1, sum2);
        return sumRepository.findById(userId, sumId);
    }
}

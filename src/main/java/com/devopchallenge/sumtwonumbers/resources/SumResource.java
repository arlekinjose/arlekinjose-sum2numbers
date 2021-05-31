package com.devopchallenge.sumtwonumbers.resources;

import com.devopchallenge.sumtwonumbers.domain.Sum;
import com.devopchallenge.sumtwonumbers.services.SumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sums")
public class SumResource {

    @Autowired
    SumService sumService;

    @GetMapping("")
    public ResponseEntity<List<Sum>> getAllSums(HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");
        List<Sum> sums = sumService.fetchAllSums(userId);
        return new ResponseEntity<>(sums, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Sum> addSum(HttpServletRequest request,
                                                @RequestBody Map<String, Object> sumMap) {
        int userId = (Integer) request.getAttribute("userId");
        Integer sum1 = (Integer) sumMap.get("sum1");
        Integer sum2 = (Integer) sumMap.get("sum2");
        Sum sum = sumService.addSum(userId, sum1, sum2);
        return new ResponseEntity<>(sum, HttpStatus.CREATED);
    }

}

package com.devopchallenge.sumtwonumbers.domain;

public class Sum {

    private Integer sumId;
    private Integer userId;
    private Integer sum1;
    private Integer sum2;

    public Sum(Integer sumId, Integer userId, Integer sum1, Integer sum2) {
        this.sumId = sumId;
        this.userId = userId;
        this.sum1 = sum1;
        this.sum2 = sum2;
    }

    public Integer getSum(){
        return Integer.sum(sum1, sum2);
    }

    public Integer getSumId() {
        return sumId;
    }

    public void setSumId(Integer sumId) {
        this.sumId = sumId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSum1() {
        return sum1;
    }

    public void setSum1(Integer sum1) {
        this.sum1 = sum1;
    }
    public Integer getSum2() {
        return sum2;
    }

    public void setSum2(Integer sum2) {
        this.sum2 = sum2;
    }
   
}

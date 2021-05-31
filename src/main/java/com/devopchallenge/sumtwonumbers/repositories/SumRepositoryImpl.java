package com.devopchallenge.sumtwonumbers.repositories;

import com.devopchallenge.sumtwonumbers.domain.Sum;
import com.devopchallenge.sumtwonumbers.exceptions.s2nBadRequestException;
import com.devopchallenge.sumtwonumbers.exceptions.s2nResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SumRepositoryImpl implements SumRepository {

    private static final String SQL_FIND_ALL = "SELECT SUM_ID, USER_ID, SUM1, SUM2, RESULT " +
    "FROM S2N_SUMS WHERE USER_ID = ? GROUP BY SUM_ID";
    private static final String SQL_FIND_BY_ID = "SELECT SUM_ID, USER_ID, SUM1, SUM2, RESULT " +
    "FROM S2N_SUMS WHERE USER_ID = ? AND SUM_ID = ? GROUP BY SUM_ID";
    private static final String SQL_CREATE = "INSERT INTO S2N_SUMS (SUM_ID, USER_ID, SUM1, SUM2, RESULT) VALUES(NEXTVAL('S2N_SUMS_SEQ'), ?, ?, ?, ?)";
 
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Sum> findAll(Integer userId) throws s2nResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId}, sumRowMapper);
    }

    @Override
    public Sum findById(Integer userId, Integer sumId) throws s2nResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, sumId}, sumRowMapper);
        }catch (Exception e) {
            throw new s2nResourceNotFoundException("Sum not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer sum1, Integer sum2) throws s2nBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, sum1);
                ps.setInt(3, sum2);
                ps.setInt(4, Integer.sum(sum1, sum2));
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("SUM_ID");
        }catch (Exception e) {
            throw new s2nBadRequestException("Invalid request");
        }
    }

    private RowMapper<Sum> sumRowMapper = ((rs, rowNum) -> {
        return new Sum(rs.getInt("SUM_ID"),
                rs.getInt("USER_ID"),
                rs.getInt("SUM1"),
                rs.getInt("SUM2"));
    });
}

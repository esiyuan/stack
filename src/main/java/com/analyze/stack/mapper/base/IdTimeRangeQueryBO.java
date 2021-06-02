package com.analyze.stack.mapper.base;

import lombok.Data;

import java.util.Date;

/**
 * 根据id区间或者时间区间查询
 *
 * @author qusan
 * @create 2019-07-15-21:03
 */
@Data
public class IdTimeRangeQueryBO {

    private Long startId;
    private Long endId;

    private Date startTime;
    private Date endTime;


}

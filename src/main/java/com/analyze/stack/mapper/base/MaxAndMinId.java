package com.analyze.stack.mapper.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qusan
 * @create 2019-07-31-14:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MaxAndMinId {

    public static final MaxAndMinId EMPTY = new MaxAndMinId();

    private Long minId;
    private Long maxId;


    public int pages(int step) {
        return (int) Math.ceil((maxId - minId / 1.0) / step);
    }

    public MaxAndMinId[] splitByStep(int step) {
        int pages = (int) Math.ceil((maxId - minId / 1.0) / step);
        log.info("splitByStep pages = {}", pages);
        long start = minId;
        MaxAndMinId[] idRanges = new MaxAndMinId[pages];
        for (int i = 0; i < pages; i++) {
            long end = (start + step);
            idRanges[i] = new MaxAndMinId(start, end);
            start = end;
        }
        return idRanges;
    }

    public IdTimeRangeQueryBO convertTo() {
        IdTimeRangeQueryBO idTimeRangeQueryBo = new IdTimeRangeQueryBO();
        idTimeRangeQueryBo.setStartId(minId);
        idTimeRangeQueryBo.setEndId(maxId);
        return idTimeRangeQueryBo;
    }
}

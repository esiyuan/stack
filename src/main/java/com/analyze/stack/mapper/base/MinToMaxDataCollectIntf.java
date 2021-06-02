package com.analyze.stack.mapper.base;

import java.util.List;

/**
 * 从小到大的数据收集
 *
 * @author qusan
 * @create 2019-09-12-10:50
 */
public interface MinToMaxDataCollectIntf<T> {

    MaxAndMinId selectMaxAndMinId(MaxAndMinId record);

    List<T> selectBetween(IdTimeRangeQueryBO param);

}

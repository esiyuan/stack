package com.analyze.stack.process;

import com.analyze.stack.mapper.StackMapper;
import com.analyze.stack.mapper.base.IdTimeRangeQueryBO;
import com.analyze.stack.mapper.base.MaxAndMinId;
import com.analyze.stack.mapper.base.MinToMaxDataCollectIntf;
import com.analyze.stack.pojo.model.StackDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author guanjie@mgtv.com
 * @create 2021-06-02-16:48
 */
@Component
public class StackCollector implements MinToMaxDataCollectIntf<StackDO> {
    @Resource
    private StackMapper stackMapper;

    @Override
    public MaxAndMinId selectMaxAndMinId(MaxAndMinId record) {
        return stackMapper.selectMaxAndMinId(record);
    }

    @Override
    public List<StackDO> selectBetween(IdTimeRangeQueryBO param) {
        return stackMapper.selectBetween(param);
    }
}

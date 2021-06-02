package com.analyze.stack.mapper;

import com.analyze.stack.mapper.base.IdTimeRangeQueryBO;
import com.analyze.stack.mapper.base.MaxAndMinId;
import com.analyze.stack.pojo.model.StackDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-06-02
 */
public interface StackMapper  extends BaseMapper<StackDO>  {

    MaxAndMinId selectMaxAndMinId(MaxAndMinId maxAndMinId);

    List<StackDO> selectBetween(IdTimeRangeQueryBO param);
}

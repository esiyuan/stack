package com.analyze.stack.pojo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("m_stack")
@EqualsAndHashCode(callSuper = false)
public class StackDO {

    private Long id;

    private String name;

    private String code;


}

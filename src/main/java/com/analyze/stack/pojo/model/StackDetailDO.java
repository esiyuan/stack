package com.analyze.stack.pojo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jobob
 * @since 2021-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_stack_detail")
public class StackDetailDO implements Serializable {

    private Long id;
    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private Integer curPrice;

    private Integer rise;

    private Integer curTotal;
    private Integer totalPrice;

    private String curDate;


}

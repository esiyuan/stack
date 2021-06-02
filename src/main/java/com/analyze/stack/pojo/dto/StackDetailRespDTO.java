package com.analyze.stack.pojo.dto;

import lombok.Data;

/**
 * @author guanjie@mgtv.com
 * @create 2021-06-02-17:12
 */
@Data
public class StackDetailRespDTO {
    /**
     * 市值 百万
     */
    private Integer totalPrice;
    private String code;

    private String name;

    private Integer curPrice;

    private Integer rise;
    /**
     * 成交量 万
     */
    private Integer curTotal;


}

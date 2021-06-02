package com.analyze.stack.util.util.validate;

import lombok.Data;

/**
 * 校验结果
 *
 * @author qusan
 * @create 2019-08-21-20:19
 */
@Data
public class ValidResult {

    /**
     * 是否有错误
     */
    private boolean hasErrors;
    /**
     * 错误信息
     */
    private String errorMsg;


}

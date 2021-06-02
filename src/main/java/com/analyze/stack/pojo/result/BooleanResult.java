package com.analyze.stack.pojo.result;

import lombok.Data;

/**
 * @author guanjie@mgtv.com
 * @create 2020-12-23-17:00
 */
@Data
public class BooleanResult<T> {
    private boolean succeed;
    private T data;
    private String msg;

    public BooleanResult() {
    }

    public BooleanResult(boolean succeed) {
        this.succeed = succeed;
    }

    public final static BooleanResult SUCCESS = new BooleanResult(true);
    public final static BooleanResult FAIL = new BooleanResult(false);

    public static <T> BooleanResult<T> success(T data) {
        BooleanResult booleanResult = new BooleanResult(true);
        booleanResult.setData(data);
        return booleanResult;
    }

    public static BooleanResult fail(String msg) {
        BooleanResult booleanResult = new BooleanResult(false);
        booleanResult.setMsg(msg);
        return booleanResult;
    }

    public boolean failed() {
        return !succeed;
    }

}

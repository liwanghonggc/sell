package com.lwh.sell.vo;

import lombok.Data;

/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
@Data
public class ResultVO<T> {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    private T data;

    public ResultVO() {
    }

    private ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultVO ofSuccess() {
        return ofSuccess(null);
    }

    public static ResultVO ofSuccess(Object object) {
        return new ResultVO(0, "成功", object);
    }

    public static ResultVO ofError(Integer code, String msg){
        return new ResultVO(code, msg, null);
    }
}

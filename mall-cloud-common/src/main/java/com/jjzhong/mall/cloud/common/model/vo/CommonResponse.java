package com.jjzhong.mall.cloud.common.model.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jjzhong.mall.cloud.common.exception.MallException;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应类
 * @param <T> 返回的 data 的数据类型
 */
@Data
public class CommonResponse<T> implements Serializable {
    private Integer status;
    private String msg;
    private T data;
    public static final int OK_CODE = 10000;
    public static final String OK_MSG = "SUCCESS";
    public static final int ERROR_CODE = -1;
    public static final String ERROR_MSG = "ERROR";

    public CommonResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonResponse() {
        this(OK_CODE, OK_MSG);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(OK_CODE, OK_MSG);
    }

    public static <T> CommonResponse<T> success(T result) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setData(result);
        return response;
    }
    public static <T> CommonResponse<T> error() {
        return new CommonResponse<>(ERROR_CODE, ERROR_MSG);
    }

    public static <T> CommonResponse<T> error(Integer code, String msg) {
        return new CommonResponse<>(code, msg);
    }

    public static <T> CommonResponse<T> error(MallException ex) {
        return new CommonResponse<>(ex.getCode(), ex.getMessage());
    }

    /** 用于判断请求是否失败 */
    @JsonIgnore
    public boolean isError() {
        return this.getStatus().equals(ERROR_CODE);
    }

    /** 用于判断请求是否成功 */
    @JsonIgnore
    public boolean isSuccess() {
        return this.getStatus().equals(OK_CODE);
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

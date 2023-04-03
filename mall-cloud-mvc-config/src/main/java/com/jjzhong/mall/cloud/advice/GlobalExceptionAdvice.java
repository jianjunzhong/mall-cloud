package com.jjzhong.mall.cloud.advice;

import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.exception.MallException;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.Set;

/**
 * 统一异常处理类
 */
@Slf4j
@RestControllerAdvice(value = "com.jjzhong.mall.cloud")
public class GlobalExceptionAdvice {
    /**
     * 自定义异常的处理方法
     * @param e 自定义异常
     * @return 统一响应
     */
    @ExceptionHandler(MallException.class)
    public CommonResponse<Object> handleMallException(MallException e) {
        log.info("MallException: [{}]", e.getMessage(), e);
        return CommonResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(BindException.class)
    public CommonResponse<Object> handleBindException(BindException e) {
        log.info("BindException: [{}]", e.getMessage(), e);
        return CommonResponse.error(
                MallCommonExceptionEnum.REQUEST_PARAM_ERROR.getCode(),
                handleBindingResult(e.getBindingResult())
        );
    }

    /**
     * 缺少传递的参数异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info("MissingServletRequestParameterException: [{}]", e.getMessage(), e);
        return CommonResponse.error(new MallCommonException(MallCommonExceptionEnum.REQUEST_PARAM_ERROR));
    }

    /**
     * 传递的参数类型与方法声明的参数不匹配异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResponse<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("MethodArgumentTypeMismatchException: [{}]", e.getMessage(), e);
        return CommonResponse.error(new MallCommonException(MallCommonExceptionEnum.REQUEST_PARAM_ERROR));
    }

    /**
     * 参数读取异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("HttpMessageNotReadableException: [{}]", e.getMessage(), e);
        return CommonResponse.error(new MallCommonException(MallCommonExceptionEnum.REQUEST_PARAM_ERROR));
    }

    /**
     * 参数未通过校验异常的处理方法，会返回相应字段的错误提示，例如：
     * {
     *   "status": 10006,
     *   "msg": "参数错误: 邮箱格式错误",
     *   "data": null
     * }
     * @param e 异常
     * @return 清晰的统一相应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResponse<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.info("ConstraintViolationException: [{}]", e.getMessage(), e);
        return CommonResponse.error(
                MallCommonExceptionEnum.REQUEST_PARAM_ERROR.getCode(),
                handleConstraintViolation(e.getConstraintViolations())
        );
    }

    /**
     * 当 Sentinel 熔断降级的方法中抛出异常时，使用的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(NestedServletException.class)
    public CommonResponse<Object> handleNestedServletException(NestedServletException e) {
        log.info("NestedServletException: [{}]", e.getMessage(), e);
        if (e.getCause().getCause() instanceof MallException)
            return CommonResponse.error((MallException) e.getCause().getCause());
        else
            return CommonResponse.error(new MallCommonException(MallCommonExceptionEnum.UNKNOWN_ERROR));
    }

    /**
     * 其他异常
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<Object> handleException(Exception e) {
        log.info("Exception: [{}]", e.getMessage(), e);
        return CommonResponse.error(new MallCommonException(MallCommonExceptionEnum.SYSTEM_ERROR));
    }

    /**
     * 将参数错误异常包装成友好的提示消息
     * @param result BindException 中绑定的错误结果
     * @return 清晰友好的提示字符串，例如："参数错误: 邮箱格式错误, 验证码不能为空"
     */
    private String handleBindingResult(BindingResult result) {
        StringBuilder str = new StringBuilder(MallCommonExceptionEnum.REQUEST_PARAM_ERROR.getMessage()).append(": ");
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (int i = 0; i < allErrors.size(); i ++) {
                 str.append(allErrors.get(i).getDefaultMessage()).append(i == allErrors.size() - 1 ? "" : ", ");
            }
        }
        return str.toString();
    }

    /**
     * 将参数错误异常包装成友好的提示消息
     * @param constraintViolations ConstraintViolationException 中的错误校验结果
     * @return 清晰友好的提示字符串，例如："参数错误: 邮箱格式错误, 验证码不能为空"
     */
    private String handleConstraintViolation(Set<ConstraintViolation<?>> constraintViolations) {
        StringBuilder str = new StringBuilder(MallCommonExceptionEnum.REQUEST_PARAM_ERROR.getMessage()).append(": ");
        String res = null;
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                str.append(constraintViolation.getMessage()).append(", ");
            }
            res = str.substring(0, str.length() - 2);
        }
        return res;
    }
}

package com.demo.exception;

/**
 * 校验失败异常
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

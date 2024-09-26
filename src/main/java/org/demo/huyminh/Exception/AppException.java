package org.demo.huyminh.Exception;

import lombok.Data;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 9:18 AM
 */

@Data
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

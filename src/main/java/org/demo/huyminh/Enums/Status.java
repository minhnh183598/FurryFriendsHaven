package org.demo.huyminh.Enums;

import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;

/**
 * @author Minh
 * Date: 10/17/2024
 * Time: 00:51 AM
 */

public enum Status {
    NOT_STARTED,
    IN_PROGRESS,
    DONE,
    URGENT,
    ON_HOLD,
    OVERDUE,
    CANCELLED,
    ;

    public static Status fromString(String status) {
        for (Status taskStatus : Status.values()) {
            if (taskStatus.name().equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        throw new AppException(ErrorCode.INVALID_STATUS);
    }
}

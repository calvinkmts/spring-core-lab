package com.calvin.lab.bulkjob.domain;

public enum BulkJobStatus {
    CREATED,
    IN_PROGRESS,
    COMPLETED,
    COMPLETED_WITH_ERRORS,
    FAILED,
    CANCELLED
}

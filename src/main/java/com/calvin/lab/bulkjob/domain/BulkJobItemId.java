package com.calvin.lab.bulkjob.domain;

import lombok.Getter;

@Getter
public class BulkJobItemId {
    private final String value;

    public BulkJobItemId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BulkJobItemId cannot be null or blank");
        }
        this.value = value;
    }
}

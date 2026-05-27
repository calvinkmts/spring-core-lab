package com.calvin.lab.bulkjob.domain;

import lombok.Getter;

@Getter
public class BulkJobId {
    private String value;

    public BulkJobId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BulkJobId cannot be null or blank");
        }

        this.value = value;
    }

}

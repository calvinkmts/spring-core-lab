package com.calvin.lab.bulkjob.domain;

import lombok.Getter;

@Getter
public class BulkJobItem {
    private final BulkJobItemId id;
    private final int rowNumber;
    private BulkJobItemStatus status;
    private final String sourceKey;
    private String errorMessage;
    private final String payload;

    public BulkJobItem(BulkJobItemId id, int rowNumber, String sourceKey, String payload) {

        if (id == null) {
            throw new IllegalArgumentException("BulkJobItemId cannot be null");
        }

        if (rowNumber < 1) {
            throw new IllegalArgumentException("Row number must be greater than 0");
        }

        this.id = id;
        this.rowNumber = rowNumber;
        this.sourceKey = normalize(sourceKey);
        this.payload = normalize(payload);
        this.status = BulkJobItemStatus.PENDING;
        this.errorMessage = null;
    }

    public void markProcessing() {
        if (this.status != BulkJobItemStatus.PENDING) {
            throw new IllegalStateException("Only items with PENDING status can be marked as PROCESSING");
        }

        this.status = BulkJobItemStatus.PROCESSING;
    }

    public void markSuccess() {
        if (this.status != BulkJobItemStatus.PENDING && this.status != BulkJobItemStatus.PROCESSING) {
            throw new IllegalStateException("Only items with PENDING or PROCESSING status can be marked as SUCCESS");
        }

        this.status = BulkJobItemStatus.SUCCESS;
        this.errorMessage = null;
    }

    public void markFailed(String errorMessage) {
        if (this.status != BulkJobItemStatus.PENDING && this.status != BulkJobItemStatus.PROCESSING) {
            throw new IllegalStateException("Only items with PENDING or PROCESSING status can be marked as FAILED");
        }

        if (errorMessage == null || errorMessage.isBlank()) {
            throw new IllegalArgumentException("Error message cannot be null or blank when marking item as FAILED");
        }

        this.status = BulkJobItemStatus.FAILED;
        this.errorMessage = normalize(errorMessage);
    }

    public boolean isSuccess() {
        return this.status == BulkJobItemStatus.SUCCESS;
    }

    public boolean isFailed() {
        return this.status == BulkJobItemStatus.FAILED;
    }

    public boolean isPending() {
        return this.status == BulkJobItemStatus.PENDING;
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

}

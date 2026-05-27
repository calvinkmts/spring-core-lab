package com.calvin.lab.bulkjob.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class BulkJob {

    private final BulkJobId id;
    private final BulkJobType type;
    private BulkJobStatus status;

    private final String createdBy;
    private final Instant createdAt;

    private Instant startedAt;
    private Instant completedAt;

    private int totalItems;
    private int successItems;
    private int failedItems;

    private final List<BulkJobItem> items;

    public BulkJob(BulkJobId id, BulkJobType type, String createdBy) {

        if (id == null) {
            throw new IllegalArgumentException("BulkJobId cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("BulkJobType cannot be null");
        }

        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException("CreatedBy cannot be null or blank");
        }

        this.id = id;
        this.type = type;
        this.createdBy = createdBy.trim();

        this.status = BulkJobStatus.CREATED;

        this.createdAt = Instant.now();
        this.startedAt = null;
        this.completedAt = null;

        this.totalItems = 0;
        this.successItems = 0;
        this.failedItems = 0;

        this.items = new ArrayList<>();
    }

    public List<BulkJobItem> getItems() {
        return List.copyOf(items);
    }

    public void addItem(BulkJobItem item) {
        if (this.status != BulkJobStatus.CREATED) {
            throw new IllegalStateException("Items can only be added to jobs with CREATED status");
        }

        if (item == null) {
            throw new IllegalArgumentException("BulkJobItem cannot be null");
        }

        this.items.add(item);
        this.totalItems = this.items.size();
    }

    public void start() {
        if (this.status != BulkJobStatus.CREATED) {
            throw new IllegalStateException("Only jobs with CREATED status can be started");
        }

        if (this.items.isEmpty()) {
            throw new IllegalStateException("Cannot start a bulk job with no items");
        }

        this.status = BulkJobStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
    }

    public void markItemSuccess(BulkJobItemId itemId) {
        ensureInProgress();
        findItemById(itemId).markSuccess();
        recalculateSummary();
    }

    public void markItemFailed(BulkJobItemId itemId, String errorMessage) {
        ensureInProgress();
        findItemById(itemId).markFailed(errorMessage);
        recalculateSummary();
    }

    public void complete() {
        ensureInProgress();

        boolean hasUnresolvedItems = this.items.stream()
                .anyMatch(item -> item.isPending() || item.getStatus() == BulkJobItemStatus.PROCESSING);

        if (hasUnresolvedItems) {
            throw new IllegalStateException("Cannot complete bulk job with unresolved items");
        }

        this.status = this.failedItems > 0 ? BulkJobStatus.COMPLETED_WITH_ERRORS : BulkJobStatus.COMPLETED;

        this.completedAt = Instant.now();
    }

    private void recalculateSummary() {
        this.totalItems = this.items.size();
        this.successItems = (int) this.items.stream().filter(item -> item.getStatus() == BulkJobItemStatus.SUCCESS)
                .count();
        this.failedItems = (int) this.items.stream().filter(item -> item.getStatus() == BulkJobItemStatus.FAILED)
                .count();
    }

    private void ensureInProgress() {
        if (this.status != BulkJobStatus.IN_PROGRESS) {
            throw new IllegalStateException("Bulk job must be in IN_PROGRESS status to update item status");
        }
    }

    private BulkJobItem findItemById(BulkJobItemId itemId) {

        if (itemId == null) {
            throw new IllegalArgumentException("BulkJobItemId cannot be null");
        }

        return this.items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("BulkJobItem not found: %s", itemId.getValue())));
    }

}

package com.calvin.lab.bulkjob.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BulkJobTest {

    @Test
    void should_start_with_created_status() {
        // Arrange
        BulkJobId id = new BulkJobId("job-123");
        BulkJobType type = BulkJobType.UPLOAD;
        String createdBy = "user-1";

        // Act
        BulkJob bulkJob = new BulkJob(id, type, createdBy);

        // Assert
        assertThat(bulkJob.getStatus()).isEqualTo(BulkJobStatus.CREATED);
        assertThat(bulkJob.getTotalItems()).isEqualTo(0);
        assertThat(bulkJob.getSuccessItems()).isEqualTo(0);
        assertThat(bulkJob.getFailedItems()).isEqualTo(0);
    }

    @Test
    void should_add_item_when_status_is_created() {
        // Arrange
        BulkJobId id = new BulkJobId("job-123");
        BulkJobType type = BulkJobType.UPLOAD;
        String createdBy = "user-1";
        BulkJob bulkJob = new BulkJob(id, type, createdBy);

        BulkJobItemId itemId = new BulkJobItemId("item-1");
        int rowNumber = 1;
        String sourceKey = "key-1";
        String payload = "payload-1";
        BulkJobItem item = new BulkJobItem(itemId, rowNumber, sourceKey, payload);

        // Act
        bulkJob.addItem(item);

        // Assert
        assertThat(bulkJob.getStatus()).isEqualTo(BulkJobStatus.CREATED);
        assertThat(bulkJob.getItems()).hasSize(1);
        assertThat(bulkJob.getItems()).extracting(BulkJobItem::getId).contains(itemId);
    }

    @Test
    void should_not_start_job_without_items() {
        // Arrange
        BulkJobId id = new BulkJobId("job-123");
        BulkJobType type = BulkJobType.UPLOAD;
        String createdBy = "user-1";
        BulkJob bulkJob = new BulkJob(id, type, createdBy);

        // Act & Assert
        assertThatThrownBy(() -> bulkJob.start())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot start a bulk job with no items");

    }

    @Test
    void should_mark_item_failed_and_recalculate_summary() {
        // Arrange
        BulkJobId id = new BulkJobId("job-123");
        BulkJobType type = BulkJobType.UPLOAD;
        String createdBy = "user-1";
        BulkJob bulkJob = new BulkJob(id, type, createdBy);

        BulkJobItemId itemId = new BulkJobItemId("item-1");
        int rowNumber = 1;
        String sourceKey = "key-1";
        String payload = "payload-1";
        BulkJobItem item = new BulkJobItem(itemId, rowNumber, sourceKey, payload);

        // Act
        bulkJob.addItem(item);
        bulkJob.start();
        bulkJob.markItemFailed(itemId, "Error processing item");

        // Assert
        assertThat(bulkJob.getFailedItems()).isEqualTo(1);
    }

    @Test
    void should_complete_with_errors_when_failed_items_exist() {
        // Arrange
        BulkJobId id = new BulkJobId("job-123");
        BulkJobType type = BulkJobType.UPLOAD;
        String createdBy = "user-1";
        BulkJob bulkJob = new BulkJob(id, type, createdBy);

        BulkJobItemId itemId1 = new BulkJobItemId("item-1");
        int rowNumber1 = 1;
        String sourceKey1 = "key-1";
        String payload1 = "payload-1";
        BulkJobItem item1 = new BulkJobItem(itemId1, rowNumber1, sourceKey1, payload1);

        BulkJobItemId itemId2 = new BulkJobItemId("item-2");
        int rowNumber2 = 2;
        String sourceKey2 = "key-2";
        String payload2 = "payload-2";
        BulkJobItem item2 = new BulkJobItem(itemId2, rowNumber2, sourceKey2, payload2);

        // Act
        bulkJob.addItem(item1);
        bulkJob.addItem(item2);
        bulkJob.start();
        bulkJob.markItemFailed(itemId1, "Error processing item 1");
        bulkJob.markItemSuccess(itemId2);
        bulkJob.complete();

        // Assert
        assertThat(bulkJob.getStatus()).isEqualTo(BulkJobStatus.COMPLETED_WITH_ERRORS);
    }
}

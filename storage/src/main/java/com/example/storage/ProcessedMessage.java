package com.example.storage;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_messages")
public class ProcessedMessage {
    @Id
    private String messageId;

    private LocalDateTime processedAt;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return "ProcessedMessage{" +
                "messageId='" + messageId + '\'' +
                ", processedAt=" + processedAt +
                '}';
    }
}


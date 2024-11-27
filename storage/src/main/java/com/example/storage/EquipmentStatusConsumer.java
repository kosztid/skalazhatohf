    package com.example.storage;

    import io.github.resilience4j.retry.annotation.Retry;
    import org.springframework.amqp.rabbit.annotation.RabbitListener;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;

    @Service
public class EquipmentStatusConsumer {

    private final EquipmentService service;
    private final ProcessedMessageRepository messageRepository;

    public EquipmentStatusConsumer(EquipmentService service, ProcessedMessageRepository messageRepository) {
        this.service = service;
        this.messageRepository = messageRepository;
    }
    @Retry(name = "rabbitmqRetry", fallbackMethod = "handleRabbitMQError")
    @RabbitListener(queues = RabbitMQConfig.STATUS_UPDATE_QUEUE)
    public void handleStatusUpdate(StatusUpdateMessage request) {
        System.out.println("Received message from RabbitMQ: " + request);

        if (messageRepository.existsById(request.getMessageId())) {
            System.out.println("Üzenet már feldolgozva: " + request.getMessageId());
            return;
        }

        try {
            service.updateStatus(request.getId(), request.getStatus());
            System.out.println("Equipment status updated successfully for ID: " + request.getId());

            ProcessedMessage processedMessage = new ProcessedMessage();
            processedMessage.setMessageId(request.getMessageId());
            processedMessage.setProcessedAt(LocalDateTime.now());
            messageRepository.save(processedMessage);
        } catch (Exception e) {
            System.err.println("Error processing message from RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleRabbitMQError(StatusUpdateMessage request, Throwable exception) {
        System.err.println("Hiba a RabbitMQ feldolgozás során: " + request);
        System.err.println("Fallback aktiválva. Részletek: " + exception.getMessage());
    }
}



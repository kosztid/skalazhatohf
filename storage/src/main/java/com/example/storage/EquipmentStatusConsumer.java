    package com.example.storage;

    import org.springframework.amqp.rabbit.annotation.RabbitListener;
    import org.springframework.stereotype.Service;

    @Service
    public class EquipmentStatusConsumer {

        private final EquipmentService service;

        public EquipmentStatusConsumer(EquipmentService service) {
            this.service = service;
        }

        /**
         * RabbitMQ Listener to process messages from the queue.
         * @param request the deserialized message received from RabbitMQ
         */
        @RabbitListener(queues = RabbitMQConfig.STATUS_UPDATE_QUEUE)
        public void handleStatusUpdate(StatusUpdateMessage request) {
            System.out.println("Received message from RabbitMQ: " + request);

            try {
                service.updateStatus(request.getId(), request.getStatus());
                System.out.println("Equipment status updated successfully for ID: " + request.getId());
            } catch (Exception e) {
                System.err.println("Error processing message from RabbitMQ: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


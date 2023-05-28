package ru.task.publisher;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.task.dto.request.OrderRequest;

@Component
@EnableBinding(OrderPublisherBinding.class)
public class OrderMessagePublisher {
    private final OrderPublisherBinding orderPublisherBinding;

    public OrderMessagePublisher(OrderPublisherBinding orderPublisherBinding) {
        this.orderPublisherBinding = orderPublisherBinding;
    }

    public void publish(OrderRequest message) {
        Message<OrderRequest> kafkaMessage = MessageBuilder.withPayload(message).build();
        orderPublisherBinding.myOutput().send(kafkaMessage);
    }
}

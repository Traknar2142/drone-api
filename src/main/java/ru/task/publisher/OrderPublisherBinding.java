package ru.task.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrderPublisherBinding {
    String ORDER_PUBLISHER = "orderPublisher";

    @Output(ORDER_PUBLISHER)
    MessageChannel myOutput();
}

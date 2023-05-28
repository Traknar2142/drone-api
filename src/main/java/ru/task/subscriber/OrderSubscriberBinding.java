package ru.task.subscriber;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface OrderSubscriberBinding {
    String ORDER_SUBSCRIBER = "orderSubscriber";

    @Input(ORDER_SUBSCRIBER)
    MessageChannel myInput();
}

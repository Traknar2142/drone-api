package ru.task.subscriber;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ru.task.dto.request.OrderRequest;
import ru.task.service.OrderProcessor;

@Component
@EnableBinding(OrderSubscriberBinding.class)
public class OrderMessageSubscriber {
    private final OrderSubscriberBinding orderSubscriberBinding;
    private final OrderProcessor orderProcessor;

    public OrderMessageSubscriber(OrderSubscriberBinding orderSubscriberBinding, OrderProcessor orderProcessor) {
        this.orderSubscriberBinding = orderSubscriberBinding;
        this.orderProcessor = orderProcessor;
    }

    @StreamListener(target = OrderSubscriberBinding.ORDER_SUBSCRIBER)
    public void consume(OrderRequest orderRequest){
        orderProcessor.process(orderRequest);
    }
}

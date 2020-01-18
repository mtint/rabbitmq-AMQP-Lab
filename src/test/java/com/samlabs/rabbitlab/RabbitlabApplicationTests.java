package com.samlabs.rabbitlab;

import com.github.fridujo.rabbitmq.mock.MockChannel;
import com.github.fridujo.rabbitmq.mock.MockConnection;
import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RabbitlabApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void basic_consume_case() throws IOException, TimeoutException, InterruptedException {
        String exchangeName = "test-exchange";
        String routingKey = "test.key";

        try (Connection conn = new MockConnectionFactory().newConnection()) {
            assertThat(conn).isInstanceOf(MockConnection.class);

            try (Channel channel = conn.createChannel()) {
                assertThat(channel).isInstanceOf(MockChannel.class);

                channel.exchangeDeclare(exchangeName, "direct", true);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, exchangeName, routingKey);

                List<String> messages = new ArrayList<>();
                channel.basicConsume(queueName, false, "myConsumerTag",
                        new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope,
                                                       AMQP.BasicProperties properties,
                                                       byte[] body) throws IOException {
                                long deliveryTag = envelope.getDeliveryTag();
                                messages.add(new String(body));
                                // (process the message components here ...)
                                channel.basicAck(deliveryTag, false);
                            }
                        });

                byte[] messageBodyBytes = "Hello, world!".getBytes();
                channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);

                TimeUnit.MILLISECONDS.sleep(200L);

                assertThat(messages).containsExactly("Hello, world!");
            }
        }
    }

    @Test
    public void basic_consume_nack_case() throws IOException, TimeoutException, InterruptedException {
        String exchangeName = "test-exchange";
        String routingKey = "test.key";

        AtomicInteger atomicInteger = new AtomicInteger();
        final Semaphore waitForAtLeastOneDelivery = new Semaphore(0);
        final Semaphore waitForCancellation = new Semaphore(0);

        try (Connection conn = new MockConnectionFactory().newConnection()) {
            assertThat(conn).isInstanceOf(MockConnection.class);

            try (Channel channel = conn.createChannel()) {
                assertThat(channel).isInstanceOf(MockChannel.class);

                channel.exchangeDeclare(exchangeName, "direct", true);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, exchangeName, routingKey);

                channel.basicConsume(queueName, false, "myConsumerTag",
                        new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope,
                                                       AMQP.BasicProperties properties,
                                                       byte[] body) throws IOException {
                                waitForAtLeastOneDelivery.release();
                                long deliveryTag = envelope.getDeliveryTag();
                                atomicInteger.incrementAndGet();
                                channel.basicNack(deliveryTag, false, true);
                            }

                            @Override
                            public void handleCancel(String consumerTag) {
                                waitForCancellation.release();
                            }
                        });

                byte[] messageBodyBytes = "Hello, world!".getBytes();
                channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);
                waitForAtLeastOneDelivery.acquire();
            }
        }


    }
}

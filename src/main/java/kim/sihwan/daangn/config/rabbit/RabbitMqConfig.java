package kim.sihwan.daangn.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMqConfig {

    private static final String keywordQueueName = "keyword-queue";
    private static final String chatQueueName = "chat-queue";
    private static final String topicExchangeName = "push-exchange";
    private static final String directExchangeName = "chat-exchange";

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Bean
    Queue keywordQueue() {
        return new Queue(keywordQueueName, false);
    }

    @Bean
    Queue chatQueue() {
        return new Queue(chatQueueName, false);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }

    @Bean
    Binding keywordQueueBinding(Queue keywordQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(keywordQueue).to(topicExchange).with("push.keyword");
    }

    @Bean
    Binding chatQueueBinding(Queue chatQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(chatQueue).to(topicExchange).with("push.chat");
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory,
                                  MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(factory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory factory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {

        return new RabbitAdmin(factory);
    }
}
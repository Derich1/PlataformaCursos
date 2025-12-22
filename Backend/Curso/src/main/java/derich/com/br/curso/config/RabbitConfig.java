package derich.com.br.curso.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    public static final String EMAIL_EXCHANGE = "exchange.email";
    public static final String EMAIL_QUEUE = "fila.email";
    public static final String EMAIL_ROUTING = "routing.email";

    public static final String VALIDACAO_CURSO_EXCHANGE = "usuario.validacao.exchange";
    public static final String VALIDACAO_CURSO_QUEUE = "usuario.validacao.queue";
    public static final String VALIDACAO_CURSO_ROUTING_KEY = "usuario.validacao.routing";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public Exchange emailExchange() {
        return ExchangeBuilder.directExchange(EMAIL_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public Binding bindingEmail(Queue emailQueue, Exchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(EMAIL_ROUTING)
                .noargs();
    }

    @Bean
    public Exchange validacaoExchange() {
        return ExchangeBuilder.directExchange(VALIDACAO_CURSO_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue validacaoQueue() {
        return QueueBuilder.durable(VALIDACAO_CURSO_QUEUE).build();
    }

    @Bean
    public Binding bindingValidacao(Queue validacaoQueue, Exchange validacaoExchange) {
        return BindingBuilder
                .bind(validacaoQueue)
                .to(validacaoExchange)
                .with(VALIDACAO_CURSO_ROUTING_KEY)
                .noargs();
    }
}


package derich.com.br.Usuario.config;

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

    public static final String VALIDACAO_CURSO_EXCHANGE = "curso.validacao.exchange";
    public static final String VALIDACAO_CURSO_QUEUE = "curso.validacao.queue";
    public static final String VALIDACAO_CURSO_ROUTING_KEY = "curso.validacao.routing";

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
    public Exchange cursoValidacaoExchange() {
        return ExchangeBuilder.directExchange(VALIDACAO_CURSO_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue cursoValidacaoQueue() {
        return QueueBuilder.durable(VALIDACAO_CURSO_QUEUE).build();
    }

    @Bean
    public Binding bindingCursoValidacao(Queue cursoValidacaoQueue, Exchange cursoValidacaoExchange) {
        return BindingBuilder
                .bind(cursoValidacaoQueue)
                .to(cursoValidacaoExchange)
                .with(VALIDACAO_CURSO_ROUTING_KEY)
                .noargs();
    }
}

package com.algaworks.algasensors.temperature.processing.infrastructure.rabbitmq;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQInitializer {

  private final RabbitAdmin rabbitAdmin;

  public RabbitMQInitializer(RabbitAdmin rabbitAdmin) {
    this.rabbitAdmin = rabbitAdmin;
  }

  @PostConstruct
  public void initialize() {
    rabbitAdmin.initialize();
  }
}

package com.algaworks.algasensors.temperature.processing.api.controller;

import static com.algaworks.algasensors.temperature.processing.infrastructure.rabbitmq.RabbitMQConfig.FANOUT_EXCHANGE_NAME;

import com.algaworks.algasensors.temperature.processing.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.processing.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
public class TemperatureProcessingController {

  private static final Logger log = LoggerFactory.getLogger(TemperatureProcessingController.class);

  private final RabbitTemplate rabbitTemplate;

  public TemperatureProcessingController(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
  public void data(@PathVariable TSID sensorId, @RequestBody String input) {
    if (input == null || input.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input cannot be null or empty");
    }

    Double temperature;

    try {
      temperature = Double.parseDouble(input);
    } catch (NumberFormatException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid temperature value");
    }

    var temperatureLogOutput = TemperatureLogOutput.builder()
        .id(IdGenerator.generateTimeBasedUUID())
        .sensorId(sensorId)
        .value(temperature)
        .registeredAt(OffsetDateTime.now())
        .build();

    log.info("temperatureLogOutput: {}", temperatureLogOutput);

    rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", temperatureLogOutput, message -> {
      message.getMessageProperties().setHeader("sensorId", sensorId.toString());
      return message;
    });
  }
}

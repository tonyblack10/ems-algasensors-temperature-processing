package com.algaworks.algasensors.temperature.processing.api.config.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.hypersistence.tsid.TSID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TSIDJacksonConfig {

  @Bean
  public Module tsidModule() {
    var module = new SimpleModule();
    module.addSerializer(TSID.class, new TSIDToStringSerializer());

    return module;
  }

}

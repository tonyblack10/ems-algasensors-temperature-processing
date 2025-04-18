package com.algaworks.algasensors.temperature.processing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class UUIDv7Test {

    @Test
    void shouldGenerateUUIDv7() {
        var uuid = IdGenerator.generateTimeBasedUUID();

        var uuidDateTime = UUIDv7Utils.extractOffsetDateTime(uuid).truncatedTo(ChronoUnit.MINUTES);
        var currentDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Assertions.assertThat(uuidDateTime).isEqualTo(currentDateTime);
    }

}

package com.poppulo.lottery.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZoneDateDeSerializer extends JsonDeserializer<ZonedDateTime> {

    @Autowired
    private ZoneId zoneId;

    private DateTimeFormatter DTF;

    public ZoneDateDeSerializer() {
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }

        DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]'Z'").withZone(zoneId);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return ZonedDateTime.parse(jp.getText(), DTF);
    }


}

package com.poppulo.lottery.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZoneDateSerializer extends JsonSerializer<ZonedDateTime> {

    @Autowired
    private ZoneId zoneId;

    private DateTimeFormatter DTF;

    public ZoneDateSerializer() {
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
        DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]'Z'").withZone(zoneId);
    }

    @Override
    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //add your custom date parser
        String formattedString = zonedDateTime.format(DTF);
        gen.writeString(formattedString);
    }

    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }
}


package com.poppulo.lottery.config;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Autowired
    private ZoneId zoneId;

    public ZonedateTimeConverter() {
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
    }

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zoneDateTime) {
        return zoneDateTime == null ? null : Timestamp.from(zoneDateTime.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return sqlTimestamp == null ? null : sqlTimestamp.toInstant().atZone(zoneId);
    }
}

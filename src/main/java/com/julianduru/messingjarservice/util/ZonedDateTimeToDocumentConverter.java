package com.julianduru.messingjarservice.util;

import com.julianduru.util.TimeUtil;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import java.sql.Date;
import java.time.ZonedDateTime;

/**
 * created by julian on 14/04/2022
 */
@Component
@WritingConverter
public class ZonedDateTimeToDocumentConverter implements Converter<ZonedDateTime, Document> {
    static final String DATE_TIME = "dateTime";
    static final String ZONE = "zone";

    @Override
    public Document convert(@Nullable ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }

        Document document = new Document();
        document.put(DATE_TIME, Date.from(zonedDateTime.toInstant()));
        document.put(ZONE, zonedDateTime.getZone().getId());
        document.put("offset", zonedDateTime.getOffset().toString());

        return document;
    }
}


package com.zaloni.hack.appInsights.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom JSON Serializer class for Joda DateTime type. It converts Joda DateTime object
 * to a ISO DateTime String representation.
 */
public class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {

    private static DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZoneUTC();

    @Override
    public void serialize(DateTime value, JsonGenerator gen,
                          SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        gen.writeString(formatter.print(value));
    }
}

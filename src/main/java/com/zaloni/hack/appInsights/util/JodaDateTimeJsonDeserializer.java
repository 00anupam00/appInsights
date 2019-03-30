package com.zaloni.hack.appInsights.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JodaDateTimeJsonDeserializer extends JsonDeserializer<DateTime> {

    private static DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZoneUTC();

    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_STRING) {
            String dateTimeAsString = jsonParser.getText().trim();
            return formatter.parseDateTime(dateTimeAsString);
        }

        return null;
    }

}


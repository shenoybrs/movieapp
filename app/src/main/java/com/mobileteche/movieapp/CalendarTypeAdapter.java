package com.mobileteche.movieapp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarTypeAdapter implements JsonDeserializer<Calendar> {

    private static final String FORMAT_OUT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String FORMAT_OUT_B = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_SHORT = "M/dd/yyyy";

    private SimpleDateFormat sdf = new SimpleDateFormat();

    @Override
    public Calendar deserialize(JsonElement json, Type type, JsonDeserializationContext ctx)
            throws JsonParseException {
        Calendar cal = null;

        sdf.applyPattern(FORMAT_OUT);
        try {
            Date d = sdf.parse(json.getAsString());
            cal = Calendar.getInstance();
            cal.setTime(d);
            return cal;
        } catch (ParseException e) {
            // eat it
        }
        sdf.applyPattern(FORMAT_OUT_B);
        try {
            Date d = sdf.parse(json.getAsString());
            cal = Calendar.getInstance();
            cal.setTime(d);
            return cal;
        } catch (ParseException e) {
            // eat it
        }

        sdf.applyPattern(FORMAT_SHORT);
        try {
            Date d = sdf.parse(json.getAsString());
            cal = Calendar.getInstance();
            cal.setTime(d);
            return cal;
        } catch (ParseException e) {
            // eat it
        }

        return cal;
    }

}

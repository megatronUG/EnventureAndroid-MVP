package org.enventureenterprises.enventure.data;

import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import org.joda.time.DateTime;

/**
 * Created by mossplix on 7/6/17.
 */

public class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    @Override
    public JsonElement serialize(final @NonNull DateTime src, final @NonNull Type srcType,
                                 final @NonNull JsonSerializationContext context) {
        return new JsonPrimitive(src.getMillis() / 1000);
    }

    @Override
    public DateTime deserialize(final @NonNull JsonElement json, final @NonNull Type type,
                                final @NonNull JsonDeserializationContext context) {
        return new DateTime(json.getAsInt() * 1000L);
    }
}

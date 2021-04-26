package com.hmily.designpattern.util;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @ClassName JSONUtils
 * @Description json 字符串 和 对象转换
 **/
public abstract class JSONUtils {

    /**
     * Represents the default the format of the date that defined in JSON
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static class GsonUTCDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private final DateFormat dateFormat;

        public GsonUTCDateAdapter(String timeZone) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN); // This is
            // the
            // format
            // I
            // need
            if (StringUtils.isNotEmpty(timeZone)) {
                // This is the key line which converts the date to UTC which
                // cannot be accessed with the default serializer
                dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            }
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(dateFormat.format(date));
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            try {
                return dateFormat.parse(jsonElement.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public static List<String> parseStringList(JsonElement je) {
        List<String> list = null;
        if (null == je)
            return list;

        JsonArray ar = je.getAsJsonArray();
        if (null == ar)
            return list;

        int size = ar.size();
        list = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            JsonElement e = ar.get(i);
            String strTmp = e.getAsString();
            list.add(strTmp);
        }

        return list;
    }

    public static List<Integer> parseIntList(JsonElement je) {
        List<Integer> list = null;
        if (null == je)
            return list;

        JsonArray ar = je.getAsJsonArray();
        if (null == ar)
            return list;

        int size = ar.size();
        list = new ArrayList<Integer>();
        for (int i = 0; i < size; ++i) {
            JsonElement e = ar.get(i);
            int iTmp = e.getAsInt();
            list.add(iTmp);
        }

        return list;
    }

    public static List<Long> parseLongList(JsonElement je) {
        List<Long> list = null;
        if (null == je)
            return list;

        JsonArray ar = je.getAsJsonArray();
        if (null == ar)
            return list;

        int size = ar.size();
        list = new ArrayList<Long>();
        for (int i = 0; i < size; ++i) {
            JsonElement e = ar.get(i);
            Long iTmp = e.getAsLong();
            list.add(iTmp);
        }

        return list;
    }

    /**
     * Parse an object to JSON element. Return {@code null} if {@code null}
     * object provided.
     *
     * @param object
     *            the object to parse, may be null
     * @return the translated JSON element
     */
    public static JsonElement toJsonElement(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            String str = (String) object;
            if (StringUtils.isNotEmpty(str)) {
                return new JsonParser().parse(str);
            } else {
                return null;
            }
        } else {
            String json = createGson().toJson(object);
            if (StringUtils.isNotEmpty(json)) {
                JsonElement parse = new JsonParser().parse(json);
                if (parse.isJsonNull()) { // JsonNull不符合业务要求
                    return null;
                } else {
                    return parse;
                }
            } else {
                return null;
            }
        }
    }

    public static JsonObject toJsonObject(Object object) {
        String json = null;
        if (object == null) {
            return new JsonObject();
        } else if (object instanceof String) {
            json = (String) object;
        } else if (object instanceof JsonElement) {
            json = object.toString();
        } else {
            json = createGson().toJson(object);
        }
        if (StringUtils.isNotEmpty(json)) {
            JsonElement parse = new JsonParser().parse(json);
            if (parse.isJsonObject()) {
                return (JsonObject) parse;
            } else {
                return new JsonObject();
            }
        } else {
            return new JsonObject();
        }
    }

    public static JsonObject toJsonObject(Gson gson, Object object) {
        String json = null;
        if (object == null) {
            return new JsonObject();
        } else if (object instanceof String) {
            json = (String) object;
        } else if (object instanceof JsonElement) {
            json = object.toString();
        } else {
            json = gson.toJson(object);
        }
        if (StringUtils.isNotEmpty(json)) {
            JsonElement parse = new JsonParser().parse(json);
            if (parse.isJsonObject()) {
                return (JsonObject) parse;
            } else {
                return new JsonObject();
            }
        } else {
            return new JsonObject();
        }
    }

    public static JsonArray toJsonArray(Object object) {
        String json = null;
        if (object == null) {
            return new JsonArray();
        } else if (object instanceof String) {
            json = (String) object;
        } else if (object instanceof JsonElement) {
            json = object.toString();
        } else {
            json = createGson().toJson(object);
        }
        if (StringUtils.isNotEmpty(json)) {
            JsonElement parse = new JsonParser().parse(json);
            if (parse.isJsonArray()) {
                return (JsonArray) parse;
            } else {
                return new JsonArray();
            }
        } else {
            return new JsonArray();
        }
    }

    public static JsonArray toJsonArray(Gson gson, Object object) {
        String json = null;
        if (object == null) {
            return new JsonArray();
        } else if (object instanceof String) {
            json = (String) object;
        } else if (object instanceof JsonElement) {
            json = object.toString();
        } else {
            json = gson.toJson(object);
        }

        if (StringUtils.isNotEmpty(json)) {
            JsonElement parse = new JsonParser().parse(json);
            if (parse.isJsonArray()) {
                return (JsonArray) parse;
            } else {
                return new JsonArray();
            }
        } else {
            return new JsonArray();
        }
    }

    /**
     * Parse the String element to specified customize object with specified
     * date-format.
     *
     * @param object
     *            the source element to parse
     * @param clazz
     *            the customize object type
     * @return an customize object
     */
    public static <T> T fromJson(Object object, Class<T> clazz, String format) {
        Gson gson = createGson(format);
        return gson.fromJson((String) object, clazz);
    }

    public static <T> T fromJson(Gson gson, Object object, Class<T> clazz, String format) {
        return gson.fromJson((String) object, clazz);
    }

    /**
     * Parse the JSON/String/Reader element to specified customize object.
     *
     * @param object
     *            the source element to parse
     * @param clazz
     *            the customize object type
     * @return an customize object
     */
    @Deprecated
    public static <T> T fromJson(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            return gson.fromJson((String) object, clazz);
        } else if (object instanceof JsonElement) {
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            return gson.fromJson((JsonElement) object, clazz);
        } else if (object instanceof Reader) {
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            return gson.fromJson((Reader) object, clazz);
        } else {
            return null;
        }
    }

    public static <T> T fromJsonNew(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
            return gson.fromJson((String) object, clazz);
        } else if (object instanceof JsonElement) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
            return gson.fromJson((JsonElement) object, clazz);
        } else if (object instanceof Reader) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
            return gson.fromJson((Reader) object, clazz);
        } else {
            return null;
        }
    }

    /**
     * Parse the JSON/String/Reader element to specified customize object.
     *
     * @param object
     *            the source element to parse
     * @param type
     *            the customize object type
     */
    @Deprecated
    public static <T> T fromJson(Object object, Type type) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            return gson.fromJson((String) object, type);
        } else if (object instanceof Reader) {
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            return gson.fromJson((Reader) object, type);
        } else if (object instanceof JsonElement) {
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            return gson.fromJson(object.toString(), type);
        } else {
            return null;
        }
    }

    public static <T> T fromJsonNew(Object object, Type type) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
            return gson.fromJson((String) object, type);
        } else if (object instanceof Reader) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
            return gson.fromJson((Reader) object, type);
        } else if (object instanceof JsonElement) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
            return gson.fromJson(object.toString(), type);
        } else {
            return null;
        }
    }

    public static <T> T fromJsonNew(Gson gson, Object object, Type type) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return gson.fromJson((String) object, type);
        } else if (object instanceof Reader) {
            return gson.fromJson((Reader) object, type);
        } else if (object instanceof JsonElement) {
            return gson.fromJson(object.toString(), type);
        } else {
            return null;
        }
    }

    public static <T> T fromJsonNew(Object object, Type type, String format) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            Gson gson = new GsonBuilder().setDateFormat(format).create();
            return gson.fromJson((String) object, type);
        } else if (object instanceof Reader) {
            Gson gson = new GsonBuilder().setDateFormat(format).create();
            return gson.fromJson((Reader) object, type);
        } else if (object instanceof JsonElement) {
            Gson gson = new GsonBuilder().setDateFormat(format).create();
            return gson.fromJson(object.toString(), type);
        } else {
            return null;
        }
    }

    /**
     * Parse the JSON element to a list collection object.
     *
     * @param object
     *            the source element to parse
     * @param clazz
     *            the customize object type
     * @return the translated list collection
     */
    @Deprecated
    public static <T> List<T> fromJsonArray(Object object, Class<T> clazz) {
        return fromJson(object, buildParameterizedType(List.class, clazz));
    }

    public static <T> List<T> fromJsonArrayNew(Object object, Class<T> clazz) {
        return fromJsonNew(object, buildParameterizedType(List.class, clazz));
    }

    public static <T> List<T> fromJsonArrayNew(Gson gson, Object object, Class<T> clazz) {
        return fromJsonNew(gson, object, buildParameterizedType(List.class, clazz));
    }

    public static <T> List<T> fromJsonArrayNew(Object object, Class<T> clazz, String format) {
        return fromJsonNew(object, buildParameterizedType(List.class, clazz), format);
    }

    public static String toJsonString(Object obj) {
        Gson gson = createGson();
        String target = gson.toJson(obj);
        return target;
    }

    public static String toJsonString(Gson gson, Object obj) {
        String target = gson.toJson(obj);
        return target;
    }

    public static String toJsonString(Object obj, Type type) {
        Gson gson = createGson();
        String target = gson.toJson(obj, type);
        return target;
    }

    public static String toJsonString(Gson gson, Object obj, Type type) {
        String target = gson.toJson(obj, type);
        return target;
    }

    public static String toJsonString(Object obj, String format) {
        Gson gson = createGson(format);
        String json = gson.toJson(obj);
        return json;
    }

    /**
     * To construct a {@code Gson} object.
     *
     * @return the {@code Gson} object
     */
    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // set the default format of the date in JSON
        gsonBuilder.setDateFormat(DEFAULT_DATE_PATTERN);
        return gsonBuilder.create();
    }

    private static Gson createGson(String format) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // set the default format of the date in JSON
        gsonBuilder.setDateFormat(format);
        return gsonBuilder.create();
    }

    /**
     * Get the parameterized type of the generic parameter.
     *
     * @param raw
     *            the argument type
     * @param args
     *            the generic parameter.
     * @return the parameterized type reference
     */
    private static ParameterizedType buildParameterizedType(final Class<?> raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    @Deprecated
    public static <T> T parseFromJson(String json, Class<T> clazz) {
        JsonElement obj = new JsonParser().parse(json);
        return JSONUtils.fromJson(obj, clazz);
    }

    public static Integer checkAsInt(JsonObject object, String key) {
        Integer value = null;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive != null && StringUtils.isNotEmpty(primitive.getAsString())) {
                value = primitive.getAsInt();
            }
        }
        return value;
    }

    public static Long checkAsLong(JsonObject object, String key) {
        Long value = null;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive != null && StringUtils.isNotEmpty(primitive.getAsString())) {
                value = primitive.getAsLong();
            }
        }
        return value;
    }

    public static Double checkAsDouble(JsonObject object, String key) {
        Double value = null;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive != null && StringUtils.isNotEmpty(primitive.getAsString())) {
                value = primitive.getAsDouble();
            }
        }
        return value;
    }

    public static String checkAsString(JsonObject object, String key) {
        String value = null;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive != null) {
                value = primitive.getAsString();
            }
        }
        return value;
    }

    public static int checkAsInt(JsonObject object, String key, int defaultValue) {
        Integer value = checkAsInt(object, key);
        if (value == null) {
            return defaultValue;
        } else {
            return value.intValue();
        }
    }

    public static long checkAsLong(JsonObject object, String key, long defaultValue) {
        Long value = checkAsLong(object, key);
        if (value == null) {
            return defaultValue;
        } else {
            return value.longValue();
        }
    }

    public static double checkAsDouble(JsonObject object, String key, double defaultValue) {
        Double value = checkAsDouble(object, key);
        if (value == null) {
            return defaultValue;
        } else {
            return value.doubleValue();
        }
    }

    public static Boolean checkAsBoolean(JsonObject object, String key) {
        Boolean value = null;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive != null && StringUtils.isNotEmpty(primitive.getAsString())) {
                value = primitive.getAsBoolean();
            }
        }
        return value;
    }

    public static String checkAsString(JsonObject object, String key, String defaultValue) {
        String value = checkAsString(object, key);
        return value != null ? value : defaultValue;
    }

    public static Boolean checkAsBoolean(JsonObject object, String key, boolean defaultValue) {
        Boolean value = checkAsBoolean(object, key);
        return value != null ? value : defaultValue;
    }

    public static long checkAsLongWithThrowable(JsonObject object, String key) {
        long value = 0;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            throw new IllegalArgumentException(key + " is null");
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive == null) {
                throw new IllegalArgumentException(key + " is null");
            } else {
                value = primitive.getAsLong();
            }
        }
        return value;
    }

    public static String checkAsStringWithThrowable(JsonObject object, String key) {
        String value = null;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            throw new IllegalArgumentException(key + " is null");
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive == null) {
                throw new IllegalArgumentException(key + " is null");
            } else {
                value = primitive.getAsString();
            }
        }
        return value;
    }

    public static int checkAsIntWithThrowable(JsonObject object, String key) {
        int value = 0;
        JsonElement jsonElement = object.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            throw new IllegalArgumentException(key + " is null");
        } else {
            JsonPrimitive primitive = object.getAsJsonPrimitive(key);
            if (primitive == null) {
                throw new IllegalArgumentException(key + " is null");
            } else {
                value = primitive.getAsInt();
            }
        }
        return value;
    }

    public static int parseResult(String result) {
        int r = 0;
        if (StringUtils.isNotEmpty(result)) {
            JsonObject parse = (JsonObject) new JsonParser().parse(result);
            JsonElement temp = parse.get("result");
            if (temp.isJsonObject()) {
                r = temp.getAsJsonObject().getAsJsonPrimitive("status").getAsInt();
            }
        }
        return r;
    }

    @Deprecated
    public static String toJson(Object obj) {
        Gson gson = createGson();
        String json = gson.toJson(obj);
        return json;
    }

    @Deprecated
    public static String toJson(Object obj, String format) {
        Gson gson = createGson(format);
        String json = gson.toJson(obj);
        return json;
    }

    public static String toJsonWithTimeZone(Object obj, String timeZone) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter(timeZone)).create();
        String json = gson.toJson(obj);
        return json;
    }

    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }
}

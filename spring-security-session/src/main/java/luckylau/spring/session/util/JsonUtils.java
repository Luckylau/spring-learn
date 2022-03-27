package luckylau.spring.session.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * @Author luckylau
 * @Date 2022/3/27
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String objectToJson(Object data) {
        try {
            String result = MAPPER.writeValueAsString(data);
            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToBean(String jsonData, Class<T> beanType) {
        try {
            T result = MAPPER.readValue(jsonData, beanType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);

        try {
            List<T> resultList = MAPPER.readValue(jsonData, javaType);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <K, V> Map<K, V> jsonToMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);

        try {
            Map<K, V> resultMap = MAPPER.readValue(jsonData, javaType);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

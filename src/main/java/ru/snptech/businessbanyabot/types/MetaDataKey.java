package ru.snptech.businessbanyabot.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class MetaDataKey<T> {
    private final String serializationName;
    private final Class<T> type;

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public T getValue(Map<String, Object> params) {
        return (T) params.get(serializationName);
    }

    public <T> T getValue(Map<String, Object> params, Class<T> clazz) {
        return mapper.convertValue(params.get(serializationName), clazz);
    }

    @Override
    public String toString() {
        return serializationName;
    }

    public void setValue(Map<String, Object> params, T value) {
        params.put(serializationName, value);
    }

    public void remove(Map<String, Object> params) {
        params.remove(serializationName);
    }

    public boolean contains(Map<String, Object> params) {
        return params.containsKey(serializationName);
    }

}

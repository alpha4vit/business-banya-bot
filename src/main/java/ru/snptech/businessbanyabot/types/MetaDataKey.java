package ru.snptech.businessbanyabot.types;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class MetaDataKey<T> {
    private final String serializationName;
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public T getValue(Map<String, Object> params) {
        return (T) params.get(serializationName);
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

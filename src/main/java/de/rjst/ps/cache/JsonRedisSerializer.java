package de.rjst.ps.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@RequiredArgsConstructor
public class JsonRedisSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> tClass;

    @Override
    public byte[] serialize(final T t) {
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (final Exception ex) {
            throw new SerializationException("Error serializing object", ex);
        }
    }

    @Override
    public T deserialize(final byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, tClass);
        } catch (final Exception ex) {
            throw new SerializationException("Error deserializing object", ex);
        }
    }
}

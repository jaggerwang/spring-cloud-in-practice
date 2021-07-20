package net.jaggerwang.scip.post.adapter.dao.jpa.converter;

import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jagger Wang
 */
@Converter
public class PostImageIdsConverter implements AttributeConverter<List<Long>, String> {
    private ObjectMapper objectMapper;

    public PostImageIdsConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            var json = objectMapper.writeValueAsString(attribute);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return List.of();
        }

        try {
            var meta = objectMapper.readValue(dbData, Long[].class);
            return Arrays.asList(meta);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

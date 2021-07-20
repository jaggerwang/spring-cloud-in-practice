package net.jaggerwang.scip.file.adapter.dao.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.entity.FileBO;

/**
 * @author Jagger Wang
 */
@Converter
public class FileMetaConverter implements AttributeConverter<FileBO.Meta, String> {
    private ObjectMapper objectMapper;

    public FileMetaConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(FileBO.Meta attribute) {
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
    public FileBO.Meta convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, FileBO.Meta.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

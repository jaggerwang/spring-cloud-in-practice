package net.jaggerwang.scip.common.adapter.controller.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RootDto {
    private String code;
    private String message;
    private Map<String, Object> data;

    public RootDto(String code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RootDto(String code, String message) {
        this(code, message, Map.of());
    }

    public RootDto(Map<String, Object> data) {
        this("ok", "", data);
    }

    public RootDto() {
        this("ok", "", Map.of());
    }

    public RootDto addDataEntry(String key, Object value) {
        data.put(key, value);
        return this;
    }
}
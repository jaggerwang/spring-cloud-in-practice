package net.jaggerwang.scip.common.usecase.port.service.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRejectDto {
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}

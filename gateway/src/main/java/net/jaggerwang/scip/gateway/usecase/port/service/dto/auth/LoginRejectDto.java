package net.jaggerwang.scip.gateway.usecase.port.service.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRejectDto {
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}

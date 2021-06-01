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
public class LoginAcceptDTO {
    private String subject;

    @Builder.Default
    private Boolean remember = false;

    @JsonProperty("remember_for")
    @Builder.Default
    private Integer rememberFor = 3600;

    private String acr;
}

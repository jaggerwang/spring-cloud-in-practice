package net.jaggerwang.scip.common.usecase.port.service.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentAcceptDTO {
    @JsonProperty("grant_scope")
    @Builder.Default
    private List<String> grantScope = List.of();

    @JsonProperty("grant_access_token_audience")
    @Builder.Default
    private List<String> grantAccessTokenAudience = List.of();

    @Builder.Default
    private Boolean remember = false;

    @JsonProperty("remember_for")
    @Builder.Default
    private Integer rememberFor = 3600;

    @Builder.Default
    private Map<String, Object> session = Map.of();
}

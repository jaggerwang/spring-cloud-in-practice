package net.jaggerwang.scip.common.usecase.port.service.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestDto {
    @Builder.Default
    private Boolean skip = false;

    private String subject;

    private ClientDto client;

    @JsonProperty("request_url")
    private String requestUrl;

    @JsonProperty("requested_scope")
    @Builder.Default
    private List<String> requestedScope = List.of();

    @JsonProperty("oidc_context")
    @Builder.Default
    private Map<String, Object> oidcContext = Map.of();

    @Builder.Default
    private Map<String, Object> context = Map.of();
}

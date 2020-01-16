package net.jaggerwang.scip.gateway.usecase.port.service.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoutRequestDto {
    private String subject;

    private String sid;

    @JsonProperty("request_url")
    private String requestUrl;

    @JsonProperty("rp_initiated")
    @Builder.Default
    private Boolean rpInitiated = false;
}

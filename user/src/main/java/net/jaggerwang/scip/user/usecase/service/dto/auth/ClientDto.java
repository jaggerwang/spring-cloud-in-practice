package net.jaggerwang.scip.user.usecase.service.dto.auth;

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
public class ClientDto {
    @JsonProperty("client_id")
    private String id;

    @JsonProperty("client_name")
    private String name;

    @JsonProperty("logo_uri")
    private String logoUri;

    @JsonProperty("policy_uri")
    private String policyUri;

    @JsonProperty("tos_uri")
    private String tosUri;
}

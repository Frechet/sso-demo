package ru.intabia.sso.demo.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * User authorization dto.
 */
@Data
@ApiModel(description = "User authorization model")
public class User {

    @JsonProperty
    @ApiModelProperty
    private Long id;

    @JsonProperty
    @ApiModelProperty
    private String email;

    @JsonProperty
    @ApiModelProperty
    private String password;

    @JsonProperty
    @ApiModelProperty
    private String username;

    @JsonProperty
    @ApiModelProperty
    private String phone;
}

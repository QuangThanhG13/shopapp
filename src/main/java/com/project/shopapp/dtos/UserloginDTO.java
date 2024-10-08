package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserloginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number please enter")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}

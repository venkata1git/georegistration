package com.georegistration.domain;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern.List({
            @Pattern(regexp = ".*[a-z].*", message = "Password must contain a lowercase letter"),
            @Pattern(regexp = ".*[A-Z].*", message = "Password must contain an uppercase letter"),
            @Pattern(regexp = ".*[0-9].*", message = "Password must contain a number"),
            @Pattern(regexp = ".*[_#$%\\.].*", message = "Password must contain one of these special characters: _ # $ % .")
    })
    private String password;

    @NotBlank(message = "IP Address cannot be empty")
    private String ipAddress;
}

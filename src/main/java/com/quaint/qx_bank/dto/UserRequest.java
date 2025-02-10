package com.quaint.qx_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Schema(
            name = "First Name"
    )
    private String firstName;
    @Schema(
            name = "Last Name"
    )
    private String lastName;
    @Schema(
            name = "Other Name"
    )
    private String otherName;
    @Schema(
            name = "Gender"
    )
    private String gender;
    @Schema(
            name = "Address"
    )
    private String address;
    @Schema(
            name = "State Of Origin"
    )
    private String stateOfOrigin;
    @Schema(
            name = "Email Address"
    )
    private String email;
    @Schema(
            name = "Phone Number"
    )
    private String phoneNumber;
    @Schema(
            name = "Alternative Phone Number"
    )
    private String alternativePhoneNumber;
}

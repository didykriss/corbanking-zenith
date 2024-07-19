package com.chris.cba.chriscba.dto;

import com.chris.cba.chriscba.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @Schema(
            description = "User Account firstName",
            example = "Daniel"
    )
    private String firstName;
    @Schema(
            description = "User Account lastName",
            example = "Agada"
    )
    private String lastName;
    @Schema(
            description = "User Account otherName",
            example = "Tochukwu"
    )
    private String otherName;
    @Schema(
            description = "User Account Sex",
            example = "Male/Female"
    )
    private String gender;
    @Schema(
            description = "User Account Address",
            example = "54 Oterubi Ogidan st"
    )
    private String address;
    @Schema(
            description = "User Account State of Origin",
            example = "Lagos"
    )
    private String stateOfOrigin;

    @Schema(
            description = "User Account phoneNumber",
            example = "08012345678"
    )
    private String phoneNumber;
    @Schema(
            description = "User Account alternativePhoneNumber",
            example = "08012345678"
    )
    private String alternativePhoneNumber;
    @Schema(
            description = "User Account Status",
            example = "ACTIVE/INACTIVE"
    )
    private Status status;

}

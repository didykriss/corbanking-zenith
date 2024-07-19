package com.chris.cba.chriscba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnquiryRequest {
    @Schema(
            description = "User Account Number",
            example = "1234567890"
    )
    private String accountNumber;
}

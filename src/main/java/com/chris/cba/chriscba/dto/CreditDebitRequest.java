package com.chris.cba.chriscba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDebitRequest {
    @Schema(
            description = "User Account Number",
            example = "1234567890"
    )
    private String accountNumber;
    @Schema(
            description = "Amount to be credited/debited",
            example = "1000.00"
    )
    private BigDecimal amount;

}

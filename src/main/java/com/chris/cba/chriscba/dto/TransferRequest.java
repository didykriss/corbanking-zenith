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
public class TransferRequest {
    @Schema(
            description = "Source Account Number",
            example = "1234567890"
    )
    private String sourceAccountNumber;
    @Schema(
            description = "Destination Account Number",
            example = "1234567890"
    )
    private String destinationAccountNumber;
    @Schema(
            description = "Narration",
            example = "Transfer"
    )
    private String narration;
    @Schema(
            description = "Amount to be transferred",
            example = "1000.00"
    )
    private BigDecimal amount;
}

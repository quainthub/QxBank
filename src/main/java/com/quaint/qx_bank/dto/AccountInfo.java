package com.quaint.qx_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    @Schema(
            name = "Account Name"
    )
    private String accountName;
    @Schema(
            name = "Account Number"
    )
    private String accountNumber;
    @Schema(
            name = "Account Balance"
    )
    private BigDecimal accountBalance;
}

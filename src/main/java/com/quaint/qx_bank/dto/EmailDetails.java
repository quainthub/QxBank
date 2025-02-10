package com.quaint.qx_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    @Schema(
            name = "Recipient Email"
    )
    private String recipient;
    @Schema(
            name = "Message Body"
    )
    private String messageBody;
    @Schema(
            name = "Email Subject"
    )
    private String subject;
    @Schema(
            name = "Attachment"
    )
    private String attachment;
}

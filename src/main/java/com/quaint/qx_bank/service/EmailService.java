package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);

}

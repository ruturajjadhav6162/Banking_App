package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}

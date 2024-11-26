package com.banking.Banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data //it creates Getter,Setter,toString(),equals(),hashcode()
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    private String accountNumber;
    private BigDecimal accountBalance;
    private String accountName;
}

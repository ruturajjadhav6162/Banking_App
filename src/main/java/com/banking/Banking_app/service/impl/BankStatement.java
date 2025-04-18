package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.entity.Transaction;
import com.banking.Banking_app.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatement {
   @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(start)) // >= start
                .filter(transaction -> !transaction.getCreatedAt().isAfter(end))   // <= end
                .toList();
    }
}

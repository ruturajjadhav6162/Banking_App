package com.banking.Banking_app.controller;

import com.banking.Banking_app.entity.Transaction;
import com.banking.Banking_app.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {
    @Autowired
    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> getBankStatement(@RequestParam String accountNumber,
                                              @RequestParam String startDate,
                                              @RequestParam String endDate) {
        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }
}

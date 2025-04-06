package com.banking.Banking_app.service.impl;
import com.banking.Banking_app.dto.TransactionDto;
import com.banking.Banking_app.entity.Transaction;
import com.banking.Banking_app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    @Transactional
    public void saveTransaction(TransactionDto transactionDto){
     Transaction transaction = Transaction.builder()
             .transactionType(transactionDto.getTransactionType())
             .accountNumber(transactionDto.getAccountNumber())
             .amount(transactionDto.getAmount())
             .status("SUCCESS")
             .build();
     transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully");
    }
}

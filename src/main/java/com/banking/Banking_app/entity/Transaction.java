package com.banking.Banking_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String transactionID;
    private String transactionType;
    private String amount;
    private String accountNumber;
    private String status;

}

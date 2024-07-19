package com.chris.cba.chriscba.service.impl;

import com.chris.cba.chriscba.dto.TransactionDto;
import com.chris.cba.chriscba.entity.TransactionEntity;
import com.chris.cba.chriscba.enums.Status;
import com.chris.cba.chriscba.repository.TransactionRepository;
import com.chris.cba.chriscba.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        TransactionEntity transaction = TransactionEntity.builder()
                .transactionType(transactionDto.getTransactionType())
                .transactionAmount(transactionDto.getTransactionAmount())
                .transactionAccountNumber(transactionDto.getTransactionAccountNumber())
                .transactionStatus(Status.ACTIVE)
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }

}


package com.chris.cba.chriscba.service;

import com.chris.cba.chriscba.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    void saveTransaction(TransactionDto transaction);
}

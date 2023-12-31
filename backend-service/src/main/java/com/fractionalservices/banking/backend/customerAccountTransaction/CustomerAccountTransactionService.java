package com.fractionalservices.banking.backend.customerAccountTransaction;

import com.fractionalservices.banking.backend.authentication.InvalidCustomerException;
import com.fractionalservices.banking.backend.exception.BadRequestException;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface CustomerAccountTransactionService {
    List<CustomerTransactionResponse> getCustomerTransactions(CustomerTransactionRequest customerTransactionRequest, HttpHeaders headers) throws BadRequestException, InvalidCustomerException, com.fractionalservices.banking.backend.transaction.NoTransactionException;
}

package com.fractionalservices.banking.backend.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.url.account-transaction-service}")
    private String accountTransactionServiceUrl;

    @Override
    public List<TransactionResponse> getTransactions(TransactionRequest transactionRequest) {
        logger.info("Get transaction, transaction request : {}", transactionRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionRequest> requestEntity = new HttpEntity(transactionRequest, headers);

        ResponseEntity<List<TransactionResponse>> responseEntity =
                restTemplate.exchange(accountTransactionServiceUrl,
                        HttpMethod.POST, requestEntity,
                        new ParameterizedTypeReference<List<TransactionResponse>>() {
                        });
        List<TransactionResponse> transactionResponses = responseEntity.getBody();
        logger.info("Get transaction, transaction response : {}", transactionResponses);
        return transactionResponses;
    }
}

package com.fractionalservices.banking.backend.customerAccountTransaction;


import com.fractionalservices.banking.backend.authentication.AuthenticationValidationService;
import com.fractionalservices.banking.backend.authentication.CustomerDetails;
import com.fractionalservices.banking.backend.authentication.InvalidCustomerException;
import com.fractionalservices.banking.backend.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerAccountTransactionController {

    private static Logger logger = LoggerFactory.getLogger(CustomerAccountTransactionController.class);

    @Autowired
    private AuthenticationValidationService authenticationValidationService;

    @Autowired
    private CustomerAccountTransactionService customerAccountTransactionService;

    @PostMapping("/customer/transactions")
    public ResponseEntity<CustomerAccountTransactionResponse> getCustomerTransactions(
            @RequestBody CustomerTransactionRequest customerTransactionRequest,
            @RequestHeader HttpHeaders headers)
            throws BadRequestException, InvalidCustomerException, com.fractionalservices.banking.backend.transaction.NoTransactionException {
        logger.debug("Get Customer Transactions start");

        // Authentication Validation
        CustomerDetails customerDetails = authenticationValidationService.validateAndGetCustomer(headers, customerTransactionRequest.getCustomerId());

        logger.info("Customer is valid with id : {}", customerDetails.getCustomerId());
        // Validate Request Parameters
        validateCustomerTransactionRequest(customerTransactionRequest, customerDetails.getCustomerId());

        logger.info("Customer validation is successful, getting customer transactions - start");

        List<CustomerTransactionResponse> responses = customerAccountTransactionService.getCustomerTransactions(customerTransactionRequest, headers);
        logger.info("Getting customer transactions - ends");

//        if (CollectionUtils.isEmpty(responses)) {
//            logger.error("No transactions found error");
//            throw new NoTransactionException("No Transactions found");
//        }

        CustomerAccountTransactionResponse catr = this.getTransactionTotal(responses);
        ResponseEntity<CustomerAccountTransactionResponse> responseEntity = new ResponseEntity<CustomerAccountTransactionResponse>(catr, HttpStatus.OK);

        logger.debug("Get Customer Transactions ends");
        return responseEntity;
    }

    private CustomerAccountTransactionResponse getTransactionTotal(List<CustomerTransactionResponse> response) {
        logger.debug("Start doing totals - starts");
        CustomerAccountTransactionResponse catr = new CustomerAccountTransactionResponse();
        catr.setCustomerTransactionResponses(response);
        catr.setCreditTotal(0d);
        catr.setDebitTotal(0d);

        response.forEach(ctr -> {
            if (ctr.getTransactionType().equalsIgnoreCase("Debit")) {
                catr.setDebitTotal(catr.getDebitTotal() + ctr.getAmount());
            } else if (ctr.getTransactionType().equalsIgnoreCase("Credit")) {
                catr.setCreditTotal(catr.getCreditTotal() + ctr.getAmount());
            }
        });

        logger.debug("Start doing totals - ends");
        return catr;
    }

    private void validateCustomerTransactionRequest(CustomerTransactionRequest customerTransactionRequest, String customerId) throws BadRequestException, InvalidCustomerException {

        if (customerTransactionRequest.getCurrency() == null || customerTransactionRequest.getCurrency().trim().length() == 0) {
            logger.error("Currency is missing error");
            throw new BadRequestException("Currency is missing");
        }

        if (customerTransactionRequest.getYear() == null || customerTransactionRequest.getYear() < 0) {
            logger.error("Invalid Year error");
            throw new BadRequestException("Invalid Year");
        }

        if (customerTransactionRequest.getMonth() == null || customerTransactionRequest.getMonth() < 0) {
            logger.error("Invalid Month error");
            throw new BadRequestException("Invalid Month");
        }

    }
}

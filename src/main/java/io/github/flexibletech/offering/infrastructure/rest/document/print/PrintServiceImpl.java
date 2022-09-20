package io.github.flexibletech.offering.infrastructure.rest.document.print;

import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;
import io.github.flexibletech.offering.domain.loanapplication.document.PrintService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrintServiceImpl implements PrintService {
    private final PrintServiceClient printServiceClient;

    private static final String PRINT_SERVICE = "print-service";
    private static final String PDF_FORMAT = "PDF";
    private static final String LOAN_APPLICATION_VARIABLE_NAME = "loanApplication";
    private static final String CLIENT_VARIABLE_NAME = "client";

    public PrintServiceImpl(PrintServiceClient printServiceClient) {
        this.printServiceClient = printServiceClient;
    }

    @Override
    @Bulkhead(name = PRINT_SERVICE)
    @CircuitBreaker(name = PRINT_SERVICE)
    public byte[] print(LoanApplication loanApplication, Document.Type documentType, Client client) {
        return printServiceClient.print(
                new PrintDocumentRequest(
                        defineTemplate(documentType),
                        Map.of(LOAN_APPLICATION_VARIABLE_NAME, loanApplication, CLIENT_VARIABLE_NAME, client),
                        PDF_FORMAT));
    }

    private String defineTemplate(Document.Type documentType) {
        switch (documentType) {
            case FORM:
                return "form";
            case INSURANCE:
                return "insurance";
            case CONDITIONS:
                return "conditions";
            default:
                throw new IllegalArgumentException(String.format("Unknown document type %s", documentType.name()));
        }
    }

}

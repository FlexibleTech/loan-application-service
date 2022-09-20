package io.github.flexibletech.offering.domain.loanapplication.document;

import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;

public interface PrintService {

    byte[] print(LoanApplication loanApplication, Document.Type documentType, Client client);

}

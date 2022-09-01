package io.github.flexibletech.offering.domain.document;

import io.github.flexibletech.offering.domain.LoanApplication;

public interface PrintService {

    byte[] print(LoanApplication loanApplication, Document.Type documentType);

}

package io.github.flexibletech.offering.domain.loanapplication.document;

public interface DocumentStorage {

    String place(String documentName, byte[] content);

}

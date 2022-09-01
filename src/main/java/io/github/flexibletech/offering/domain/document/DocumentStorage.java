package io.github.flexibletech.offering.domain.document;

public interface DocumentStorage {

    String place(String documentName, byte[] content);

}

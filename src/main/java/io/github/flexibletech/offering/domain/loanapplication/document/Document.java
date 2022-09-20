package io.github.flexibletech.offering.domain.loanapplication.document;

import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document implements ValueObject {
    private String id;
    private Type type;
    private boolean signed;

    @Transient
    public static final String FORMAT = ".pdf";

    public Document sign() {
        return new Document(this.id, this.type, true);
    }

    public static Document newUnsignedDocument(String id, Document.Type documentType) {
        return new Document(id, documentType, false);
    }

    public enum Type {
        FORM,
        CONDITIONS,
        INSURANCE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Document document = (Document) o;
        return type == document.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

}

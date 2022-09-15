package io.github.flexibletech.offering.domain;

import io.github.flexibletech.offering.domain.common.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanApplicationId implements ValueObject, Serializable {
    @Transient
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    @Transient
    private static final String PREFIX = "LOANAPP";
    @Transient
    private static final String SUFFIX_FORMAT = "%05d";

    private String id;

    public static LoanApplicationId nextLoanApplicationId(Long sequenceValue) {
        var id = PREFIX + LocalDateTime.now().format(FORMATTER) + String.format(SUFFIX_FORMAT, sequenceValue);
        return new LoanApplicationId(id);
    }

    public static LoanApplicationId fromValue(String value) {
        return new LoanApplicationId(value);
    }

    @Override
    public String toString() {
        return this.id;
    }

}

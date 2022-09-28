package io.github.flexibletech.offering.domain.client;

import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Passport implements ValueObject {
    private String series;
    private String number;
    private LocalDate issueDate;
    private String department;
    private String departmentCode;

    Passport update(String series, String number,
                    LocalDate issueDate, String department,
                    String departmentCode) {
        return new Passport(
                Optional.ofNullable(series).orElse(this.series),
                Optional.ofNullable(number).orElse(this.number),
                Optional.ofNullable(issueDate).orElse(this.issueDate),
                Optional.ofNullable(department).orElse(this.department),
                Optional.ofNullable(departmentCode).orElse(this.departmentCode));
    }
}

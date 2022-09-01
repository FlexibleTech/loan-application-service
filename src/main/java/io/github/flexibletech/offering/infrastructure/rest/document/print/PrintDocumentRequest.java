package io.github.flexibletech.offering.infrastructure.rest.document.print;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrintDocumentRequest {
    private String template;
    private Map<String, Object> variables;
    private String format;
}

package io.github.flexibletech.offering.infrastructure.rest;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;
import io.github.flexibletech.offering.domain.loanapplication.document.PrintService;
import io.github.flexibletech.offering.infrastructure.rest.document.print.PrintDocumentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

@WireMockTest(httpPort = 8085)
public class PrintServiceIT extends AbstractIntegrationTest {
    @Autowired
    private PrintService printService;

    @Test
    public void shouldPrintDocument() throws IOException {
        var expectedContent = ResourceUtil.getByteArray("classpath:files/test.pdf");
        WireMock.stubFor(
                WireMock.post("/api/documents")
                        .withRequestBody(
                                WireMock.equalToJson(ResourceUtil.getString("classpath:json/printDocumentRequest.json"))
                        )
                        .willReturn(WireMock.aResponse().withBody(expectedContent))
        );

        System.out.println(objectMapper.writeValueAsString(new PrintDocumentRequest(
                "form",
                Map.of("loanApplication", TestLoanApplicationFactory.newLoanApplicationWithoutDocuments(), "client", TestClientFactory.newStandardMarriedClient()),
                ".pdf")));

        var actualContent = printService.print(TestLoanApplicationFactory.newLoanApplicationWithoutDocuments(),
                Document.Type.FORM,
                TestClientFactory.newStandardMarriedClient());

        Assertions.assertArrayEquals(expectedContent, actualContent);
    }

}

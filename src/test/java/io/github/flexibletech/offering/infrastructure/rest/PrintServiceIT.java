package io.github.flexibletech.offering.infrastructure.rest;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.domain.document.Document;
import io.github.flexibletech.offering.domain.document.PrintService;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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

        var actualContent = printService.print(TestLoanApplicationFactory.newLoanApplicationWithoutDocuments(), Document.Type.FORM);

        Assertions.assertArrayEquals(expectedContent, actualContent);
    }

}

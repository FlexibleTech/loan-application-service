package io.github.flexibletech.offering.infrastructure.rest;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferRepository;
import io.github.flexibletech.offering.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

@WireMockTest(httpPort = 8087)
public class PreApprovedOfferRepositoryIT extends AbstractIntegrationTest {
    @Autowired
    private PreApprovedOfferRepository preApprovedOfferRepository;

    @Test
    public void shouldFindPreApprovedOfferForClient() throws IOException {
        WireMock.stubFor(
                WireMock.get("/api/pre-approved-offers?client_id=20056671")
                        .willReturn(WireMock.aResponse().withBody(
                                        ResourceUtil.getString("classpath:json/getPreApprovedOfferResponse.json"))
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        var preApprovedOffer = preApprovedOfferRepository.findForClient(new ClientId(TestValues.CLIENT_ID));

        Assertions.assertNotNull(preApprovedOffer);
        Assertions.assertEquals(preApprovedOffer.getClientId(), TestValues.CLIENT_ID);
        Assertions.assertEquals(preApprovedOffer.getMaxAmount(), TestValues.PRE_APPROVED_OFFER_MAX_AMOUNT);
        Assertions.assertEquals(preApprovedOffer.getMinAmount(), TestValues.PRE_APPROVED_OFFER_MIN_AMOUNT);
        Assertions.assertEquals(preApprovedOffer.getId().toString(), TestValues.PRE_APPROVED_OFFER_ID);
    }
}

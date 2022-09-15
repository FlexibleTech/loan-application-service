package io.github.flexibletech.offering.infrastructure.rest.preapprovedoffer;

import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOffer;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreApprovedOfferRepositoryImpl implements PreApprovedOfferRepository {
    private final PreApprovedOfferServiceClient preApprovedOfferServiceClient;

    private static final String CACHE_NAME = "pre-approved-offers-cache";
    private static final String PRE_APPROVED_OFFER_SERVICE = "pre-approved-offers-service";

    @Autowired
    public PreApprovedOfferRepositoryImpl(PreApprovedOfferServiceClient preApprovedOfferServiceClient) {
        this.preApprovedOfferServiceClient = preApprovedOfferServiceClient;
    }

    @Override
    @Bulkhead(name = PRE_APPROVED_OFFER_SERVICE)
    @Cacheable(cacheNames = CACHE_NAME, unless = "#result == null")
    @CircuitBreaker(name = PRE_APPROVED_OFFER_SERVICE, fallbackMethod = "fallback")
    public PreApprovedOffer findForClient(ClientId clientId) {
        var preApprovedOfferResponse = preApprovedOfferServiceClient.getByClientId(clientId.toString());
        return PreApprovedOffer.newPreApprovedOffer(
                preApprovedOfferResponse.getId(),
                preApprovedOfferResponse.getMinAmount(),
                preApprovedOfferResponse.getMaxAmount(),
                preApprovedOfferResponse.getClientId());
    }

    private PreApprovedOffer fallback(Exception ex) {
        log.error(ex.getMessage(), ex);
        return null;
    }

}

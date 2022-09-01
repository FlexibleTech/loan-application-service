package io.github.flexibletech.offering.domain.preapproved;

public interface PreApprovedOfferRepository {

    PreApprovedOffer findForClient(String clientId);

}

package io.github.flexibletech.offering.domain.preapproved;

import io.github.flexibletech.offering.domain.client.ClientId;

public interface PreApprovedOfferRepository {

    PreApprovedOffer findForClient(ClientId clientId);

}

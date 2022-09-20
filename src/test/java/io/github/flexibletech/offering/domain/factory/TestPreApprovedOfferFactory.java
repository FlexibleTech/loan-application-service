package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOffer;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferId;

public class TestPreApprovedOfferFactory {
    private TestPreApprovedOfferFactory() {
    }

    public static PreApprovedOffer newPreApprovedOffer() {
        return new PreApprovedOffer(
                new PreApprovedOfferId(TestValues.PRE_APPROVED_OFFER_ID),
                TestValues.PRE_APPROVED_OFFER_MIN_AMOUNT,
                TestValues.PRE_APPROVED_OFFER_MAX_AMOUNT,
                TestValues.CLIENT_ID);
    }

    public static PreApprovedOffer newPreApprovedOfferWithSmallMaxOfferAmount() {
        return new PreApprovedOffer(
                new PreApprovedOfferId(TestValues.PRE_APPROVED_OFFER_ID),
                TestValues.PRE_APPROVED_OFFER_MIN_AMOUNT,
                TestValues.PRE_APPROVED_OFFER_SMALL_MAX_AMOUNT,
                TestValues.CLIENT_ID);
    }

}

package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOffer;

public class TestPreApprovedOfferFactory {
    private TestPreApprovedOfferFactory() {
    }

    public static PreApprovedOffer newPreApprovedOffer() {
        return new PreApprovedOffer(
                TestValues.PRE_APPROVED_OFFER_ID,
                TestValues.PRE_APPROVED_OFFER_MIN_AMOUNT,
                TestValues.PRE_APPROVED_OFFER_MAX_AMOUNT,
                TestValues.CLIENT_ID);
    }

    public static PreApprovedOffer newPreApprovedOfferWithSmallMaxOfferAmount() {
        return new PreApprovedOffer(
                TestValues.PRE_APPROVED_OFFER_ID,
                TestValues.PRE_APPROVED_OFFER_MIN_AMOUNT,
                TestValues.PRE_APPROVED_OFFER_SMALL_MAX_AMOUNT,
                TestValues.CLIENT_ID);
    }

}

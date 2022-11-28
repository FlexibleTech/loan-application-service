package io.github.flexibletech.offering.infrastructure.rest.preapprovedoffer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "preapprovedoffer-service", url = "${service.preapprovedoffer.url}")
public interface PreApprovedOfferServiceClient {

    @GetMapping(value = "/pre-approved-offers")
    PreApprovedOfferResponse getByClientId(@RequestParam("client_id") String clientId);

}

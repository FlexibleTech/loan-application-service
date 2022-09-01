package io.github.flexibletech.offering.infrastructure.rest.document.print;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "print-service", url = "${service.print.url}")
public interface PrintServiceClient {

    @PostMapping("/api/documents")
    byte[] print(@RequestBody PrintDocumentRequest request);

}

package io.github.flexibletech.offering.web;

import io.github.flexibletech.offering.application.dto.ConditionsDto;
import io.github.flexibletech.offering.application.dto.LoanApplicationDto;
import lombok.experimental.UtilityClass;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@UtilityClass
public class HypermediaUtil {

    void addLinks(LoanApplicationDto loanApplication) {
        addSelfLink(loanApplication);
        addChoseConditionsLink(loanApplication);
        addSignDocumentPackageLink(loanApplication);
    }

    private void addSelfLink(LoanApplicationDto loanApplication) {
        loanApplication.add(
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(LoanApplicationController.class)
                                        .findLoanApplicationById(loanApplication.getId()))
                        .withSelfRel());
    }

    private void addChoseConditionsLink(LoanApplicationDto loanApplication) {
        loanApplication.add(
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(LoanApplicationController.class)
                                        .choseConditionsForLoanApplication(loanApplication.getId(), new ConditionsDto()))
                        .withRel("chose-conditions"));
    }

    private void addSignDocumentPackageLink(LoanApplicationDto loanApplication) {
        loanApplication.add(
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(LoanApplicationController.class)
                                        .signDocumentsForLoanApplication(loanApplication.getId()))
                        .withRel("sign-document-package"));
    }

}

package io.github.flexibletech.offering.web;

import io.github.flexibletech.offering.application.LoanApplicationService;
import io.github.flexibletech.offering.application.dto.ConditionsDto;
import io.github.flexibletech.offering.application.dto.LoanApplicationDto;
import io.github.flexibletech.offering.application.dto.StartNewLoanApplicationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanApplicationDto startNewLoanApplication(@Valid @RequestBody StartNewLoanApplicationRequest request) {
        var loanApplication = loanApplicationService.startNewLoanApplication(request);
        HypermediaUtil.addLinks(loanApplication);
        return loanApplication;
    }

    @PostMapping("/{id}/conditions")
    public ResponseEntity<Void> choseConditionsForLoanApplication(@PathVariable("id") String loanApplicationId,
                                                                  @Valid @RequestBody ConditionsDto conditions) {
        loanApplicationService.choseConditionsForLoanApplication(loanApplicationId, conditions);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/documents/signature")
    public ResponseEntity<Void> signDocumentsForLoanApplication(@PathVariable("id") String loanApplicationId) {
        loanApplicationService.signDocumentsForLoanApplication(loanApplicationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoanApplicationDto findLoanApplicationById(@PathVariable("id") String loanApplicationId) {
        var loanApplication = loanApplicationService.findLoanApplicationById(loanApplicationId);
        HypermediaUtil.addLinks(loanApplication);
        return loanApplication;
    }

}

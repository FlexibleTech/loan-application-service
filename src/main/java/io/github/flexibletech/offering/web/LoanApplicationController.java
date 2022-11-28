package io.github.flexibletech.offering.web;

import io.github.flexibletech.offering.application.loanapplication.LoanApplicationService;
import io.github.flexibletech.offering.application.loanapplication.dto.ChoseConditionsRequest;
import io.github.flexibletech.offering.application.loanapplication.dto.LoanApplicationDto;
import io.github.flexibletech.offering.application.loanapplication.dto.StartNewLoanApplicationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "LoanApplication API", description = "Реализует REST API для управления жизненным циклом заявки на кредит")
@RequestMapping("/v1/loan-applications")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать заявку на кредит", security = @SecurityRequirement(name = "auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заявка на кредит успешно создана",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoanApplicationDto.class))}),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")})
    public LoanApplicationDto startNewLoanApplication(@Valid @RequestBody StartNewLoanApplicationRequest request) {
        var loanApplication = loanApplicationService.startNewLoanApplication(request);
        HypermediaUtil.addLinks(loanApplication);
        return loanApplication;
    }

    @PostMapping("/{id}/conditions")
    @Operation(summary = "Выбор условий кредита", security = @SecurityRequirement(name = "auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Условия успешно выбраны"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "404", description = "Заявка на кредит с данным id не найдена")})
    public ResponseEntity<Void> choseConditionsForLoanApplication(@PathVariable("id") String loanApplicationId,
                                                                  @Valid @RequestBody ChoseConditionsRequest conditions) {
        loanApplicationService.choseConditionsForLoanApplication(loanApplicationId, conditions);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/documents/sign")
    @Operation(summary = "Подпись пакета документов", security = @SecurityRequirement(name = "auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пакет документов успешно подписан"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "404", description = "Заявка на кредит с данным id не найдена")})
    public ResponseEntity<Void> signDocumentsForLoanApplication(@PathVariable("id") String loanApplicationId) {
        loanApplicationService.signDocumentPackageForLoanApplication(loanApplicationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Найти заявку на кредит по id", security = @SecurityRequirement(name = "auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заявка найдена",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoanApplicationDto.class))}),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка"),
            @ApiResponse(responseCode = "404", description = "Заявка на кредит с данным id не найдена")})
    public LoanApplicationDto findLoanApplicationById(@PathVariable("id") String loanApplicationId) {
        var loanApplication = loanApplicationService.findLoanApplicationById(loanApplicationId);
        HypermediaUtil.addLinks(loanApplication);
        return loanApplication;
    }

}

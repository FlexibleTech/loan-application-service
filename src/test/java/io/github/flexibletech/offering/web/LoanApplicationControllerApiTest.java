package io.github.flexibletech.offering.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.flexibletech.offering.application.loanapplication.LoanApplicationNotFoundException;
import io.github.flexibletech.offering.application.loanapplication.LoanApplicationService;
import io.github.flexibletech.offering.application.TestApplicationObjectsFactory;
import io.github.flexibletech.offering.application.loanapplication.dto.ConditionsDto;
import io.github.flexibletech.offering.application.loanapplication.dto.StartNewLoanApplicationRequest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.TestValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithMockUser
@WebMvcTest(LoanApplicationController.class)
public class LoanApplicationControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @Test
    public void shouldStartNewLoanApplication() throws Exception {
        Mockito.when(loanApplicationService.startNewLoanApplication(ArgumentMatchers.any(StartNewLoanApplicationRequest.class)))
                .thenReturn(TestApplicationObjectsFactory.newLoanApplicationDtoWithoutOffer());

        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/loan-applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newStartNewLoanApplicationRequest()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var expectedResponse = ResourceUtil.getString("classpath:json/startLoanApplicationResponse.json");
        JSONAssert.assertEquals(actualResponse, expectedResponse, JSONCompareMode.LENIENT);
    }

    @Test
    public void shouldGetLoanApplication() throws Exception {
        Mockito.when(loanApplicationService.findLoanApplicationById(Mockito.eq(TestValues.LOAN_APPLICATION_ID)))
                .thenReturn(TestApplicationObjectsFactory.newLoanApplicationDtoWithOffer());

        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/loan-applications/{id}", TestValues.LOAN_APPLICATION_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var expectedResponse = ResourceUtil.getString("classpath:json/findLoanApplicationResponse.json");
        JSONAssert.assertEquals(
                expectedResponse, actualResponse,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("offer.firstPaymentDate", (o, t1) -> true),
                        new Customization("offer.lastPaymentDate", (o, t1) -> true)));
    }

    @Test
    public void shouldChoseConditionsForLoanApplication() throws Exception {
        Mockito.when(loanApplicationService.choseConditionsForLoanApplication(Mockito.eq(TestValues.LOAN_APPLICATION_ID),
                        ArgumentMatchers.any(ConditionsDto.class)))
                .thenReturn(TestApplicationObjectsFactory.newConditionsDto());

        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/loan-applications/{id}/conditions", TestValues.LOAN_APPLICATION_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newConditionsDto()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertTrue(actualResponse.isBlank());
    }

    @Test
    public void signDocumentsForLoanApplication() throws Exception {
        Mockito.doNothing().when(loanApplicationService)
                .signDocumentPackageForLoanApplication(Mockito.eq(TestValues.LOAN_APPLICATION_ID));

        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/loan-applications/{id}/documents/sign", TestValues.LOAN_APPLICATION_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertTrue(actualResponse.isBlank());
    }

    @Test
    public void shouldReturn404Response() throws Exception {
        Mockito.when(loanApplicationService.findLoanApplicationById(Mockito.eq(TestValues.LOAN_APPLICATION_ID)))
                .thenThrow(new LoanApplicationNotFoundException(
                        String.format("Loan application with id %s is not found", TestValues.LOAN_APPLICATION_ID)));

        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/loan-applications/{id}", TestValues.LOAN_APPLICATION_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn()
                .getResponse();

        Assertions.assertEquals(actualResponse.getErrorMessage(), TestValues.LOAN_APPLICATION_NOT_FOUND_ERROR_MESSAGE);
    }

    @Test
    public void shouldReturn400Response() throws Exception {
        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/loan-applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newInvalidStartNewLoanApplicationRequest()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse();

        Assertions.assertEquals(actualResponse.getErrorMessage(), TestValues.EMPTY_CLIENT_NAME_MESSAGE);
    }

}

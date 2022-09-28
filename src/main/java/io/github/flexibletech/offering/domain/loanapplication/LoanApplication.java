package io.github.flexibletech.offering.domain.loanapplication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.github.flexibletech.offering.domain.AggregateRoot;
import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;
import io.github.flexibletech.offering.domain.loanapplication.issuance.IssuanceId;
import io.github.flexibletech.offering.domain.loanapplication.risk.ConditionsRestrictions;
import io.github.flexibletech.offering.domain.loanapplication.risk.RiskDecision;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOffer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Entity
@AllArgsConstructor
@Table(name = "loan_applications")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class LoanApplication extends AggregateRoot {
    @EmbeddedId
    @GenericGenerator(
            name = "loan_application_id_generator",
            strategy = "io.github.flexibletech.offering.infrastructure.persistence.LoanApplicationIdentifierGenerator",
            parameters = @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "loan_application_seq"))
    @GeneratedValue(
            generator = "loan_application_id_generator",
            strategy = GenerationType.SEQUENCE)
    private LoanApplicationId id;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "client_id"))
    private ClientId clientId;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Conditions conditions;
    @Enumerated(EnumType.STRING)
    private IncomeConfirmationType incomeConfirmationType = IncomeConfirmationType.TWO_NDFL;
    @Enumerated(EnumType.STRING)
    private LoanProgram loanProgram;
    private LocalDateTime completedAt;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private RiskDecision riskDecision;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Offer offer;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Set<Document> documentPackage = new HashSet<>();
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "issuance_id"))
    private IssuanceId issuanceId;

    @Transient
    //Ставка по кредиту в %
    public static final double LOAN_RATE = 16;

    private LoanApplication(ClientId clientId, LoanProgram loanProgram, Conditions conditions) {
        this.clientId = clientId;
        this.loanProgram = loanProgram;
        this.conditions = conditions;
    }

    /**
     * Получить решение рисков.
     *
     * @param riskDecision Решение рисков.
     */
    public void acceptRiskDecision(RiskDecision riskDecision) {
        if (riskDecision.isApproved()) this.status = Status.APPROVED;
        else {
            this.status = Status.DECLINED;
            this.completedAt = LocalDateTime.now();
        }

        this.riskDecision = riskDecision;
    }

    /**
     * Определение типа подтверждения дохода.
     *
     * @param preApprovedOffer Предодобренное предложение.
     * @return Тип подтверждения дохода.
     */
    public IncomeConfirmationType defineIncomeConfirmationType(PreApprovedOffer preApprovedOffer, Client client) {
        Supplier<IncomeConfirmationType> incomeConfirmationTypeSupplier =
                () -> {
                    if (Objects.isNull(client)) return IncomeConfirmationType.TWO_NDFL;
                    //Проверка условия - клиент зарплатный и указанный доход соответствует сумме зарплаты, полученной от рисков.
                    if (client.isPayroll() && riskDecision.doesIncomeMatchSalary(client.getIncome()))
                        return IncomeConfirmationType.SALARY_RECEIPT;
                    //Проверка условия - клиент премиальный и выбранная сумма меньше максимально допустимого значения.
                    if (client.isPremium() && this.conditions.isChosenAmountLessThanMaxPremiumAllowableValue())
                        return IncomeConfirmationType.NONE;
                    //Проверка условия - достпуна ли выбранная сумма без подтверждения.
                    if (this.conditions.isChosenAmountAvailableWithoutConfirmation(preApprovedOffer))
                        return IncomeConfirmationType.NONE;
                    return IncomeConfirmationType.TWO_NDFL;
                };

        this.incomeConfirmationType = incomeConfirmationTypeSupplier.get();
        return this.incomeConfirmationType;
    }

    /**
     * Выбор условий.
     *
     * @param amount    Сумма.
     * @param period    Период.
     * @param insurance Страховка.
     */
    public void choseConditions(BigDecimal amount, Integer period, Boolean insurance) {
        var newConditions = this.conditions.newConditions(Amount.fromValue(amount), period, insurance);
        if (!this.conditions.equals(newConditions)) this.conditions = newConditions;

        this.conditions = this.conditions.adjustAmountIfInsuranceChosen();
    }

    //Расчет предложения
    public void calculateOffer() {
        if (conditions.isInsurance())
            this.offer = Offer.newOfferWithInsurance(this.conditions);
        else this.offer = Offer.newOffer(this.conditions);
    }

    public Document getDocumentByType(Document.Type documentType) {
        return this.documentPackage.stream()
                .filter(document -> document.getType() == documentType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Document with type %s is not found", documentType)));
    }

    public void addDocument(String documentId, Document.Type documentType) {
        var document = Document.newUnsignedDocument(documentId, documentType);
        this.documentPackage.add(document);
    }

    public void waitForDocumentPackageSignature() {
        this.status = Status.PENDING_DOCUMENT_PACKAGE_SIGNATURE;
    }

    public String makeDocumentName(Document.Type documentType) {
        var epoch = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        return String.format("%s_%s_%s", epoch, this.id, documentType.name()) + Document.FORMAT;
    }

    public void signDocumentPackage() {
        var signedDocumentPackage = this.documentPackage
                .stream()
                .map(Document::sign)
                .collect(Collectors.toSet());
        this.documentPackage.clear();
        this.documentPackage.addAll(signedDocumentPackage);

        this.status = Status.PENDING_ISSUANCE;
    }

    @JsonIgnore
    public boolean isDocumentPackageSigned() {
        if (this.documentPackage.isEmpty()) return false;
        return this.documentPackage.stream()
                .map(Document::isSigned)
                .reduce(true, (signed1, signed2) -> signed1 && signed2);
    }

    public void complete(String issuanceId) {
        this.issuanceId = new IssuanceId(issuanceId);
        this.completedAt = LocalDateTime.now();
        this.status = Status.COMPLETED;
    }

    public void cancel() {
        this.status = Status.CANCEL;
        this.completedAt = LocalDateTime.now();
    }

    @JsonIgnore
    public boolean isDeclined() {
        return this.status == Status.DECLINED;
    }

    @JsonIgnore
    public boolean isCompleted() {
        return this.status == Status.COMPLETED;
    }

    public ConditionsRestrictions getConditionsRestrictions() {
        return Optional.ofNullable(this.riskDecision)
                .map(RiskDecision::getConditionsRestrictions)
                .orElse(null);
    }

    public Amount getAmount() {
        return this.conditions.getAmount();
    }

    public Integer getPeriod() {
        return this.conditions.getPeriod();
    }

    public boolean isInsurance() {
        return this.conditions.isInsurance();
    }

    public static LoanApplication newLoanApplication(Client client, PreApprovedOffer preApprovedOffer,
                                                     BigDecimal amount, int period, boolean insurance) {
        var loanProgram = defineLoanProgramForClient(client, preApprovedOffer);
        //Доход супруга(-ги) может быть указан только если клиент не холост.
        if (client.hasSpouseIncomeAndUnmarried())
            throw new IllegalArgumentException(String.format("Unable to specify spouse income for unmarried client %s",
                    client.getId()));
        var conditions = Conditions.newConditions(amount, period, insurance);

        return new LoanApplication(client.getId(), loanProgram, conditions);
    }

    /**
     * Оределение программы кредитования в зависимости от категории клиента
     * и наличия предодобренного предложения.
     *
     * @param client           Клиент.
     * @param preApprovedOffer Предодобренное предложение.
     * @return Программа кредитования.
     */
    private static LoanApplication.LoanProgram defineLoanProgramForClient(Client client, PreApprovedOffer preApprovedOffer) {
        if (client.isPayroll()) return LoanApplication.LoanProgram.PAYROLL_CLIENT;
        if (client.isPremium()) return LoanApplication.LoanProgram.SPECIAL;
        if (Objects.nonNull(preApprovedOffer)) return LoanApplication.LoanProgram.PREAPPROVED;

        return LoanApplication.LoanProgram.COMMON;
    }

    public enum Status {
        NEW,
        APPROVED,
        DECLINED,
        PENDING_DOCUMENT_PACKAGE_SIGNATURE,
        PENDING_ISSUANCE,
        CANCEL,
        COMPLETED
    }

    public enum LoanProgram {
        COMMON,
        PREAPPROVED,
        PAYROLL_CLIENT,
        SPECIAL
    }

    public enum IncomeConfirmationType {
        NONE,
        SALARY_RECEIPT,
        TWO_NDFL
    }

    public static Builder newBuilder() {
        return new LoanApplication().new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Builder {

        public Builder withId(LoanApplicationId id) {
            LoanApplication.this.id = id;
            return this;
        }

        public Builder withClient(String clientId) {
            LoanApplication.this.clientId = new ClientId(clientId);
            return this;
        }

        public Builder withStatus(Status status) {
            LoanApplication.this.status = status;
            return this;
        }

        public Builder withConditions(Conditions conditions) {
            LoanApplication.this.conditions = conditions;
            return this;
        }

        public Builder withLoanProgram(LoanProgram loanProgram) {
            LoanApplication.this.loanProgram = loanProgram;
            return this;
        }

        public Builder withRiskDecision(RiskDecision riskDecision) {
            LoanApplication.this.riskDecision = riskDecision;
            return this;
        }

        public Builder withOffer(Offer offer) {
            LoanApplication.this.offer = offer;
            return this;
        }

        public Builder withDocumentPackage(Document... documents) {
            LoanApplication.this.documentPackage = new HashSet<>(Arrays.asList(documents));
            return this;
        }

        public LoanApplication build() {
            return LoanApplication.this;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoanApplication that = (LoanApplication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

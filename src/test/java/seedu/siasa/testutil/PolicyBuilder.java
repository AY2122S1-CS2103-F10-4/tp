package seedu.siasa.testutil;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import seedu.siasa.model.person.Person;
import seedu.siasa.model.policy.Commission;
import seedu.siasa.model.policy.CoverageExpiryDate;
import seedu.siasa.model.policy.Policy;
import seedu.siasa.model.policy.PaymentStructure;
import seedu.siasa.model.policy.Title;

public class PolicyBuilder {
    public static final int DEFAULT_COMMISSION_PERCENTAGE = 10;
    public static final int DEFAULT_COMMISSION_NUMBER_OF_PAYMENTS = 10;
    public static final int DEFAULT_PAYMENT_AMOUNT = 100;
    public static final int DEFAULT_PAYMENT_FREQUENCY = 1;
    public static final int DEFAULT_NUMBER_OF_PAYMENTS = 10;
    public static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.now().plusYears(1);
    public static final String DEFAULT_TITLE = "Full Life Plan";

    private Title title;
    private PaymentStructure paymentStructure;
    private CoverageExpiryDate coverageExpiryDate;
    private Commission commission;

    private Person owner;

    /**
     * Creates a {@code PolicyBuilder} with the default details and  owner.
     */
    public PolicyBuilder(Person owner) {
        requireNonNull(owner);
        this.title = new Title(DEFAULT_TITLE);
        this.paymentStructure =
                new PaymentStructure(DEFAULT_PAYMENT_AMOUNT, DEFAULT_PAYMENT_FREQUENCY, DEFAULT_NUMBER_OF_PAYMENTS);
        this.coverageExpiryDate = new CoverageExpiryDate(DEFAULT_EXPIRY_DATE);
        this.commission = new Commission(DEFAULT_COMMISSION_PERCENTAGE, DEFAULT_COMMISSION_NUMBER_OF_PAYMENTS);
        this.owner = owner;
    }

    /**
     * Initializes the PolicyBuilder with the data of {@code policyToCopy}.
     */
    public PolicyBuilder(Policy policyToCopy) {
        requireNonNull(policyToCopy);
        this.title = policyToCopy.getTitle();
        this.paymentStructure = policyToCopy.getPaymentStructure();
        this.coverageExpiryDate = policyToCopy.getCoverageExpiryDate();
        this.commission = policyToCopy.getCommission();
        this.owner = policyToCopy.getOwner();
    }

    /**
     * Sets the {@code Title} of the {@code Policy} that we are building.
     */
    public PolicyBuilder withTitle(String title) {
        this.title = new Title(title);
        return this;
    }

    /**
     * Sets the {@code Price} of the {@code Policy} that we are building.
     * @param paymentAmount
     * @param paymentFrequency
     * @param numberOfPayments
     */
    public PolicyBuilder withPaymentStructure(int paymentAmount, int paymentFrequency, int numberOfPayments) {
        this.paymentStructure = new PaymentStructure(paymentAmount, paymentFrequency, numberOfPayments);
        return this;
    }

    public PolicyBuilder withPaymentStructure(int paymentAmount) {
        this.paymentStructure = new PaymentStructure(paymentAmount, 1, 1);
        return this;
    }

    /**
     * Sets the {@code Commission} of the {@code Policy} that we are building.
     */
    public PolicyBuilder withCommission(int commissionPercentage, int numberOfPayments) {
        this.commission = new Commission(commissionPercentage, numberOfPayments);
        return this;
    }

    public PolicyBuilder withCommission(int commissionPercentage) {
        this.commission = new Commission(commissionPercentage, 0);
        return this;
    }

    /**
     * Sets the {@code ExpiryDate} of the {@code Policy} that we are building.
     */
    public PolicyBuilder withExpiryDate(LocalDate date) {
        this.coverageExpiryDate = new CoverageExpiryDate(date);
        return this;
    }

    /**
     * Sets the {@code Owner} of the {@code Policy} that we are building.
     */
    public PolicyBuilder withOwner(Person owner) {
        this.owner = owner;
        return this;
    }

    public Policy build() {
        return new Policy(title, paymentStructure, coverageExpiryDate, commission, owner);
    }

}

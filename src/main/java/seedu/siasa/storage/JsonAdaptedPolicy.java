package seedu.siasa.storage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.siasa.commons.exceptions.IllegalValueException;
import seedu.siasa.model.person.Person;
import seedu.siasa.model.policy.Commission;
import seedu.siasa.model.policy.CoverageExpiryDate;
import seedu.siasa.model.policy.Policy;
import seedu.siasa.model.policy.PaymentStructure;
import seedu.siasa.model.policy.Title;


/**
 * Jackson-friendly version of {@link Policy}.
 */
public class JsonAdaptedPolicy {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Policy's %s field is missing!";

    private final String title;
    private final JsonAdaptedPaymentStructure paymentStructure;
    private final String coverageExpiryDate;
    private final JsonAdaptedCommission commission;
    private final JsonAdaptedPerson owner;

    /**
     * Constructs a {@code JsonAdaptedPolicy} with the given policy details.
     */
    @JsonCreator
    public JsonAdaptedPolicy(@JsonProperty("title") String title,
                             @JsonProperty("paymentStructure") JsonAdaptedPaymentStructure paymentStructure,
                             @JsonProperty("coverageExpiryDate") String coverageExpiryDate,
                             @JsonProperty("commission") JsonAdaptedCommission commission,
                             @JsonProperty("owner") JsonAdaptedPerson owner) {
        this.title = title;
        this.paymentStructure = paymentStructure;
        this.coverageExpiryDate = coverageExpiryDate;
        this.commission = commission;
        this.owner = owner;
    }

    /**
     * Converts a given {@code Policy} into this class for Jackson use.
     */
    public JsonAdaptedPolicy(Policy source) {
        title = source.getTitle().toString();
        paymentStructure = new JsonAdaptedPaymentStructure(source.getPaymentStructure());
        coverageExpiryDate = source.getCoverageExpiryDate().toString();
        commission = new JsonAdaptedCommission(source.getCommission());
        owner = new JsonAdaptedPerson(source.getOwner());
    }

    public JsonAdaptedPerson getOwner() {
        return owner;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Policy} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted policy.
     */
    public Policy toModelType(Person policyOwner) throws IllegalValueException {
        if (title == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Title.class.getSimpleName()));
        }
        if (!Title.isValidTitle(title)) {
            throw new IllegalValueException(Title.MESSAGE_CONSTRAINTS);
        }
        final Title modelTitle = new Title(title);

        if (paymentStructure == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, PaymentStructure.class.getSimpleName()));
        }

        final PaymentStructure modelPaymentStructure = paymentStructure.toModelType();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            if (coverageExpiryDate == null) {
                throw new IllegalValueException(
                        String.format(MISSING_FIELD_MESSAGE_FORMAT, CoverageExpiryDate.class.getSimpleName()));
            }
            LocalDate date = LocalDate.parse(coverageExpiryDate, formatter);
            if (!CoverageExpiryDate.isValidExpiryDate(date)) {
                throw new IllegalValueException(CoverageExpiryDate.MESSAGE_CONSTRAINTS);
            }
        } catch (IllegalValueException | DateTimeParseException e) {
            throw new IllegalValueException(e.getMessage());
        }

        LocalDate date = LocalDate.parse(coverageExpiryDate, formatter);
        final CoverageExpiryDate modelCoverageExpiryDate = new CoverageExpiryDate(date);

        if (commission == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Commission.class.getSimpleName()));
        }

        final Commission modelCommission = commission.toModelType(modelPaymentStructure);

        if (policyOwner == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName()));
        }
        return new Policy(modelTitle, modelPaymentStructure, modelCoverageExpiryDate, modelCommission, policyOwner);
    }
}

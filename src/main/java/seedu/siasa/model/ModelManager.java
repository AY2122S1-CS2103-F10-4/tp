package seedu.siasa.model;

import static java.util.Objects.requireNonNull;
import static seedu.siasa.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.siasa.model.contact.PersonComparator.CONTACT_SORT_BY_ALPHA_ASC;
import static seedu.siasa.model.policy.PolicyComparator.POLICY_SORT_BY_ALPHA_ASC;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.siasa.commons.core.GuiSettings;
import seedu.siasa.commons.core.LogsCenter;
import seedu.siasa.model.contact.Contact;
import seedu.siasa.model.policy.Policy;

/**
 * Represents the in-memory model of the SIASA data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Siasa siasa;
    private final UserPrefs userPrefs;
    private final FilteredList<Contact> filteredContacts;
    private final FilteredList<Policy> filteredPolicies;
    private final SortedList<Contact> sortedContacts;
    private final SortedList<Policy> sortedPolicies;

    /**
     * Initializes a ModelManager with the given SIASA and userPrefs.
     */
    public ModelManager(ReadOnlySiasa siasa, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(siasa, userPrefs);

        logger.fine("Initializing with SIASA: " + siasa + " and user prefs " + userPrefs);

        this.siasa = new Siasa(siasa);
        this.userPrefs = new UserPrefs(userPrefs);
        sortedContacts = new SortedList<>(this.siasa.getContactList(), CONTACT_SORT_BY_ALPHA_ASC);
        sortedPolicies = new SortedList<>(this.siasa.getPolicyList(), POLICY_SORT_BY_ALPHA_ASC);
        filteredContacts = new FilteredList<>(sortedContacts);
        filteredPolicies = new FilteredList<>(sortedPolicies);
    }

    public ModelManager() {
        this(new Siasa(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSiasaFilePath() {
        return userPrefs.getSiasaFilePath();
    }

    @Override
    public void setSiasaFilePath(Path siasaFilePathPath) {
        requireNonNull(siasaFilePathPath);
        userPrefs.setSiasaFilePath(siasaFilePathPath);
    }

    //=========== Siasa ================================================================================

    @Override
    public void setSiasa(ReadOnlySiasa siasa) {
        this.siasa.resetData(siasa);
    }

    @Override
    public ReadOnlySiasa getSiasa() {
        return siasa;
    }

    //=========== Person CRUD ================================================================================

    @Override
    public boolean hasContact(Contact contact) {
        requireNonNull(contact);
        return siasa.hasContact(contact);
    }

    @Override
    public Optional<Contact> getSimilarContact(Contact contact) {
        requireNonNull(contact);
        return siasa.getSimilarContact(contact);
    }

    @Override
    public void deleteContact(Contact target) {
        siasa.removeContactAndAssociatedPolicies(target);
    }

    @Override
    public void addContact(Contact contact) {
        siasa.addContact(contact);
        removeAllFilters();
    }

    @Override
    public void setContact(Contact target, Contact editedContact) {
        requireAllNonNull(target, editedContact);

        siasa.setContact(target, editedContact);
    }

    @Override
    public Map<Contact, Integer> getNumberPoliciesPerContact() {
        return siasa.getNumberPoliciesPerContact();
    }

    @Override
    public Map<Contact, Integer> getCommissionPerContact() {
        return siasa.getCommissionPerContact();
    }

    //=========== Policy CRUD ================================================================================

    @Override
    public boolean hasPolicy(Policy policy) {
        requireNonNull(policy);
        return siasa.hasPolicy(policy);
    }

    @Override
    public Optional<Policy> getSimilarPolicy(Policy policy) {
        requireNonNull(policy);
        return siasa.getSimilarPolicy(policy);
    }

    @Override
    public void deletePolicy(Policy target) {
        siasa.removePolicy(target);
    }

    @Override
    public void addPolicy(Policy person) {
        siasa.addPolicy(person);
        removeAllFilters();
    }

    @Override
    public void setPolicy(Policy target, Policy editedPolicy) {
        requireAllNonNull(target, editedPolicy);

        siasa.setPolicy(target, editedPolicy);
    }

    @Override
    public void removePoliciesBelongingTo(Contact target) {
        siasa.removePoliciesBelongingTo(target);
    }

    @Override
    public int getTotalCommission() {
        return siasa.getTotalCommission();
    }

    //=========== Filtered Contact List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Contact} backed by the internal list of
     * {@code versionedSiasa}
     */
    @Override
    public ObservableList<Contact> getFilteredContactList() {
        return filteredContacts;
    }

    @Override
    public void updateFilteredContactList(Predicate<Contact> predicate) {
        requireNonNull(predicate);
        filteredContacts.setPredicate(predicate);
    }

    @Override
    public void updateFilteredContactList(Comparator<Contact> comparator) {
        requireNonNull(comparator);
        sortedContacts.setComparator(comparator);
    }

    @Override
    public Predicate<Policy> getCurrentPolicyPredicate() {
        @SuppressWarnings("unchecked")
        Predicate<Policy> result = (Predicate<Policy>) filteredPolicies.getPredicate();
        return result;
    }

    //=========== Filtered Policy List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Policy} backed by the internal list of
     * {@code versionedSiasa}
     */
    @Override
    public ObservableList<Policy> getFilteredPolicyList() {
        return filteredPolicies;
    }

    @Override
    public void updateFilteredPolicyList(Predicate<Policy> predicate) {
        requireNonNull(predicate);
        filteredPolicies.setPredicate(predicate);
    }

    @Override
    public void updateFilteredPolicyList(Comparator<Policy> comparator) {
        requireNonNull(comparator);
        sortedPolicies.setComparator(comparator);
    }

    /**
     * Removes all filters on the filtered person list.
     */
    public void removeAllFilters() {
        updateFilteredContactList(PREDICATE_SHOW_ALL_CONTACTS);
        updateFilteredPolicyList(PREDICATE_SHOW_ALL_POLICIES);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return siasa.equals(other.siasa)
                && userPrefs.equals(other.userPrefs)
                && filteredContacts.equals(other.filteredContacts)
                && filteredPolicies.equals(other.filteredPolicies);
    }

}

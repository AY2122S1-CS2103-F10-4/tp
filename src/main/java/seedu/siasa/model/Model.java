package seedu.siasa.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.siasa.commons.core.GuiSettings;
import seedu.siasa.model.contact.Contact;
import seedu.siasa.model.policy.Policy;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Contact> PREDICATE_SHOW_ALL_CONTACTS = unused -> true;
    Predicate<Policy> PREDICATE_SHOW_ALL_POLICIES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' SIASA file path.
     */
    Path getSiasaFilePath();

    /**
     * Sets the user prefs' SIASA file path.
     */
    void setSiasaFilePath(Path siasaFilePathPath);

    /**
     * Replaces SIASA data with the data in {@code siasa}.
     */
    void setSiasa(ReadOnlySiasa siasa);

    /** Returns the AddressBook */
    ReadOnlySiasa getSiasa();

    /**
     * Returns true if a contact with the same identity as {@code contact} exists in the SIASA.
     */
    boolean hasContact(Contact contact);

    /**
     * Returns true if a contact with a similar full name as {@code contact}
     * exists in SIASA. Similar is defined as full names having an edit distance of zero
     * or one (case insensitive).
     */
    Optional<Contact> getSimilarContact(Contact contact);

    /**
     * Deletes the given contact.
     * The contact must exist in the SIASA.
     */
    void deleteContact(Contact target);

    /**
     * Adds the given contact.
     * {@code contact} must not already exist in the SIASA.
     */
    void addContact(Contact contact);

    /**
     * Replaces the given contact {@code target} with {@code editedContact}.
     * {@code target} must exist in the SIASA.
     * The contact identity of {@code editedContact} must not be the same as another existing contact in the SIASA.
     */
    void setContact(Contact target, Contact editedContact);

    /** Returns an unmodifiable view of the filtered contact list */
    ObservableList<Contact> getFilteredContactList();

    /**
     * Updates the filter of the contact list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredContactList(Predicate<Contact> predicate);

    /**
     * Updates the comparator of the contact list to sort by the given {@code comparator}.
     * @throws NullPointerException if {@code comparator} is null.
     */
    void updateFilteredContactList(Comparator<Contact> comparator);

    /**
     * Returns a map of contacts and the number of policies each of them has.
     */
    Map<Contact, Integer> getNumberPoliciesPerContact();

    /**
     * Returrns a map of contacts and the commission from each contact.
     */
    Map<Contact, Integer> getCommissionPerContact();

    /**
     * Returns true if a policy with the same identity as {@code policy} exists in the SIASA.
     */
    boolean hasPolicy(Policy policy);

    /**
     * Returns true if a policy with similar title and the same owner as {@code policy}
     * exists in the SIASA. Similar titles are defined as having edit distance of zero or
     * one (case insensitive).
     */
    Optional<Policy> getSimilarPolicy(Policy policy);

    /**
     * Deletes the given policy.
     * The policy must exist in the SIASA.
     */
    void deletePolicy(Policy target);

    /**
     * Adds the given policy.
     * {@code policy} must not already exist in the SIASA.
     */
    void addPolicy(Policy policy);

    /**
     * Replaces the given policy {@code target} with {@code editedPolicy}.
     * {@code target} must exist in the SIASA.
     * The policy identity of {@code editedPolicy} must not be the same as another existing policy in the SIASA.
     */
    void setPolicy(Policy target, Policy editedPolicy);

    /**
     * Returns the total commission of the policy list.
     */
    int getTotalCommission();

    /**
     * Removes all policies belonging to the given person {@code target}.
     * {@code target} must exist in the SIASA.
     */
    void removePoliciesBelongingTo(Contact target);

    /** Returns an unmodifiable view of the filtered policy list */
    ObservableList<Policy> getFilteredPolicyList();

    /**
     * Updates the filter of the policy list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPolicyList(Predicate<Policy> predicate);

    /**
     * Updates the comparator of the policy list to sort by the given {@code comparator}.
     * @throws NullPointerException if {@code comparator} is null.
     */
    void updateFilteredPolicyList(Comparator<Policy> predicate);

    /**
     * Gets the current {@code predicate} applied to the Policy list.
     */
    Predicate<Policy> getCurrentPolicyPredicate();
}

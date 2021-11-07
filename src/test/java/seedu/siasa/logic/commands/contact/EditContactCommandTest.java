package seedu.siasa.logic.commands.contact;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.siasa.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.siasa.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.siasa.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.siasa.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.siasa.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.siasa.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.siasa.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.siasa.logic.commands.CommandTestUtil.showContactAtIndex;
import static seedu.siasa.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;
import static seedu.siasa.testutil.TypicalIndexes.INDEX_SECOND_CONTACT;
import static seedu.siasa.testutil.TypicalSiasa.getTypicalSiasa;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.util.Pair;
import seedu.siasa.commons.core.Messages;
import seedu.siasa.commons.core.index.Index;
import seedu.siasa.logic.commands.ClearCommand;
import seedu.siasa.logic.commands.contact.EditContactCommand.EditContactDescriptor;
import seedu.siasa.model.Model;
import seedu.siasa.model.ModelManager;
import seedu.siasa.model.Siasa;
import seedu.siasa.model.UserPrefs;
import seedu.siasa.model.contact.Contact;
import seedu.siasa.model.policy.Policy;
import seedu.siasa.testutil.ContactBuilder;
import seedu.siasa.testutil.EditContactDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditContactCommandTest {

    private Model model = new ModelManager(getTypicalSiasa(), new UserPrefs());

    /**
     * Helper method that returns a new `Policy` with reference to the provided `Contact`
     */
    private static Policy newPolicyWtihNewOwner(Policy policy, Contact owner) {
        return new Policy(
                policy.getTitle(),
                policy.getPaymentStructure(),
                policy.getCoverageExpiryDate().orElse(null),
                policy.getCommission(),
                owner,
                policy.getTags());
    }

    /**
     * Helper method that updates all `Policy`s that were owned by `contactToEdit` to be owned by `editedContact`
     */
    private static void updateModelAfterContactChange(Model model, Contact contactToEdit, Contact editedContact) {
        List<Policy> policyList = model.getFilteredPolicyList();
        ArrayList<Pair<Policy, Policy>> policiesToBeUpdated = new ArrayList<>();
        for (Policy p : policyList) {
            if (p.getOwner().equals(contactToEdit)) {
                policiesToBeUpdated.add(new Pair<>(p, newPolicyWtihNewOwner(p, editedContact)));
            }
        }
        for (Pair<Policy, Policy> pair : policiesToBeUpdated) {
            model.setPolicy(pair.getKey(), pair.getValue());
        }
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Contact editedContact = new ContactBuilder().build();
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(editedContact).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT, descriptor);

        String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new Siasa(model.getSiasa()), new UserPrefs());

        updateModelAfterContactChange(expectedModel, model.getFilteredContactList().get(0), editedContact);

        expectedModel.setContact(model.getFilteredContactList().get(0), editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastContact = Index.fromOneBased(model.getFilteredContactList().size());
        Contact lastContact = model.getFilteredContactList().get(indexLastContact.getZeroBased());

        ContactBuilder contactInList = new ContactBuilder(lastContact);
        Contact editedContact = contactInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withTags(VALID_TAG_HUSBAND).build();

        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditContactCommand editContactCommand = new EditContactCommand(indexLastContact, descriptor);

        String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new Siasa(model.getSiasa()), new UserPrefs());
        expectedModel.setContact(lastContact, editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditContactCommand editContactCommand =
            new EditContactCommand(INDEX_FIRST_CONTACT, new EditContactDescriptor());
        Contact editedContact = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());

        String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new Siasa(model.getSiasa()), new UserPrefs());

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);

        Contact contactInFilteredList = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Contact editedContact = new ContactBuilder(contactInFilteredList).withName(VALID_NAME_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT,
            new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new Siasa(model.getSiasa()), new UserPrefs());

        updateModelAfterContactChange(expectedModel, model.getFilteredContactList().get(0), editedContact);

        expectedModel.setContact(model.getFilteredContactList().get(0), editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateContactUnfilteredList_failure() {
        Contact firstContact = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(firstContact).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_SECOND_CONTACT, descriptor);

        assertCommandFailure(editContactCommand, model, EditContactCommand.MESSAGE_DUPLICATE_CONTACT);
    }

    @Test
    public void execute_duplicateContactFilteredList_failure() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);

        // edit person in filtered list into a duplicate in address book
        Contact contactInList = model.getSiasa().getContactList().get(INDEX_SECOND_CONTACT.getZeroBased());
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT,
            new EditContactDescriptorBuilder(contactInList).build());

        assertCommandFailure(editContactCommand, model, EditContactCommand.MESSAGE_DUPLICATE_CONTACT);
    }

    @Test
    public void execute_invalidContactIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editContactCommand, model, Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidContactIndexFilteredList_failure() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);
        Index outOfBoundIndex = INDEX_SECOND_CONTACT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getSiasa().getContactList().size());

        EditContactCommand editContactCommand = new EditContactCommand(outOfBoundIndex,
            new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editContactCommand, model, Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditContactCommand standardCommand = new EditContactCommand(INDEX_FIRST_CONTACT, DESC_AMY);

        // same values -> returns true
        EditContactDescriptor copyDescriptor = new EditContactDescriptor(DESC_AMY);
        EditContactCommand commandWithSameValues = new EditContactCommand(INDEX_FIRST_CONTACT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditContactCommand(INDEX_SECOND_CONTACT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditContactCommand(INDEX_FIRST_CONTACT, DESC_BOB)));
    }

}

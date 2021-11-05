package seedu.siasa.logic.commands.contact;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.siasa.logic.commands.Command;
import seedu.siasa.logic.commands.CommandResult;
import seedu.siasa.model.Model;
import seedu.siasa.model.contact.Contact;

/**
 * Sorts the contact list.
 */
public class SortContactCommand extends Command {

    public static final String COMMAND_WORD = "sortcontact";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the contact list alphabetically by the order specified.\n"
            + "Parameters: ORDER (asc, dsc)\n"
            + "Example: " + COMMAND_WORD + " dsc";

    public static final String MESSAGE_SUCCESS = "Sorted contacts";

    public static final String MESSAGE_NO_SUCH_COMPARATOR = "No such sorting order";

    public static final String MESSAGE_NO_POLICIES = "There are no policies to sort.";

    private final Comparator<Contact> comparator;

    public SortContactCommand() {
        this.comparator = null;
    }

    public SortContactCommand(Comparator<Contact> comparator) {
        this.comparator = comparator;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (comparator != null) {
            model.updateFilteredContactList(comparator);
            if (model.getFilteredPolicyList().isEmpty()) {
                return new CommandResult(MESSAGE_NO_POLICIES);
            } else {
                return new CommandResult(MESSAGE_SUCCESS);
            }
        } else {
            return new CommandResult(MESSAGE_NO_SUCH_COMPARATOR);
        }
    }
}

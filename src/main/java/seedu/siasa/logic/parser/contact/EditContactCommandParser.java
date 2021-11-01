package seedu.siasa.logic.parser.contact;

import static java.util.Objects.requireNonNull;
import static seedu.siasa.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.siasa.commons.core.index.Index;
import seedu.siasa.logic.commands.contact.EditContactCommand;
import seedu.siasa.logic.commands.contact.EditContactCommand.EditContactDescriptor;
import seedu.siasa.logic.parser.ArgumentMultimap;
import seedu.siasa.logic.parser.ArgumentTokenizer;
import seedu.siasa.logic.parser.Parser;
import seedu.siasa.logic.parser.ParserUtil;
import seedu.siasa.logic.parser.exceptions.ParseException;
import seedu.siasa.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditContactCommand object
 */
public class EditContactCommandParser implements Parser<EditContactCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditContactCommand
     * and returns an EditContactCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditContactCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    EditContactCommand.MESSAGE_USAGE), pe);
        }

        EditContactDescriptor editContactDescriptor = new EditContactCommand.EditContactDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editContactDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editContactDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editContactDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editContactDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editContactDescriptor::setTags);

        if (!editContactDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditContactCommand.MESSAGE_NOT_EDITED);
        }

        return new EditContactCommand(index, editContactDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
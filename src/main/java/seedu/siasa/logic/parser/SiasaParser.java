package seedu.siasa.logic.parser;

import static seedu.siasa.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.siasa.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.siasa.logic.commands.ClearCommand;
import seedu.siasa.logic.commands.Command;
import seedu.siasa.logic.commands.client.EditClientCommand;
import seedu.siasa.logic.commands.ExitCommand;
import seedu.siasa.logic.commands.client.FindClientCommand;
import seedu.siasa.logic.commands.HelpCommand;
import seedu.siasa.logic.commands.client.AddClientCommand;
import seedu.siasa.logic.commands.client.ClearClientPolicyCommand;
import seedu.siasa.logic.commands.client.DeleteClientCommand;
import seedu.siasa.logic.commands.client.ListClientCommand;
import seedu.siasa.logic.commands.client.ListClientPolicyCommand;
import seedu.siasa.logic.commands.policy.AddPolicyCommand;
import seedu.siasa.logic.commands.policy.DeletePolicyCommand;
import seedu.siasa.logic.commands.policy.ListPolicyCommand;
import seedu.siasa.logic.parser.client.AddClientCommandParser;
import seedu.siasa.logic.parser.client.ClearClientPolicyCommandParser;
import seedu.siasa.logic.parser.client.DeleteClientCommandParser;
import seedu.siasa.logic.parser.client.ListClientPolicyCommandParser;
import seedu.siasa.logic.parser.exceptions.ParseException;
import seedu.siasa.logic.parser.policy.AddPolicyCommandParser;
import seedu.siasa.logic.parser.policy.DeletePolicyCommandParser;

/**
 * Parses user input.
 */
public class SiasaParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddClientCommand.COMMAND_WORD:
            return new AddClientCommandParser().parse(arguments);

        case AddPolicyCommand.COMMAND_WORD:
            return new AddPolicyCommandParser().parse(arguments);

        case EditClientCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteClientCommand.COMMAND_WORD:
            return new DeleteClientCommandParser().parse(arguments);

        case DeletePolicyCommand.COMMAND_WORD:
            return new DeletePolicyCommandParser().parse(arguments);

        case ClearClientPolicyCommand.COMMAND_WORD:
            return new ClearClientPolicyCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindClientCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListClientPolicyCommand.COMMAND_WORD:
            return new ListClientPolicyCommandParser().parse(arguments);

        case ListClientCommand.COMMAND_WORD:
            return new ListClientCommand();

        case ListPolicyCommand.COMMAND_WORD:
            return new ListPolicyCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}

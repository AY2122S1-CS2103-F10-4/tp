package seedu.siasa.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.collections.ObservableList;
import seedu.siasa.logic.commands.exceptions.CommandException;
import seedu.siasa.model.Model;
import seedu.siasa.model.person.Person;
import seedu.siasa.model.policy.Policy;
import seedu.siasa.model.policy.PolicyIsOwnedByPredicate;

public class DownloadCommand extends Command {

    public static final String COMMAND_WORD = "download";

    public static final String CURRENT_DATE = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    public static final String TXT_FILEPATH = "data\\stats.txt";
    public static final String MESSAGE_DOWNLOAD_SUCCESS = "File has been downloaded, you can view it at "
            + TXT_FILEPATH;
    public static final String MESSAGE_ERROR_WRITING_FILE = "There was an error saving the file.";
    public static final String TITLE_UNDERLINE = "-----------------------";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        int totalCommission = model.getTotalCommission();

        Map<Person, Integer> numberPoliciesPerPerson = model.getNumberPoliciesPerPerson();

        List<Person> modifiablePersonList = new ArrayList<>(model.getFilteredPersonList());

        // TODO: Abstract out when comparators for sorting policies are finished.
        modifiablePersonList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person personA, Person personB) {
                model.updateFilteredPolicyList(new PolicyIsOwnedByPredicate(personA));
                ObservableList<Policy> personAPolicies = model.getFilteredPolicyList();
                int commissionFromPersonA = getCommissionFromPolicyList(personAPolicies);

                model.updateFilteredPolicyList(new PolicyIsOwnedByPredicate(personB));
                ObservableList<Policy> personBPolicies = model.getFilteredPolicyList();
                int commissionFromPersonB = getCommissionFromPolicyList(personBPolicies);

                model.updateFilteredPolicyList(x -> true);

                return (commissionFromPersonB - commissionFromPersonA);
            }
        });

        List<String> listStringForTxt = stringListBuilderForTxt(totalCommission, modifiablePersonList, numberPoliciesPerPerson);

        try {
            writeToTxt(listStringForTxt);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_ERROR_WRITING_FILE);
        }

        return new CommandResult(MESSAGE_DOWNLOAD_SUCCESS);
    }

    private int getCommissionFromPolicyList(List<Policy> policyList) {
        float total = 0;
        for (Policy policy : policyList) {
            total = total + ((float) policy.getCommission().commissionPercentage / 100)
                    * policy.getPrice().priceInCents;
        }
        return (int) total;
    }

    private float getAvgPoliciesPerClient(Map<Person, Integer> numberPoliciesPerPerson) {
        int countPersons = numberPoliciesPerPerson.size();
        int countPolicies = numberPoliciesPerPerson.values()
                .stream().mapToInt(Integer::intValue).sum();
        return (float) countPolicies / countPersons;
    }

    private List<String> stringListBuilderForTxt(int totalCommission, List<Person> sortedPersons, Map<Person, Integer> numberPoliciesPerPerson) {
        List<String> stringList = new ArrayList<>();

        stringList.add("Statistics for " + CURRENT_DATE + "\n");
        stringList.add("Most premium clients:\n" + TITLE_UNDERLINE);
        for (Person person : sortedPersons) {
            stringList.add(person.toString());
        }
        stringList.add("\n");

        stringList.add("Number of policies per client:\n" + TITLE_UNDERLINE);
        numberPoliciesPerPerson.forEach((person, count) -> {
            stringList.add(String.format("%s: %d policies", person.getName().fullName, count));
        });
        stringList.add("\n");

        stringList.add("Average number of policies per client: "
                + String.format("%.2f", getAvgPoliciesPerClient(numberPoliciesPerPerson)));

        stringList.add("Total Commission: " + centsToDollars(totalCommission));
        return stringList;
    }

    private void writeToTxt(List<String> stringList) throws IOException {
        FileWriter fileWriter = new FileWriter(TXT_FILEPATH);
        for (String string : stringList) {
            fileWriter.append(string);
            fileWriter.append("\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }

    private String centsToDollars(int priceInCents) {
        int cents = priceInCents % 100;
        int dollars = (priceInCents - cents) / 100;

        String centsStr;
        if (cents <= 9) {
            centsStr = 0 + "" + cents;
        } else {
            centsStr = Integer.toString(cents);
        }

        return "$" + dollars + "." + centsStr;
    }

}

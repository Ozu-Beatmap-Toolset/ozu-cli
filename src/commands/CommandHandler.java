package commands;

import commands.analyse.AnalyseCommand;
import commands.fix.FixCommand;
import commands.help.HelpCommand;

import java.util.*;
import java.util.function.Consumer;

public class CommandHandler {

    public static final String WRONG_COMMAND_SIZE_TYPE_HELP_MESSAGE = "Missing specifiers. Type command \"ozu help\" to see the list of commands.";
    public static final String INVALID_COMMAND_NAME_TYPE_HELP_MESSAGE = " is not a valid command name. Type \"ozu help\" to see the list of commands.";

    public static final Map<String, Consumer<String[]>> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put("help", HelpCommand::execute);
        COMMANDS.put("fix", FixCommand::execute);
        COMMANDS.put("analyse", AnalyseCommand::execute);
    }

    public static void executeWithProgramArguments(String[] args) {
        if(args.length < 1) {
            throw new IllegalArgumentException(WRONG_COMMAND_SIZE_TYPE_HELP_MESSAGE);
        }

        try {
            COMMANDS.get(args[0]).accept(args);
        }
        catch (NullPointerException exception) {
            throw new IllegalArgumentException("\"" + args[0] + "\"" + INVALID_COMMAND_NAME_TYPE_HELP_MESSAGE);
        }
    }
}

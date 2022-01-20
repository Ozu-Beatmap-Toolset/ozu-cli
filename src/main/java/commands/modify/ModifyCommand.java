package commands.modify;

import commands.CommandHandler;
import commands.modify.bpm.BpmCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ModifyCommand {

    public static final Map<String, Consumer<String[]>> SUB_COMMANDS = new HashMap<>();
    static {
        SUB_COMMANDS.put(BpmCommand.getCommandName(), BpmCommand::execute);
    }

    public static void execute(String[] args) {
        if(args.length < 2) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        CommandHandler.showHelpCommandIfNullOrGet(SUB_COMMANDS.get(args[1])).accept(args);
    }

    public static String getCommandName() {
        return "modify";
    }

    public static String smallDescription() {
        return "Modify elements of a beatmap";
    }

    public static String detailedDescription() {
        return "";
    }

}

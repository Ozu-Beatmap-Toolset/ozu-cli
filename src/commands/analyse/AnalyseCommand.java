package commands.analyse;

import commands.CommandHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AnalyseCommand {

    public static final Map<String, Consumer<String[]>> SUB_COMMANDS = new HashMap<>();
    static {
        SUB_COMMANDS.put("difficulty", null);
        SUB_COMMANDS.put("map-style", null);
    }

    public static void execute(String[] args) {
        if(args.length < 2) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        CommandHandler.showHelpCommandIfNullOrGet(SUB_COMMANDS.get(args[1])).accept(args);
    }

    public static String getCommandName() {
        return "analyse";
    }

    public static String smallDescription() {
        return "Reports information about a beatmap in the console";
    }
}

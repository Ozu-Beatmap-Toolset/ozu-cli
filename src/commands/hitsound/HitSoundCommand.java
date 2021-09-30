package commands.hitsound;

import commands.CommandHandler;
import commands.help.HelpCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HitSoundCommand {

    public static final Map<String, Consumer<String[]>> SUB_COMMANDS = new HashMap<>();
    static {

    }

    public static void execute(String[] args) {
        if(args.length < 2) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        CommandHandler.showHelpCommandIfNullOrGet(SUB_COMMANDS.get(args[1])).accept(args);
    }

    public static String getCommandName() {
        return "hitsound";
    }

    public static String smallDescription() {
        return "Tool to mess with hitsounds";
    }

}

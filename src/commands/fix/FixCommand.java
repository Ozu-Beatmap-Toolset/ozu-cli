package commands.fix;

import commands.CommandHandler;
import commands.fix.note_snap.NoteSnapCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FixCommand {

    public static final Map<String, Consumer<String[]>> SUB_COMMANDS = new HashMap<>();
    static {
        SUB_COMMANDS.put(NoteSnapCommand.getCommandName(), NoteSnapCommand::execute);
        SUB_COMMANDS.put("line-snap", null);
        SUB_COMMANDS.put("bpm-sync", null);
    }

    public static void execute(String[] args) {
        if(args.length < 2) {
            throw new IllegalArgumentException(CommandHandler.WRONG_COMMAND_SIZE_TYPE_HELP_MESSAGE);
        }

        SUB_COMMANDS.get(args[1]).accept(args);
    }

    public static String getCommandName() {
        return "fix";
    }

    public static String smallDescription() {
        return "Looks for specific errors in a beatmap and fixes them automatically";
    }

    public static String detailedDescription() {
        return "";
    }
}

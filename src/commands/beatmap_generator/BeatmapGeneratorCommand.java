package commands.beatmap_generator;

import commands.CommandHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BeatmapGeneratorCommand {

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
        return "beatmap-generator";
    }

    public static String smallDescription() {
        return "Generates beatmaps from soundfiles";
    }
}

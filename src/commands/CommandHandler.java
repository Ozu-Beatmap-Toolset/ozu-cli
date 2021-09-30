package commands;

import commands.analyse.AnalyseCommand;
import commands.beatmap_generator.BeatMapGeneratorCommand;
import commands.fix.FixCommand;
import commands.help.HelpCommand;
import commands.hitsound.HitSoundCommand;

import java.util.*;
import java.util.function.Consumer;

public class CommandHandler {

    public static final String WRONG_COMMAND_SIZE_TYPE_HELP_MESSAGE = "Type command \"ozu help\" to see the list of commands.";

    public static final Map<String, Consumer<String[]>> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put(HelpCommand.getCommandName(), HelpCommand::execute);
        COMMANDS.put(FixCommand.getCommandName(), FixCommand::execute);
        COMMANDS.put(AnalyseCommand.getCommandName(), AnalyseCommand::execute);
        COMMANDS.put(HitSoundCommand.getCommandName(), HitSoundCommand::execute);
        COMMANDS.put(BeatMapGeneratorCommand.getCommandName(), BeatMapGeneratorCommand::execute);
    }

    public static void executeWithProgramArguments(String[] args) {
        if(args.length < 1) {
            userEnteredInvalidCommand(args);
        }

        showHelpCommandIfNullOrGet(COMMANDS.get(args[0])).accept(args);
    }

    public static void userEnteredInvalidCommand(String[] args) {
        HelpCommand.printBriefCommandDescription(args);
    }

    public static Consumer<String[]> showHelpCommandIfNullOrGet(final Consumer<String[]> consumer) {
        if(consumer == null) {
            return strings -> userEnteredInvalidCommand(new String[]{});
        }
        
        return consumer;
    }
}

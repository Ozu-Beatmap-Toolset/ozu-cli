package commands;

import commands.analyse.AnalyseCommand;
import commands.beatmap_generator.BeatmapGeneratorCommand;
import commands.fix.FixCommand;
import commands.help.HelpCommand;
import commands.hitsound.HitSoundCommand;
import commands.modify.ModifyCommand;

import java.util.*;
import java.util.function.Consumer;

public class CommandHandler {

    public static final Map<String, Consumer<String[]>> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put(HelpCommand.getCommandName(), HelpCommand::execute);
        COMMANDS.put(FixCommand.getCommandName(), FixCommand::execute);
        COMMANDS.put(AnalyseCommand.getCommandName(), AnalyseCommand::execute);
        COMMANDS.put(ModifyCommand.getCommandName(), ModifyCommand::execute);
        COMMANDS.put(HitSoundCommand.getCommandName(), HitSoundCommand::execute);
        COMMANDS.put(BeatmapGeneratorCommand.getCommandName(), BeatmapGeneratorCommand::execute);
    }

    public static void executeWithProgramArguments(String[] args) {
        if(args.length < 1) {
            userEnteredInvalidCommand(args);
            return;
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

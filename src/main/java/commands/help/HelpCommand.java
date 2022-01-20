package commands.help;

import app.AppVersion;
import commands.analyse.AnalyseCommand;
import commands.beatmap_generator.BeatmapGeneratorCommand;
import commands.fix.FixCommand;
import commands.hitsound.HitSoundCommand;
import commands.modify.ModifyCommand;

public class HelpCommand {

    public static final String OZU_ASCII_LOGO = "\n" +
            "   ____               Ozu-CLI " + AppVersion.VERSION + "\n" +
            "  / __ \\____  __  __\n" +
            " / / / /_  / / / / /  A mapping toolset program for the Osu! game.\n" +
            "/ /_/ / / /_/ /_/ /\n" +
            "\\____/ /___/\\__,_/    Usage: ozu [command] [options...] [beatmap-folder-location]\n";

    public static final String INTRO_VERSION_INFO = "\nWarning: This program is in active development. Use it at your own risk!\n" +
            "         If you think you found a bug, you can check on the git repo if there are no\n" +
            "         similar issues: https://github.com/Ozu-Beatmap-Toolset/ozu-cli/issues\n" +
            "         If the issue you found is not already present, you can create one.";

    public static final String COMMAND_LIST = "\nAvailable Commands:\n" +
            "  fix                 " + FixCommand.smallDescription() + "\n" +
            "  modify              " + ModifyCommand.smallDescription() + "\n" +
            "  analyze             " + AnalyseCommand.smallDescription() + "\n" +
            "  hitsound            " + HitSoundCommand.smallDescription() + "\n" +
            "  beatmap-generator   " + BeatmapGeneratorCommand.smallDescription() + "\n";

    public static final String DETAILED_COMMAND_MESSAGE = "Type \"ozu help [command]\" for help with a specific command\n";

    public static void execute(String[] args) {
        System.out.println(OZU_ASCII_LOGO);
        System.out.println(INTRO_VERSION_INFO);
        printBriefCommandDescription(args);
    }

    public static void printBriefCommandDescription(String[] args) {
        System.out.println(COMMAND_LIST);
        System.out.println(DETAILED_COMMAND_MESSAGE);
    }

    public static String getCommandName() {
        return "help";
    }
}

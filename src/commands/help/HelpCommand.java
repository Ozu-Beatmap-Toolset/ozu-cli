package commands.help;

import commands.analyse.AnalyseCommand;
import commands.beatmap_generator.BeatMapGeneratorCommand;
import commands.fix.FixCommand;
import commands.hitsound.HitSoundCommand;

public class HelpCommand {

    public static final String OZU_ASCII_LOGO = "\n" +
            "   ____               Ozu-CLI [ALPHA] V0.0.1\n" +
            "  / __ \\____  __  __\n" +
            " / / / /_  / / / / /  A mapping toolset program for the Osu! game.\n" +
            "/ /_/ / / /_/ /_/ /\n" +
            "\\____/ /___/\\__,_/    Usage: ozu [command] [options...] [beatmap-folder-location]\n";

    public static final String INTRO_VERSION_INFO = "\nWarning: This program is in active development. Use it at your own risk!\n" +
            "         If you think you have found a bug, please report it at my email address: lebel.johnw@gmail.com\n";

    public static final String COMMAND_LIST = "\nAvailable Commands:\n\n" +
            "  fix                 " + FixCommand.smallDescription() + "\n" +
            "  analyze             " + AnalyseCommand.smallDescription() + "\n" +
            "  hitsound            " + HitSoundCommand.smallDescription() + "\n" +
            "  beatmap-generator   " + BeatMapGeneratorCommand.smallDescription() + "\n";

    public static final String DETAILED_COMMAND_MESSAGE = "Use \"ozu help [command]\" for help with a specific command\n\n";

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

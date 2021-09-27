package commands.help;

import commands.analyse.AnalyseCommand;
import commands.beatmap_generator.BeatMapGeneratorCommand;
import commands.fix.FixCommand;
import commands.hitsound.HitSoundCommand;

public class HelpCommand {

    public static final String OZU_ASCII_LOGO = "\n" +
            "   ____               Ozu-CLI\n" +
            "  / __ \\____  __  __\n" +
            " / / / /_  / / / / /  The mapping toolset program for the Osu! game.\n" +
            "/ /_/ / / /_/ /_/ /\n" +
            "\\____/ /___/\\__,_/    Usage: ozu [command] [options...] [beatmap-folder-location]\n";

    public static final String COMMAND_LIST = "\nAvailable Commands:\n\n" +
            "  fix                 " + FixCommand.smallDescription() + "\n" +
            "  analyze             " + AnalyseCommand.smallDescription() + "\n" +
            "  hitsound            " + HitSoundCommand.smallDescription() + "\n" +
            "  beatmap-generator   " + BeatMapGeneratorCommand.smallDescription() + "\n";

    public static final String DETAILED_COMMAND_MESSAGE = "Use \"ozu [command] help\" for help with a specific command\n\n";

    public static void execute(String[] args) {
        System.out.println(OZU_ASCII_LOGO);
        System.out.println(COMMAND_LIST);
        System.out.println(DETAILED_COMMAND_MESSAGE);
    }
}

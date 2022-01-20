package commands.fix.green_line_snap;

import commands.CommandHandler;
import global_parameters.GlobalCliParameters;
import global_parameters.GlobalParametersBuilder;
import osu.beatmap.Beatmap;
import osu.beatmap.serialization.BeatmapParser;
import tools.beatmap_exporter.BeatmapExporter;
import tools.timing_snapper.GreenLineSnapper;
import util.file.IOFile;

import java.util.Optional;

public class GreenLineSnapCommand {

    public static void execute(String[] args) {
        if(args.length < 3) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        final GlobalCliParameters globalCliParameters = GlobalParametersBuilder.build(args);

        final Optional<Beatmap> beatmapOpt = BeatmapParser.decode(IOFile.fileToInputStream(globalCliParameters.getBeatmapFile()).get());
        beatmapOpt.ifPresent(beatmap -> {
            GreenLineSnapper.execute(beatmap, globalCliParameters);
            BeatmapExporter.export(beatmap, globalCliParameters);
        });
    }

    public static String getCommandName() {
        return "green-line-snap";
    }
}
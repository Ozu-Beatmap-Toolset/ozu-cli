package commands.fix.note_snap;

import commands.CommandHandler;
import global_parameters.GlobalCliParameters;
import global_parameters.GlobalParametersBuilder;
import osu.beatmap.Beatmap;
import osu.beatmap.serialization.BeatmapParser;
import tools.beatmap_exporter.BeatmapExporter;
import tools.timing_snapper.NoteSnapper;

import java.io.File;
import java.util.*;

public class NoteSnapCommand {

    public static void execute(String[] args) {
        if(args.length < 3) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        final GlobalCliParameters globalCliParameters = GlobalParametersBuilder.build(args);

        final File beatmapFile = new File(args[args.length - 1]);
        final Optional<Beatmap> beatmapOpt = BeatmapParser.decode(beatmapFile);
        beatmapOpt.ifPresent(beatmap -> {
            NoteSnapper.execute(beatmap, globalCliParameters);
            BeatmapExporter.export(beatmap, beatmapFile, globalCliParameters);
        });
    }

    public static String getCommandName() {
        return "note-snap";
    }
}
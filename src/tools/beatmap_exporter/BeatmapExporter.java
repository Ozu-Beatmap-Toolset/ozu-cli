package tools.beatmap_exporter;

import commands.CliArgumentFinder;
import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;
import osu.beatmap.serialization.BeatmapParser;

import java.io.File;
import java.util.function.Supplier;

public class BeatmapExporter {

    public static void export(final Beatmap beatmap, final File beatmapFile, final GlobalCliParameters globalParameters) {
        final Supplier<File> osuFileLocation = () -> {
            if(globalParameters.isExportingInPlace()) {
                return new File(beatmapFile.getAbsolutePath() + "_" + System.currentTimeMillis() + ".osu");
            }
            return beatmapFile;
        };
        BeatmapParser.encode(beatmap, osuFileLocation.get());
    }
}

package tools.beatmap_exporter;

import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;
import osu.beatmap.serialization.BeatmapParser;

import java.io.File;
import java.util.function.Supplier;

public class BeatmapExporter {

    public static void export(final Beatmap beatmap, final GlobalCliParameters globalParameters) {
        final Supplier<File> osuFileLocation = () -> {
            if(globalParameters.isExportingInPlace()) {
                return globalParameters.getBeatmapFile();
            }
            final StringBuilder stringBuilder = new StringBuilder();
            final File beatmapFile = globalParameters.getBeatmapFile();

            stringBuilder.append(beatmapFile.getAbsolutePath());
            stringBuilder.append("_");
            stringBuilder.append(System.currentTimeMillis());
            stringBuilder.append(".osu");

            return new File(stringBuilder.toString());
        };

        BeatmapParser.encode(beatmap, osuFileLocation.get());
    }
}

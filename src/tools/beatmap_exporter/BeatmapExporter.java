package tools.beatmap_exporter;

import commands.CliArgumentFinder;
import osu.beatmap.Beatmap;
import osu.beatmap.serialization.BeatmapParser;

import java.io.File;
import java.util.function.Supplier;

public class BeatmapExporter {

    public static void export(final Beatmap beatmap, final File beatmapFile, final String[] args) {
        final CliArgumentFinder argumentFinder = new CliArgumentFinder(args);
        final Supplier<File> osuFileLocation = () -> {
            final String newFileName = beatmapFile.getAbsolutePath() + "_" + System.currentTimeMillis() + ".osu";
            return new File(newFileName);
        };
        BeatmapParser.encode(beatmap,
                argumentFinder.findArguments("-i", 0)
                        .map(emptyList -> beatmapFile)
                        .orElse(osuFileLocation.get()));
    }
}

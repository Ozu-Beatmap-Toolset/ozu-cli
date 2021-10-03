package commands.fix.bpm;

import commands.CommandHandler;
import global_parameters.GlobalCliParameters;
import global_parameters.GlobalParametersBuilder;
import osu.beatmap.Beatmap;
import osu.beatmap.serialization.BeatmapParser;
import tools.beatmap_exporter.BeatmapExporter;
import tools.bpm_offset_finder.BPMOFinder;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;


public class BpmCommand {

    public static void execute(String[] args) {
        if(args.length < 3) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        final GlobalCliParameters globalParameters = GlobalParametersBuilder.build(args);

        final File beatmapFile = globalParameters.getBeatmapFile();
        final Optional<Beatmap> beatmapOpt = BeatmapParser.decode(beatmapFile);
        beatmapOpt.ifPresent(beatmap -> {
            final File beatmapFolder = beatmapFile.getParentFile();
            final String[] ls = beatmapFolder.list();
            final Optional<String> audioFilenameOpt = Arrays.stream(ls)
                    .filter(s -> s.equals(beatmap.general.audioFileName))
                    .findFirst();
            audioFilenameOpt.ifPresent(audioFilename -> {
                try {
                    BPMOFinder.execute(beatmap, new File(beatmapFolder.getAbsolutePath() + "\\" + audioFilename));
                    BeatmapExporter.export(beatmap, globalParameters);
                }
                catch(Exception ioException) {
                    ioException.printStackTrace();
                }
            });
        });
    }

    public static String getCommandName() {
        return "bpm";
    }
}

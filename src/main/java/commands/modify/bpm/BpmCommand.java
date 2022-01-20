package commands.modify.bpm;

import commands.CliArgumentFinder;
import commands.CommandHandler;
import global_parameters.GlobalCliParameters;
import global_parameters.GlobalParametersBuilder;
import os.FfmpegAutoInstaller;
import osu.beatmap.Beatmap;
import osu.beatmap.operations.TimingPointOperations;
import osu.beatmap.serialization.BeatmapParser;
import tools.beatmap_exporter.BeatmapExporter;
import tools.beatmap_speed_changer.BeatmapSpeedChanger;
import util.file.IOFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class BpmCommand {

    public static void execute(String[] args) {
        if(args.length < 4) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        try {
            FfmpegAutoInstaller.applyStrictInstallationProcedure();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        final GlobalCliParameters globalParameters = GlobalParametersBuilder.build(args);

        final CliArgumentFinder argumentFinder = new CliArgumentFinder(args);
        final List<String> unparsedBpmArgument = argumentFinder.findArguments("bpm", 1).get();
        final double bpmParameter = Double.parseDouble(unparsedBpmArgument.get(0));

        final File beatmapFile = globalParameters.getBeatmapFile();
        final Optional<Beatmap> beatmapOpt = BeatmapParser.decode(IOFile.fileToInputStream(beatmapFile).get());
        beatmapOpt.ifPresent(beatmap -> {
            final File beatmapFolder = beatmapFile.getParentFile();
            final String[] ls = beatmapFolder.list();
            final Optional<String> audioFilenameOpt = Arrays.stream(ls)
                    .filter(s -> s.equals(beatmap.general.audioFileName))
                    .findFirst();
            audioFilenameOpt.ifPresent(audioFilename -> {
                try {
                    final double currentBpm = 60000.0/TimingPointOperations.findBeatLengthAt(
                            beatmap.timingPoints.redLineData,
                            0).get();
                    final double speedMultiplier = bpmParameter / currentBpm;
                    final File audioFile = new File(beatmapFolder.getAbsolutePath() + "\\" + audioFilename);
                    BeatmapSpeedChanger.execute(beatmap, audioFile, speedMultiplier);
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

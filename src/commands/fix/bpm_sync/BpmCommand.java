package commands.fix.bpm_sync;

import commands.CommandHandler;
import osu.beatmap.BeatMap;
import osu.beatmap.serialization.BeatMapParser;
import tools.bpm_offset_finder.BPMOFinder;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


public class BpmCommand {

    public static void execute(String[] args) {
        if(args.length < 3) {
        CommandHandler.userEnteredInvalidCommand(args);
        return;
        }

        final File beatmapFile = new File(args[args.length - 1]);
        final Optional<BeatMap> beatMapOpt = BeatMapParser.decode(beatmapFile);
        beatMapOpt.ifPresent(beatmap -> {
            final File beatmapFolder = beatmapFile.getParentFile();
            final String[] ls = beatmapFolder.list();
            final Optional<String> audioFilenameOpt = Arrays.stream(ls)
                    .filter(s -> s.equals(beatmap.general.audioFileName))
                    .findFirst();
            audioFilenameOpt.ifPresent(audioFilename -> {
                try {
                    BPMOFinder.execute(beatmap, new File(beatmapFolder.getAbsolutePath() + "\\" + audioFilename));
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            });
        });
    }


    public static String getCommandName() {
        return "bpm-sync";
    }
}

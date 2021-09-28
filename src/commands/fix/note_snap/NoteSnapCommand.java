package commands.fix.note_snap;

import commands.CliArgumentFinder;
import commands.CommandHandler;
import osu.beatmap.BeatMap;
import osu.beatmap.serialization.BeatMapParser;
import tools.timing_snapper.NoteSnapper;
import util.data_structure.tupple.Tuple2;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class NoteSnapCommand {

    public static void execute(String[] args) {
        if(args.length < 3) {
            CommandHandler.userEnteredInvalidCommand(args);
            return;
        }

        final CliArgumentFinder argumentFinder = new CliArgumentFinder(args);
        final List<Integer> enabledDivisions = new ArrayList<>();

        final Tuple2<Integer, Integer> workInterval = new Tuple2<>(Integer.MIN_VALUE, Integer.MAX_VALUE);

        IntStream.range(1, 17).forEach(i -> argumentFinder
                .findArguments("-d" + i, 0)
                .ifPresent(emptyList -> enabledDivisions.add(i)));

        argumentFinder.findArguments("-t", 2)
                .ifPresent(timeIntervalArgs -> {
                    workInterval.value1 = Integer.parseInt(timeIntervalArgs.get(0));
                    workInterval.value2 = Integer.parseInt(timeIntervalArgs.get(1));
                });

        final File beatMapFile = new File(args[args.length - 1]);
        final Optional<BeatMap> beatMapOpt = BeatMapParser.decode(beatMapFile);
        beatMapOpt.ifPresent(beatMap -> {
            NoteSnapper.execute(beatMap, enabledDivisions, workInterval);
            BeatMapParser.encode(beatMap, beatMapFile);
        });
    }

    public static String getCommandName() {
        return "note-snap";
    }
}

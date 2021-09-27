package commands.fix.note_snap;

import commands.CliArgumentFinder;
import commands.CommandHandler;
import osu.beatmap.BeatMap;
import osu.beatmap.parser.Parser;
import tools.timing_snapper.NoteSnapper;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class NoteSnapCommand {

    public static void execute(String[] args) {
        if(args.length < 3) {
            throw new IllegalArgumentException(CommandHandler.WRONG_COMMAND_SIZE_TYPE_HELP_MESSAGE);
        }

        final CliArgumentFinder argumentFinder = new CliArgumentFinder(args);
        final List<Integer> enabledDivisions = new ArrayList<>();

        IntStream.range(1, 17).forEach(i -> argumentFinder
                .findArguments("d" + i, 0)
                .ifPresent(emptyList -> enabledDivisions.add(i)));

        System.out.println("Divisions considered for the note-snapping:");
        System.out.println(enabledDivisions);

        final File beatMapFile = new File(args[args.length - 1]);
        final Optional<BeatMap> beatMapOpt = Parser.decode(beatMapFile);
        beatMapOpt.ifPresent(beatMap -> NoteSnapper.execute(beatMap, enabledDivisions));
    }
}

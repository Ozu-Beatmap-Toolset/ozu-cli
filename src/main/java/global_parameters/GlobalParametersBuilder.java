package global_parameters;

import commands.CliArgumentFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class GlobalParametersBuilder {

    private static final String EXPORT_IN_PLACE_PARAMETER_NAME = "-i";
    private static final String TIME_INTERVAL_PARAMETER_NAME = "-t";

    public static GlobalCliParameters build(final String[] args) {
        final CliArgumentFinder argumentFinder = new CliArgumentFinder(args);

        final Optional<ExportInPlace> exportInPlaceParameter = argumentFinder.findArguments(EXPORT_IN_PLACE_PARAMETER_NAME, 0)
                .map(emptyList -> new ExportInPlace());

        final Optional<TimeInterval> timeIntervalParameter = argumentFinder.findArguments(TIME_INTERVAL_PARAMETER_NAME, 2)
                .map(listOfTimeStamps -> {
                    final int min = Integer.parseInt(listOfTimeStamps.get(0));
                    final int max = Integer.parseInt(listOfTimeStamps.get(1));
                    return new TimeInterval(min, max);
                });

        final List<Integer> beatDivisorList = new ArrayList<>();
        IntStream.range(1, 17).forEach(i -> argumentFinder
                .findArguments("-d" + i, 0)
                .ifPresent(emptyList -> beatDivisorList.add(i)));
        final Optional<BeatDivisors> beatDivisorsParameter = Optional.of(new BeatDivisors(beatDivisorList));

        final Optional<File> beatmapFile = Optional.of(new File(args[args.length - 1]));

        return new GlobalCliParameters(exportInPlaceParameter, timeIntervalParameter, beatDivisorsParameter, beatmapFile);
    }
}

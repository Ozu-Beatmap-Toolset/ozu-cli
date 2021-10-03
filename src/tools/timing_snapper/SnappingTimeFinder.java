package tools.timing_snapper;

import osu.beatmap.Beatmap;
import osu.beatmap.operations.TimingPointOperations;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class SnappingTimeFinder {

    public static Optional<Integer> closestSnappedTime(final Beatmap beatmap,
                                                        final List<Integer> enabledTimeDivisions,
                                                        final int time) {
        return enabledTimeDivisions.stream()
                .map(timeDivision -> quantizedTime(beatmap, timeDivision, time))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(quantizedTimeComparision(time));
    }

    // snaps the "time" variable to the closest valid time division, if there are any
    // (osu files are actually invalid if they contain no red lines)
    private static Optional<Integer> quantizedTime(final Beatmap beatmap, final int division, final int time) {
        final Optional<Double> beatLengthOpt = TimingPointOperations.findBeatLengthAt(beatmap.timingPoints.redLineData, time);
        final Optional<Integer> offsetOpt = TimingPointOperations.findTimingOffsetAt(beatmap.timingPoints.redLineData, time);

        return beatLengthOpt.map(beatLength -> offsetOpt.map(offset -> {
            final double smallestAllowedBeatLength = beatLength/division;
            final int timeDifference = time - offset;
            final int quantizedReducedTime = (int) ((timeDifference / smallestAllowedBeatLength) + singOf(timeDifference)*0.5);
            return (int)(quantizedReducedTime * smallestAllowedBeatLength) + offset;
        })).flatMap(integerOpt -> integerOpt);
    }

    private static BinaryOperator<Integer> quantizedTimeComparision(final int time) {
        return (time1, time2) -> Math.abs(time1 - time) < Math.abs(time2 - time) ? time1 : time2;
    }

    private static double singOf(int x) {
        return x > 0 ? 1 : -1;
    }
}

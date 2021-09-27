package tools.timing_snapper;

import osu.beatmap.BeatMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class NoteSnapper {

    public static void execute(BeatMap beatMap, List<Integer> enabledTimeDivisions) {
        beatMap.hitObjects.hitCircleData.forEach(circle -> {
            closestSnappedTime(beatMap, enabledTimeDivisions, circle.time)
                    .ifPresent(quantizedTime -> circle.time = quantizedTime);
        });
        beatMap.hitObjects.hitSliderData.forEach(slider -> {
            closestSnappedTime(beatMap, enabledTimeDivisions, slider.time)
                    .ifPresent(quantizedTime -> slider.time = quantizedTime);
            // TODO
            // fix slider tails too
        });
        beatMap.hitObjects.hitSpinnerData.forEach(spinner -> {
            closestSnappedTime(beatMap, enabledTimeDivisions, spinner.time)
                    .ifPresent(quantizedTime -> spinner.time = quantizedTime);
            closestSnappedTime(beatMap, enabledTimeDivisions, spinner.endTime)
                    .ifPresent(quantizedTime -> spinner.endTime = quantizedTime);
        });
    }

    private static Optional<Integer> closestSnappedTime(BeatMap beatMap, List<Integer> enabledTimeDivisions, int time) {
        return enabledTimeDivisions.stream()
                .map(timeDivision -> quantizedTime(beatMap, timeDivision, time))
                .reduce(quantizedTimeComparision(time));
    }

    // snaps the "time" variable to the closest valid time division
    private static int quantizedTime(BeatMap beatMap, int division, int time) {
        //final double beatLength = beatMap.findBeatLengthAt(time);
        //final double smallestAllowedBeatLength = beatLength/division;
        //final int offset = beatMap.findTimingOffsetAt(time);

        //final int quantizedReducedTime = (int) (((time - offset) / smallestAllowedBeatLength) + 0.5);
        //return (int)(quantizedReducedTime * smallestAllowedBeatLength) + offset;
        return 0;
    }

    private static BinaryOperator<Integer> quantizedTimeComparision(final int time) {
        return (time1, time2) -> Math.abs(time1 - time) < Math.abs(time2 - time) ? time1 : time2;
    }
}

package tools.timing_snapper;

import osu.beatmap.BeatMap;
import osu.beatmap.hit_objects.slider.HitSliderData;
import util.data_structure.tupple.Tuple2;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class NoteSnapper {

    public static void execute(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        noteSnapCircles(beatMap, enabledTimeDivisions, workInterval);
        noteSnapSliders(beatMap, enabledTimeDivisions, workInterval);
        noteSnapSpinners(beatMap, enabledTimeDivisions, workInterval);
    }

    private static void noteSnapSpinners(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        beatMap.hitObjects.hitSpinnerData.stream()
                .filter(spinner -> spinner.time > workInterval.value1 && spinner.time < workInterval.value2)
                .forEach(spinner -> closestSnappedTime(beatMap, enabledTimeDivisions, spinner.time)
                        .ifPresent(quantizedTime -> spinner.time = quantizedTime));
        beatMap.hitObjects.hitSpinnerData.stream()
                .filter(spinner -> spinner.endTime > workInterval.value1 && spinner.endTime < workInterval.value2)
                .forEach(spinner -> closestSnappedTime(beatMap, enabledTimeDivisions, spinner.endTime)
                        .ifPresent(quantizedTime -> spinner.endTime = quantizedTime));
    }

    private static void noteSnapSliders(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        beatMap.hitObjects.hitSliderData.stream()
                .filter(slider -> slider.time > workInterval.value1 && slider.time < workInterval.value2)
                .forEach(slider -> closestSnappedTime(beatMap, enabledTimeDivisions, slider.time)
                        .ifPresent(quantizedTime -> slider.time = quantizedTime));
        beatMap.hitObjects.hitSliderData.stream()
                .filter(slider -> slider.time > workInterval.value1 && slider.time < workInterval.value2)
                .forEach(slider -> {
                    final double sliderVelocity = beatMap.findSliderVelocityAt(slider.time);
                    final double endTime = slider.time + slider.length/sliderVelocity;
                    closestSnappedTime(beatMap, enabledTimeDivisions, (int)endTime)
                            .ifPresent(quantizedTime -> slider.length = (quantizedTime - slider.time) * sliderVelocity);
                });
    }

    private static void noteSnapCircles(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        beatMap.hitObjects.hitCircleData.stream()
                .filter(circle -> circle.time > workInterval.value1 && circle.time < workInterval.value2)
                .forEach(circle -> closestSnappedTime(beatMap, enabledTimeDivisions, circle.time)
                        .ifPresent(quantizedTime -> circle.time = quantizedTime));
    }

    private static Optional<Integer> closestSnappedTime(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final int time) {
        return enabledTimeDivisions.stream()
                .map(timeDivision -> quantizedTime(beatMap, timeDivision, time))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(quantizedTimeComparision(time));
    }

    // snaps the "time" variable to the closest valid time division, if there are any
    // (osu files are actually invalid if they contain no red lines)
    private static Optional<Integer> quantizedTime(final BeatMap beatMap, final int division, final int time) {
        final Optional<Double> beatLengthOpt = beatMap.findBeatLengthAt(time);
        final Optional<Integer> offsetOpt = beatMap.findTimingOffsetAt(time);

        return beatLengthOpt.map(beatLength -> offsetOpt.map(offset -> {
            final double smallestAllowedBeatLength = beatLength/division;
            final int timeDifference = time - offset;
            final int quantizedReducedTime = (int) (((timeDifference) / smallestAllowedBeatLength) + singOf(timeDifference)*0.5);
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

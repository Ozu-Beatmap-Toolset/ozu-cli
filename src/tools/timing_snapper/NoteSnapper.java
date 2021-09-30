package tools.timing_snapper;

import osu.beatmap.BeatMap;
import util.data_structure.tupple.Tuple2;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;

public class NoteSnapper {

    public static void execute(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        final int amountOfFixedCircles = noteSnapCircles(beatMap, enabledTimeDivisions, workInterval);
        final Tuple2<Integer, Integer> amountsOfFixedSliders = noteSnapSliders(beatMap, enabledTimeDivisions, workInterval);
        final Tuple2<Integer, Integer> amountsOfFixedSpinners = noteSnapSpinners(beatMap, enabledTimeDivisions, workInterval);

        displayAmountsOfFixedObject(amountOfFixedCircles, amountsOfFixedSliders, amountsOfFixedSpinners);
    }

    private static int noteSnapCircles(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        final AtomicInteger fixedCircleCounter = new AtomicInteger(0);

        beatMap.hitObjects.hitCircleData.stream()
                .filter(circle -> circle.time > workInterval.value1 && circle.time < workInterval.value2)
                .forEach(circle -> closestSnappedTime(beatMap, enabledTimeDivisions, circle.time)
                        .ifPresent(quantizedTime -> {
                            final int previousCircleTime = circle.time;
                            circle.time = quantizedTime;
                            if(previousCircleTime != circle.time) fixedCircleCounter.incrementAndGet();
                        }));

        return fixedCircleCounter.get();
    }

    private static Tuple2<Integer, Integer> noteSnapSliders(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        final AtomicInteger fixedSliderHeadCounter = new AtomicInteger(0);
        final AtomicInteger fixedSliderTailCounter = new AtomicInteger(0);

        beatMap.hitObjects.hitSliderData.stream()
                .filter(slider -> slider.time > workInterval.value1 && slider.time < workInterval.value2)
                .forEach(slider -> closestSnappedTime(beatMap, enabledTimeDivisions, slider.time)
                        .ifPresent(quantizedTime -> {
                            final int previousSliderTime = slider.time;
                            slider.time = quantizedTime;
                            if(previousSliderTime != slider.time) fixedSliderHeadCounter.incrementAndGet();
                        }));
        beatMap.hitObjects.hitSliderData.stream()
                .filter(slider -> slider.time > workInterval.value1 && slider.time < workInterval.value2)
                .forEach(slider -> {
                    final double sliderVelocity = beatMap.findSliderVelocityAt(slider.time);
                    final double endTime = slider.time + slider.length/sliderVelocity;
                    closestSnappedTime(beatMap, enabledTimeDivisions, (int)endTime)
                            .ifPresent(quantizedTime -> {
                                final double previousSliderLength = slider.length;
                                slider.length = (quantizedTime - slider.time) * sliderVelocity;
                                if(previousSliderLength != slider.length) fixedSliderTailCounter.incrementAndGet();
                            });
                });

        return new Tuple2<>(fixedSliderHeadCounter.get(), fixedSliderTailCounter.get());
    }

    private static Tuple2<Integer, Integer> noteSnapSpinners(final BeatMap beatMap, final List<Integer> enabledTimeDivisions, final Tuple2<Integer, Integer> workInterval) {
        final AtomicInteger fixedSpinnerHeadCounter = new AtomicInteger(0);
        final AtomicInteger fixedSpinnerTailCounter = new AtomicInteger(0);

        beatMap.hitObjects.hitSpinnerData.stream()
                .filter(spinner -> spinner.time > workInterval.value1 && spinner.time < workInterval.value2)
                .forEach(spinner -> closestSnappedTime(beatMap, enabledTimeDivisions, spinner.time)
                        .ifPresent(quantizedTime -> {
                            final double previousSpinnerTime = spinner.time;
                            spinner.time = quantizedTime;
                            if(previousSpinnerTime != spinner.time) fixedSpinnerHeadCounter.incrementAndGet();
                        }));
        beatMap.hitObjects.hitSpinnerData.stream()
                .filter(spinner -> spinner.endTime > workInterval.value1 && spinner.endTime < workInterval.value2)
                .forEach(spinner -> closestSnappedTime(beatMap, enabledTimeDivisions, spinner.endTime)
                        .ifPresent(quantizedTime -> {
                            final double previousSpinnerEndTime = spinner.endTime;
                            spinner.endTime = quantizedTime;
                            if(previousSpinnerEndTime != spinner.endTime) fixedSpinnerTailCounter.incrementAndGet();
                        }));

        return new Tuple2<>(fixedSpinnerHeadCounter.get(), fixedSpinnerTailCounter.get());
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

    private static void displayAmountsOfFixedObject(int amountOfFixedCircles, Tuple2<Integer, Integer> amountsOfFixedSliders, Tuple2<Integer, Integer> amountsOfFixedSpinners) {
        System.out.println("Snapped:");
        System.out.println("  " + amountOfFixedCircles + " circle" + (amountOfFixedCircles > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSliders.value1 + " slider head" + (amountsOfFixedSliders.value1 > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSliders.value2 + " slider tail" + (amountsOfFixedSliders.value2 > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSpinners.value1 + " spinner head" + (amountsOfFixedSpinners.value1 > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSpinners.value2 + " spinner tail" + (amountsOfFixedSpinners.value2 > 1 ? "s" : ""));
    }
}

package tools.timing_snapper;

import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;
import util.data_structure.tupple.Tuple2;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinnerSnapper {

    private Tuple2<Integer, Integer> amountOfSnapsDone;

    public SpinnerSnapper() {
        amountOfSnapsDone = new Tuple2<>(0, 0);
    }

    public void snap(final Beatmap beatmap,
                     final GlobalCliParameters globalParameters) {
        final AtomicInteger fixedSpinnerHeadCounter = new AtomicInteger(0);
        final AtomicInteger fixedSpinnerTailCounter = new AtomicInteger(0);

        beatmap.hitObjects.hitSpinnerData.stream()
                .filter(spinner -> globalParameters.timeIntervalContains(spinner.time))
                .forEach(spinner -> SnappingTimeFinder.closestSnappedTime(beatmap, globalParameters.getBeatDivisors(), spinner.time)
                        .ifPresent(quantizedTime -> {
                            final double previousSpinnerTime = spinner.time;
                            spinner.time = quantizedTime;
                            if(previousSpinnerTime != spinner.time) fixedSpinnerHeadCounter.incrementAndGet();
                        }));
        beatmap.hitObjects.hitSpinnerData.stream()
                .filter(spinner -> globalParameters.timeIntervalContains(spinner.endTime))
                .forEach(spinner -> SnappingTimeFinder.closestSnappedTime(beatmap, globalParameters.getBeatDivisors(), spinner.endTime)
                        .ifPresent(quantizedTime -> {
                            final double previousSpinnerEndTime = spinner.endTime;
                            spinner.endTime = quantizedTime;
                            if(previousSpinnerEndTime != spinner.endTime) fixedSpinnerTailCounter.incrementAndGet();
                        }));

        amountOfSnapsDone = new Tuple2<>(fixedSpinnerHeadCounter.get(), fixedSpinnerTailCounter.get());
    }

    public Tuple2<Integer, Integer> getAmountOfSnapsDone() {
        return amountOfSnapsDone;
    }
}

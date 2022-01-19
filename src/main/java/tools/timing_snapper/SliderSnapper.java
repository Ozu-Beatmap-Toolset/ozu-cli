package tools.timing_snapper;

import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;
import util.data_structure.tupple.Tuple2;

import java.util.concurrent.atomic.AtomicInteger;

public class SliderSnapper {

    private Tuple2<Integer, Integer> amountOfSnapsDone;

    public SliderSnapper() {
        amountOfSnapsDone = new Tuple2<>(0, 0);
    }

    public void snap(final Beatmap beatmap,
                     final GlobalCliParameters globalParameters) {
        final AtomicInteger fixedSliderHeadCounter = new AtomicInteger(0);
        final AtomicInteger fixedSliderTailCounter = new AtomicInteger(0);

        snapHead(beatmap, globalParameters, fixedSliderHeadCounter);
        snapTail(beatmap, globalParameters, fixedSliderTailCounter);

        amountOfSnapsDone = new Tuple2<>(fixedSliderHeadCounter.get(), fixedSliderTailCounter.get());
    }

    private void snapHead(Beatmap beatmap, GlobalCliParameters globalParameters, AtomicInteger fixedSliderHeadCounter) {
        beatmap.hitObjects.hitSliderData.stream()
                .filter(slider -> globalParameters.timeIntervalContains(slider.time))
                .forEach(slider -> SnappingTimeFinder.closestSnappedTime(beatmap, globalParameters.getBeatDivisors(), slider.time)
                        .ifPresent(quantizedTime -> {
                            final int previousSliderTime = slider.time;
                            slider.time = quantizedTime;
                            if(previousSliderTime != slider.time) fixedSliderHeadCounter.incrementAndGet();
                        }));
    }

    private void snapTail(Beatmap beatmap, GlobalCliParameters globalParameters, AtomicInteger fixedSliderTailCounter) {
        beatmap.hitObjects.hitSliderData.stream()
                .filter(slider -> globalParameters.timeIntervalContains(slider.time))
                .forEach(slider -> {
                    final double endTime = slider.getEndTime(beatmap.timingPoints, beatmap.difficulty);
                    final double previousSliderLength = slider.length;
                    final int endTimeInt = (int)(endTime + 0.5);
                    SnappingTimeFinder.closestSnappedTime(beatmap, globalParameters.getBeatDivisors(), endTimeInt)
                            .ifPresent(quantizedTime -> {
                                slider.setEndTime(beatmap.timingPoints, beatmap.difficulty, (double)quantizedTime);
                                if(previousSliderLength != slider.length) fixedSliderTailCounter.incrementAndGet();
                            });
                });
    }

    public Tuple2<Integer, Integer> getAmountOfSnapsDone() {
        return amountOfSnapsDone;
    }
}

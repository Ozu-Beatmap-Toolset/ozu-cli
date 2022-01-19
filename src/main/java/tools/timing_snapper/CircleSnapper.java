package tools.timing_snapper;

import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;

import java.util.concurrent.atomic.AtomicInteger;

public class CircleSnapper {

    private int amountOfSnapsDone;

    public CircleSnapper() {
        this.amountOfSnapsDone = 0;
    }

    public void snap(final Beatmap beatmap,
                     final GlobalCliParameters globalParameters) {
        final AtomicInteger fixedCircleCounter = new AtomicInteger(0);

        beatmap.hitObjects.hitCircleData.stream()
                .filter(circle -> globalParameters.timeIntervalContains(circle.time))
                .forEach(circle -> SnappingTimeFinder.closestSnappedTime(beatmap, globalParameters.getBeatDivisors(), circle.time)
                        .ifPresent(quantizedTime -> {
                            final int previousCircleTime = circle.time;
                            circle.time = quantizedTime;
                            if(previousCircleTime != circle.time) fixedCircleCounter.incrementAndGet();
                        }));

        amountOfSnapsDone = fixedCircleCounter.get();
    }

    public int getAmountOfSnapsDone() {
        return amountOfSnapsDone;
    }
}

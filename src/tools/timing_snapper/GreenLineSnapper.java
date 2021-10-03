package tools.timing_snapper;

import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;
import java.util.concurrent.atomic.AtomicInteger;

public class GreenLineSnapper {

    public static void execute(final Beatmap beatmap, final GlobalCliParameters globalParameters) {
        final int amountOfFixedLines = snapGreenLines(beatmap, globalParameters);
        displayAmountsOfFixedLines(amountOfFixedLines);
    }

    private static int snapGreenLines(final Beatmap beatmap, final GlobalCliParameters globalParameters) {
        final AtomicInteger fixedGreenLineCounter = new AtomicInteger(0);

        beatmap.timingPoints.greenLineData.stream()
                .filter(greenLine -> globalParameters.timeIntervalContains(greenLine.time))
                .forEach(greenLine -> SnappingTimeFinder.closestSnappedTime(beatmap, globalParameters.getBeatDivisors(), greenLine.time)
                        .ifPresent(quantizedTime -> {
                            final int previousGreenLineTime = greenLine.time;
                            greenLine.time = quantizedTime;
                            if(previousGreenLineTime != greenLine.time) fixedGreenLineCounter.incrementAndGet();
                        }));

        return fixedGreenLineCounter.get();
    }

    private static void displayAmountsOfFixedLines(int amountOfFixedCircles) {
        System.out.println("Snapped:");
        System.out.println("  " + amountOfFixedCircles + " inherited line" + (amountOfFixedCircles > 1 ? "s" : ""));
    }
}

package tools.beatmap_speed_changer;

import osu.beatmap.serialization.ParsedTimingPoints;

public class TimingPointTimelineScaler {

    public static void scale(final ParsedTimingPoints timingPoints, final double speedMultiplier) {
        timingPoints.redLineData.forEach(redLineData ->
                redLineData.time = (int)(redLineData.time / speedMultiplier));
        timingPoints.greenLineData.forEach(redLineData ->
                redLineData.time = (int)(redLineData.time / speedMultiplier));
        timingPoints.redLineData.forEach(redLineData ->
                redLineData.beatLength = redLineData.beatLength / speedMultiplier);
    }
}

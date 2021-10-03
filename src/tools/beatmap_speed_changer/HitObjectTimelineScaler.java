package tools.beatmap_speed_changer;

import osu.beatmap.serialization.ParsedHitObjects;

public class HitObjectTimelineScaler {

    public static void scale(final ParsedHitObjects hitObjects, final double speedMultiplier) {
        hitObjects.hitCircleData.forEach(circleData ->
                circleData.time = (int)(circleData.time / speedMultiplier));
        hitObjects.hitSliderData.forEach(sliderData ->
                sliderData.time = (int)(sliderData.time / speedMultiplier));
        hitObjects.hitSpinnerData.forEach(spinnerData -> {
            spinnerData.time =    (int)(spinnerData.time    / speedMultiplier);
            spinnerData.endTime = (int)(spinnerData.endTime / speedMultiplier);
        });
    }
}

package tools.beatmap_speed_changer;

import osu.beatmap.Beatmap;
import tools.audio_file_converter.AudioFileSpeedChanger;
import tools.audio_file_converter.AudioFileType;

import java.io.File;
import java.text.DecimalFormat;

public class BeatmapSpeedChanger {

    public static void execute(final Beatmap beatmap, final File audioFile, final double speedMultiplier) {
        final String formattedSpeedMultiplier = new DecimalFormat("#0.00").format(speedMultiplier).replace(",", ".");
        System.out.println("Scaling audio by " + formattedSpeedMultiplier + "x ...");
        final File speedChangedAudioFile = AudioFileSpeedChanger.changeSpeed(audioFile, audioFile.getParentFile(), AudioFileType.MP3, speedMultiplier);
        System.out.println("Done.\n");

        System.out.println("Scaling beatmap data by " + formattedSpeedMultiplier + "x ...");
        beatmap.general.audioFileName = speedChangedAudioFile.getName();
        TimingPointTimelineScaler.scale(beatmap.timingPoints, speedMultiplier);
        HitObjectTimelineScaler.scale(beatmap.hitObjects, speedMultiplier);
        scaleHeaderData(beatmap, speedMultiplier);
        scaleEventData(beatmap, speedMultiplier);
        System.out.println("Done.\n");
    }

    private static void scaleHeaderData(final Beatmap beatmap, final double speedMultiplier) {
        beatmap.general.previewTime = (int)(beatmap.general.previewTime/speedMultiplier);
        beatmap.difficulty.approachRate = getScaledApproachRate(beatmap.difficulty.approachRate, speedMultiplier);

        // very rough approximation using the same function as approach rate
        beatmap.difficulty.overallDifficulty = getScaledApproachRate(beatmap.difficulty.overallDifficulty, speedMultiplier);

        // using the mean between the conversion of the approach rate and the initial value
        beatmap.difficulty.hpDrainRate = ((int)(((getScaledApproachRate(beatmap.difficulty.hpDrainRate, speedMultiplier)
                + beatmap.difficulty.hpDrainRate)/2)*10))/10.0;
    }

    private static void scaleEventData(Beatmap beatmap, double speedMultiplier) {
        beatmap.events.backgroundData.forEach(backgroundData -> {
            backgroundData.startTime /= speedMultiplier;
        });
        beatmap.events.videoData.forEach(videoData -> {
            videoData.startTime /= speedMultiplier;
        });
        beatmap.events.breakData.forEach(breakData -> {
            breakData.startTime /= speedMultiplier;
            breakData.endTime   /= speedMultiplier;
        });
    }

    private static double getScaledApproachRate(final double approachRate, double speedMultiplier) {
        final double approachRateMillis = ApproachRateFunction.getMillis(approachRate);
        final double scaledMillis = approachRateMillis / speedMultiplier;
        final double scaledAr = ApproachRateFunction.getApproachRate(scaledMillis);
        return getOrCapped(scaledAr, 0, 10);
    }

    private static double getOrCapped(final double x, final double min, final double max) {
        if(x > max) {
            return max;
        }
        return Math.max(x, min);
    }
}

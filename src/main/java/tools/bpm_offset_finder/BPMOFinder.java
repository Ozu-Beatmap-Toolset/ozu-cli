package tools.bpm_offset_finder;

import org.jtransforms.fft.FloatFFT_1D;
import osu.beatmap.Beatmap;
import tools.audio_file_converter.AudioFileConverter;
import tools.audio_file_converter.AudioFileType;
import tools.wav_file_untangler.ChannelType;
import tools.wav_file_untangler.WavFileUntangler;
import util.data_structure.tupple.Tuple2;
import util.file.IOFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class BPMOFinder {

    public static void execute(final Beatmap beatmap, final File mp3File) throws Exception {
        System.out.print("Cleaning up filesystem... ");
        new File("cache\\audio.wav").delete();
        System.out.println("Done.");

        System.out.print("Converting \"" + mp3File.getName() + "\" into .wav format... ");
        final File wavFile = AudioFileConverter.convert(mp3File, new File("cache"), AudioFileType.WAVE);
        final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        final AudioFormat audioFormat = audioInputStream.getFormat();
        System.out.println("Done.");

        System.out.print("Reading audio data... ");
        final byte[] audioData = audioInputStream.readAllBytes();
        final float[] rightChannelBuffer = WavFileUntangler.extractChannel(ChannelType.RIGHT, audioData, audioFormat);
        System.out.println("Done.");

        System.out.print("Computing bpm... ");
        final Optional<Tuple2<Double, Double>> bpmAndOffsetOpt = findBpmOfSoundBuffer(rightChannelBuffer, audioFormat);
        System.out.println("Done.");

        bpmAndOffsetOpt.ifPresent(bpmAndOffset -> {
            final double beatLength = 60000/bpmAndOffset.value1;
            final int offset = (int)(bpmAndOffset.value2*beatLength/(Math.PI*2));
            System.out.println("\nBpm is: " + bpmAndOffset.value1);

            beatmap.timingPoints.redLineData.get(0).beatLength = beatLength;
            beatmap.timingPoints.redLineData.get(0).time = offset;
        });
    }

    private static Optional<Tuple2<Double, Double>> findBpmOfSoundBuffer(float[] a, final AudioFormat audioFormat) {
        final double dftResolution = audioFormat.getSampleRate() / a.length;
        final float[] complexPairs = fftOf(a);
        final float[] amplitudesSquared = computeAmplitudesSquaredFromComplexPairs(complexPairs);

        final int smallestFrequencyIndex = (int)(1/dftResolution);
        final int biggestFrequencyIndex = (int)(5/dftResolution);

        float biggestAmplitudeYet = 0;
        int bestIndexSoFar = -1;

        for(int i = smallestFrequencyIndex; i <= biggestFrequencyIndex; i++) {
            if(amplitudesSquared[i] > biggestAmplitudeYet) {
                biggestAmplitudeYet = amplitudesSquared[i];
                bestIndexSoFar = i;
            }
        }

        if(bestIndexSoFar == -1) {
            return Optional.empty();
        }

        final double phase = Math.atan2(complexPairs[bestIndexSoFar*2 + 1], complexPairs[bestIndexSoFar*2]);

        return Optional.of(new Tuple2<>(bestIndexSoFar * dftResolution * 60, phase >= 0 ? phase : 2*Math.PI + phase));
    }

    private static float[] fftOf(float[] a) {
        FloatFFT_1D fftFunc = new FloatFFT_1D(a.length);
        float[] complexPairs = Arrays.copyOf(a, a.length*2);
        fftFunc.realForwardFull(complexPairs);
        return complexPairs;
    }

    private static float[] computeAmplitudesSquaredFromComplexPairs(float[] complexPairs) {
        float[] amplitudesSquared = new float[complexPairs.length/2];

        for(int i = 0; i < amplitudesSquared.length; i++) {
            amplitudesSquared[i] = complexPairs[i*2] * complexPairs[i*2] + complexPairs[i*2+1] * complexPairs[i*2+1];
        }

        return amplitudesSquared;
    }
}

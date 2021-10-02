package tools.bpm_offset_finder;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.FloatFFT_1D;
import osu.beatmap.Beatmap;
import tools.audiofile_converter.WinWavCliAccess;
import util.data_structure.tupple.Tuple2;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class BPMOFinder {

    public static void execute(final Beatmap beatmap, final File mp3File) throws Exception {
        System.out.println("Converting \"" + mp3File.getName() + "\" into .wav format...");
        final File wavFile = WinWavCliAccess.convertAudioFileToWavFile(mp3File);
        final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        final AudioFormat audioFormat = audioInputStream.getFormat();
        System.out.println("Done.");

        System.out.println("\nReading audio data...");
        final byte[] audioData = audioInputStream.readAllBytes();
        final float[] rightChannelBuffer = extractRightChannel(audioData, audioFormat);
        System.out.println("Done.");

        System.out.println("\nComputing bpm...");
        final Optional<Tuple2<Double, Double>> bpmAndOffsetOpt = findBpmOfSoundBuffer(rightChannelBuffer, audioFormat);
        System.out.println("Done.");

        bpmAndOffsetOpt.ifPresent(bpmAndOffset -> {
            final double beatLength = 60000/bpmAndOffset.value1;
            final int offset = (int)(bpmAndOffset.value1*beatLength/(Math.PI*2));

            System.out.println(beatLength);
            beatmap.timingPoints.redLineData.get(0).beatLength = beatLength;
            beatmap.timingPoints.redLineData.get(0).time = offset;
        });
    }

    private static float[] extractRightChannel(byte[] audioData, AudioFormat audioFormat) {
        final int sampleSize = audioFormat.getFrameSize()/audioFormat.getChannels();
        final int rightChannelLength = audioData.length/audioFormat.getFrameSize();
        final float[] rightChannelData = new float[rightChannelLength];

        for(int i = 0; i < rightChannelData.length; i++) {
            rightChannelData[i] = 0;
            for(int j = 0; j < sampleSize; j++) {
                if(audioFormat.isBigEndian()) {
                    final int audioByteIndex = i * audioFormat.getFrameSize() + (sampleSize - j - 1) + sampleSize;
                    if(j == 0) {
                        rightChannelData[i] += audioData[audioByteIndex] << (j * 8);
                    }
                    else {
                        rightChannelData[i] += asUnsignedByte(audioData[audioByteIndex]) << (j * 8);
                    }
                }
                else {
                    final int audioByteIndex = i * audioFormat.getFrameSize() + j + sampleSize;
                    if(j == sampleSize - 1) {
                        rightChannelData[i] += audioData[audioByteIndex] << (j * 8);
                    }
                    else {
                        rightChannelData[i] += asUnsignedByte(audioData[audioByteIndex]) << (j * 8);
                    }
                }
            }

            rightChannelData[i] = rightChannelData[i] / 32767;
        }

        return rightChannelData;
    }

    private static int asUnsignedByte(byte b) {
        return b >= 0 ? ((int)b) : ((int)b) + 256;
    }

    private static Optional<Tuple2<Double, Double>> findBpmOfSoundBuffer(float[] a, final AudioFormat audioFormat) {
        final double dftResolution = audioFormat.getSampleRate() / a.length;
        final float[] complexPairs = fftOf(a);
        final float[] amplitudesSquared = computeAmplitudesSquaredFromComplexPairs(complexPairs);

        final int smallestFrequencyIndex = (int)(1/dftResolution);
        final int biggestFrequencyIndex = (int)(1000/dftResolution);

        float biggestAmplitudeYet = 0;
        int bestIndexSoFar = -1;

        for(int i = smallestFrequencyIndex; i < biggestFrequencyIndex; i++) {
            if(amplitudesSquared[i] > biggestAmplitudeYet) {
                biggestAmplitudeYet = amplitudesSquared[i];
                bestIndexSoFar = i;
            }
        }

        if(bestIndexSoFar == -1) {
            return Optional.empty();
        }

        final double phase = Math.atan2(complexPairs[bestIndexSoFar*2 + 1], complexPairs[bestIndexSoFar*2]);

        return Optional.of(new Tuple2<>(bestIndexSoFar * dftResolution * 60, phase));
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

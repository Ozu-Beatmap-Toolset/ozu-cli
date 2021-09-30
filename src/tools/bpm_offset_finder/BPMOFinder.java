package tools.bpm_offset_finder;

import org.jtransforms.fft.DoubleFFT_1D;
import osu.beatmap.BeatMap;
import tools.audiofile_converter.FfmpegCliAccess;
import util.data_structure.tupple.Tuple2;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class BPMOFinder {

    public static void execute(final BeatMap beatMap, final File mp3File) throws Exception {
        System.out.println(mp3File.getAbsolutePath());
        final File wavFile = FfmpegCliAccess.convertAudioFileToWavFile(mp3File);
        final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        final AudioFormat audioFormat = audioInputStream.getFormat();
        final byte[] audioData = audioInputStream.readAllBytes();
        final double[] leftChannelBuffer = extractRightChannel(audioData, audioFormat);
        final Optional<Tuple2<Double, Double>> bpmAndOffsetOpt = findBpmOfSoundBuffer(leftChannelBuffer, audioFormat);

        bpmAndOffsetOpt.ifPresent(bpmAndOffset -> {
            final double beatLength = 60000/bpmAndOffset.value1;
            final int offset = (int)(bpmAndOffset.value1*beatLength/(Math.PI*2));
            System.out.println(beatLength);
            System.out.println(offset);
            beatMap.timingPoints.redLineData.get(0).beatLength = beatLength;
            beatMap.timingPoints.redLineData.get(0).time = offset;
        });
    }

    private static double[] extractLeftChannel(byte[] audioData, AudioFormat audioFormat) {
        final int sampleSize = audioFormat.getFrameSize()/audioFormat.getChannels();
        final int leftChannelLength = audioData.length/audioFormat.getFrameSize();
        final double[] leftChannelData = new double[leftChannelLength];

        for(int i = 0; i < leftChannelData.length; i++) {
            leftChannelData[i] = 0;
            for(int j = 0; j < sampleSize; j++) {
                if(audioFormat.isBigEndian()) {
                    final int audioByteIndex = i * sampleSize + j;
                    leftChannelData[i] += audioData[audioByteIndex] << (j*8);
                }
                else {
                    final int audioByteIndex = i * sampleSize + (sampleSize - j - 1);
                    leftChannelData[i] += audioData[audioByteIndex] << (j*8);
                }
            }

            leftChannelData[i] /= 1 << (sampleSize*8);
        }

        return leftChannelData;
    }

    private static double[] extractRightChannel(byte[] audioData, AudioFormat audioFormat) {
        final int sampleSize = audioFormat.getFrameSize()/audioFormat.getChannels();
        final int rightChannelLength = audioData.length/audioFormat.getFrameSize();
        final double[] rightChannelData = new double[rightChannelLength];

        for(int i = 0; i < rightChannelData.length; i++) {
            rightChannelData[i] = 0;
            for(int j = 0; j < sampleSize; j++) {
                if(audioFormat.isBigEndian()) {
                    final int audioByteIndex = i * sampleSize + j + sampleSize;
                    rightChannelData[i] += audioData[audioByteIndex] << (j*8);
                }
                else {
                    final int audioByteIndex = i * sampleSize + (sampleSize - j - 1) + sampleSize;
                    rightChannelData[i] += audioData[audioByteIndex] << (j*8);
                }
            }

            rightChannelData[i] /= 1 << (sampleSize*8);
        }

        return rightChannelData;
    }

    private static Optional<Tuple2<Double, Double>> findBpmOfSoundBuffer(double[] a, final AudioFormat audioFormat) {
        final double dftResolution = audioFormat.getSampleRate() / a.length;
        final double[] complexPairs = fftOf(a);
        final double[] amplitudesSquared = computeAmplitudesSquaredFromComplexPairs(complexPairs);

        final int smallestFrequencyIndex = (int)(1.0/dftResolution);
        final int biggestFrequencyIndex = (int)(5.0/dftResolution);

        double biggestAmplitudeYet = 0;
        int bestIndexSoFar = -1;

        for(int i = smallestFrequencyIndex; i < biggestFrequencyIndex; i++) {
            if(amplitudesSquared[i] > biggestAmplitudeYet) {
                biggestAmplitudeYet = amplitudesSquared[i];
                bestIndexSoFar = i;

                System.out.println(amplitudesSquared[i] + " : " + (bestIndexSoFar * dftResolution)*60);
            }
        }

        if(bestIndexSoFar == -1) {
            return Optional.empty();
        }

        final double phase = Math.atan2(complexPairs[bestIndexSoFar*2 + 1], complexPairs[bestIndexSoFar*2]);

        return Optional.of(new Tuple2<>(bestIndexSoFar * dftResolution * 60, phase));
    }

    private static double[] fftOf(double[] a) {
        DoubleFFT_1D fftFunc = new DoubleFFT_1D(a.length);
        double[] complexPairs = Arrays.copyOf(a, a.length*2);
        fftFunc.realForwardFull(complexPairs);
        return complexPairs;
    }

    private static double[] computeAmplitudesSquaredFromComplexPairs(double[] complexPairs) {
        double[] amplitudesSquared = new double[complexPairs.length/2];

        for(int i = 0; i < amplitudesSquared.length; i++) {
            amplitudesSquared[i] = complexPairs[i] * complexPairs[i] + complexPairs[i+1] * complexPairs[i+1];
        }

        return amplitudesSquared;
    }
}

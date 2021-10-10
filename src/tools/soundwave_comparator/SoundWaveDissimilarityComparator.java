package tools.soundwave_comparator;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.Arrays;

public class SoundWaveDissimilarityComparator {

    public static double compare(final double[] waveFunction1, final double[] waveFunction2) {
        if(waveFunction1.length != waveFunction2.length) {
            throw new IllegalArgumentException("arrays should have the same length");
        }

        final double[] frequencyFunction1 = Arrays.copyOf(waveFunction1, waveFunction1.length*2);
        final double[] frequencyFunction2 = Arrays.copyOf(waveFunction2, waveFunction2.length*2);

        final DoubleFFT_1D fftFunc1 = new DoubleFFT_1D(frequencyFunction1.length);
        final DoubleFFT_1D fftFunc2 = new DoubleFFT_1D(frequencyFunction2.length);

        fftFunc1.realForwardFull(frequencyFunction1);
        fftFunc2.realForwardFull(frequencyFunction2);

        final double[] amplitudeFunction1 = extractSquaredAmplitudeFromFft(frequencyFunction1);
        final double[] amplitudeFunction2 = extractSquaredAmplitudeFromFft(frequencyFunction2);

        final double meanOfFunction1 = computeMean(amplitudeFunction1);
        final double meanOfFunction2 = computeMean(amplitudeFunction2);

        double result = 0;

        for(int i = 0; i < amplitudeFunction1.length; i++) {
            result += Math.abs((amplitudeFunction1[i]-meanOfFunction1) - (amplitudeFunction2[i]-meanOfFunction2));
        }

        result /= amplitudeFunction1.length * amplitudeFunction1.length;
        result *= 0.5;

        return result;
    }

    private static double[] extractSquaredAmplitudeFromFft(final double[] complexFrequencyData) {
        final double[] result = new double[complexFrequencyData.length/2];

        for(int i = 0; i < result.length; i++) {
            final int realIndex = i*2;
            final int complexIndex = i*2 + 1;
            result[i] = complexFrequencyData[realIndex]*complexFrequencyData[realIndex] +
                    complexFrequencyData[complexIndex]*complexFrequencyData[complexIndex];
        }

        return result;
    }

    private static double computeMean(double[] amplitudeFunction1) {
        double mean = 0;

        for(double element: amplitudeFunction1) {
            mean += element;
        }

        return mean;
    }
}

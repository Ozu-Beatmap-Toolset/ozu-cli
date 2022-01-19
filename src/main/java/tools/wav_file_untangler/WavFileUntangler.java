package tools.wav_file_untangler;

import javax.sound.sampled.AudioFormat;

public class WavFileUntangler {

    public static float[] extractChannel(final ChannelType channelType, byte[] audioData, AudioFormat audioFormat) {
        final int sampleSize = audioFormat.getFrameSize()/audioFormat.getChannels();
        final int rightChannelLength = audioData.length/audioFormat.getFrameSize();
        final float[] rightChannelData = new float[rightChannelLength];

        for(int i = 0; i < rightChannelData.length; i++) {
            rightChannelData[i] = 0;
            for(int j = 0; j < sampleSize; j++) {
                final int audioDataBaseIndex = i * audioFormat.getFrameSize() + sampleSize * channelType.asValue();
                if(audioFormat.isBigEndian()) {
                    final int audioDataIndexForBigEndian = audioDataBaseIndex + (sampleSize - j - 1);
                    if(j == 0) {
                        rightChannelData[i] += audioData[audioDataIndexForBigEndian] << (j * 8);
                    }
                    else {
                        rightChannelData[i] += asUnsignedByte(audioData[audioDataIndexForBigEndian]) << (j * 8);
                    }
                }
                else {
                    final int audioDataIndexForSmallEndian = audioDataBaseIndex + j;
                    if(j == sampleSize - 1) {
                        rightChannelData[i] += audioData[audioDataIndexForSmallEndian] << (j * 8);
                    }
                    else {
                        rightChannelData[i] += asUnsignedByte(audioData[audioDataIndexForSmallEndian]) << (j * 8);
                    }
                }
            }

            rightChannelData[i] /= ((1 << (sampleSize * 8 - 1)) - 1);
        }

        return rightChannelData;
    }

    private static int asUnsignedByte(byte b) {
        return b >= 0 ? ((int)b) : ((int)b) + 256;
    }
}

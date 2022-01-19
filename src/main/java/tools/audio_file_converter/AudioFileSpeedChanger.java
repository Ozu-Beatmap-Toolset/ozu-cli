package tools.audio_file_converter;

import java.io.File;

public class AudioFileSpeedChanger {

    public static File changeSpeed(final File initialAudioFile, final File destinationFolder, final AudioFileType outputFileType, final double speedMultiplier) {
        final String outputFilePath = destinationFolder.getAbsolutePath() + "\\" +
                initialAudioFile.getName().split("\\.")[0] +
                " - " + speedMultiplier + "X" + outputFileType.fileExtension();
        final File outputFile = new File(outputFilePath);
        final String atempoParameter = atempoParameterGenerator(speedMultiplier);

        FfmpegCliAccess.call("\\\"" + initialAudioFile.getAbsolutePath() + "\\\"", "-af", "\\\"" + atempoParameter + "\\\"", "\\\"" + outputFile.getAbsolutePath() + "\\\"");

        return outputFile;
    }

    private static String atempoParameterGenerator(double atempoMultiplier) {
        double amountOfAtempoNeeded = Math.log(atempoMultiplier) / Math.log(2);
        final double lastAtempoAmount = Math.pow(2, amountOfAtempoNeeded % 1);
        final StringBuilder atempoCombined = new StringBuilder();

        while(amountOfAtempoNeeded > 1) {
            amountOfAtempoNeeded--;
            atempoCombined.append("atempo=2.0,");
        }
        while(amountOfAtempoNeeded < -1) {
            amountOfAtempoNeeded++;
            atempoCombined.append("atempo=0.5,");
        }
        atempoCombined.append("atempo=")
                .append(lastAtempoAmount);

        return atempoCombined.toString();
    }
}

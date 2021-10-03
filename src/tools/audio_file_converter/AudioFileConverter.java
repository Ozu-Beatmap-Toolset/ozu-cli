package tools.audio_file_converter;

import java.io.File;

public class AudioFileConverter {

    public static File convert(
            final File initialAudioFile,
            final File destinationFolder,
            final AudioFileType outputFileType) {
        final String outputFilePath = destinationFolder.getAbsolutePath() + "\\" +
                initialAudioFile.getName().split("\\.")[0] +
                outputFileType.fileExtension();
        final File outputFile = new File(outputFilePath);

        FfmpegCliAccess.call(
                "\\\"" + initialAudioFile.getAbsolutePath() + "\\\"",
                "\\\"" + outputFile.getAbsolutePath() + "\\\"");

        return outputFile;
    }


}

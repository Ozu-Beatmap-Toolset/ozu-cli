package tools.audio_file_converter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FfmpegCliAccess {

    public static void call(String... args) {
        final String[] ffmpegCommandArgs = {"powershell.exe", "ffmpeg -i " + String.join(" ", args)};
        try {
            final ProcessBuilder builder = new ProcessBuilder(ffmpegCommandArgs);
            final Process p = builder.start();
            p.waitFor(4, TimeUnit.SECONDS);
        }
        catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
}

package tools.audio_file_converter;

import java.io.IOException;

public class FfmpegCliAccess {

    public static void call(String... args) {
        final String[] ffmpegCommandArgs = {"powershell.exe", "ffmpeg -i " + String.join(" ", args)};
        try {
            final ProcessBuilder builder = new ProcessBuilder(ffmpegCommandArgs);
            final Process p = builder.start();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.getOutputStream().close();
            p.waitFor();
            p.destroy();
        }
        catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
}

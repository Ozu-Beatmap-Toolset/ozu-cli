package os;

import java.io.IOException;

public class CmdShell {

    public static int call(String... args) {
        final String[] ffmpegCommandArgs = {String.join(" ", args)};
        try {
            final ProcessBuilder builder = new ProcessBuilder(ffmpegCommandArgs);
            final Process p = builder.start();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.getOutputStream().close();
            p.waitFor();
            p.destroy();
            return p.exitValue();
        }
        catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
            return -1;
        }
    }
}

package os.win.powershell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Powershell {

    public static int call(String... args) {
        return call(new byte[]{}, args);
    }

    public static int call(byte[] output, String... args) {
        var params = Arrays.stream(new String[]{"powershell.exe"}).collect(Collectors.toList());
        params.addAll(Arrays.stream(args).collect(Collectors.toList()));
        final String[] ffmpegCommandArgs =  params.toArray(new String[]{});
        try {
            final ProcessBuilder builder = new ProcessBuilder(ffmpegCommandArgs);
            final Process p = builder.start();
            p.getOutputStream().write(output);
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
            p.destroy();
            return p.exitValue();
        }
        catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
            return -1;
        }
    }

    public static int callAndLog(String... args) {
        return callAndLog(new byte[]{}, args);
    }

    public static int callAndLog(byte[] output, String... args) {
        var params = Arrays.stream(new String[]{"powershell.exe"}).collect(Collectors.toList());
        params.addAll(Arrays.stream(args).collect(Collectors.toList()));
        final String[] ffmpegCommandArgs =  params.toArray(new String[]{});
        try {
            final ProcessBuilder builder = new ProcessBuilder(ffmpegCommandArgs);
            final Process p = builder.start();
            p.getOutputStream().write(output);
            p.getOutputStream().close();
            displayInputStream(p.getInputStream());
            p.getInputStream().close();
            displayInputStream(p.getErrorStream());
            p.getErrorStream().close();
            p.waitFor();
            p.destroy();
            return p.exitValue();
        }
        catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
            return -1;
        }
    }

    private static void displayInputStream(InputStream inputStream) throws IOException {
        String s;
        String previousLine = "";
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(inputStream));
        while ((s = stdInput.readLine()) != null) {
            if(!s.equals(previousLine)) {
                System.out.println(s);
            }
            previousLine = s;
        }
    }
}

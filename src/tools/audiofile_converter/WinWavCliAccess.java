package tools.audiofile_converter;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WinWavCliAccess {

    public static final String EXPORT_CACHE_LOCATION = "src\\cache";
    public static final Map<AudioFileFormat.Type, String> AVAILABLE_FORMATS = new HashMap<>();
    static {
        AVAILABLE_FORMATS.put(AudioFileFormat.Type.WAVE, ".wav");
    }
    public static final List<File> CACHED_FILES = new ArrayList<>();

    public static File convertAudioFileToWavFile(final File mp3File) {
        final String waveFilePath = EXPORT_CACHE_LOCATION + "\\temp" + CACHED_FILES.size() + AVAILABLE_FORMATS.get(AudioFileFormat.Type.WAVE);

        call("powershell.exe ", ".\\python\\WinWav\\WinWav\\Scripts\\python.exe " + ".\\python\\WinWav\\winwav.py " + "\\\"" + mp3File.getAbsolutePath() + "\\\" " + "\\\"" + waveFilePath + "\\\"");

        final File wavFile = new File(waveFilePath);
        CACHED_FILES.add(wavFile);

        return wavFile;
    }

    public static void deleteCurrentCacheOnExit() {
        CACHED_FILES.forEach(File::deleteOnExit);
    }

    private static void call(String... args) {
        try {
            ProcessBuilder builder = new ProcessBuilder(args);
            Process p = builder.start();
            p.waitFor(4, TimeUnit.SECONDS);
            p = null;
        }
        catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
}

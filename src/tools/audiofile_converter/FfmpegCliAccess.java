package tools.audiofile_converter;

import util.file.IOFile;

import javax.sound.sampled.AudioFileFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FfmpegCliAccess {

    public static final String EXPORT_CACHE_LOCATION = "src\\cache";
    public static final Map<AudioFileFormat.Type, String> AVAILABLE_FORMATS = new HashMap<>();
    static {
        AVAILABLE_FORMATS.put(AudioFileFormat.Type.WAVE, ".wav");
    }
    public static final List<File> CACHED_FILES = new ArrayList<>();

    public static File convertAudioFileToWavFile(final File mp3File) {
        final File wavFile = new File(EXPORT_CACHE_LOCATION + "\\temp" + CACHED_FILES.size() + AVAILABLE_FORMATS.get(AudioFileFormat.Type.WAVE));
        call("ffmpeg -i \\\"" + mp3File.getAbsolutePath() + "\\\" " + "\\\"" + wavFile.getAbsolutePath() + "\\\"");
        CACHED_FILES.add(wavFile);

        return wavFile;
    }

    public static void deleteCurrentCacheOnExit() {
        CACHED_FILES.forEach(File::deleteOnExit);
    }

    private static void call(String args) {
        try {
            ProcessBuilder builder = new ProcessBuilder("powershell.exe", args);
            Process p = builder.start();
            final long startTime = System.currentTimeMillis();
            while(p.isAlive()) {
                if(System.currentTimeMillis() - startTime > 1000) {
                    p.destroy();
                    //throw new RuntimeException("Couldn't convert mp3 file");
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

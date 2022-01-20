package tools.audio_file_converter;


import os.win.powershell.Powershell;

public class FfmpegCliAccess {

    public static boolean notInstalled() {
        final String[] ffmpegCommandArgs = {"ffmpeg", "-version"};
        return Powershell.call(ffmpegCommandArgs) != 0;
    }

    public static void call(String... args) {
        final String[] ffmpegCommandArgs = {"ffmpeg", "-i", String.join(" ", args)};
        Powershell.call(ffmpegCommandArgs);
    }
}

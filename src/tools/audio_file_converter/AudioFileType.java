package tools.audio_file_converter;

public enum AudioFileType {
    WAVE(".wav"),
    MP3(".mp3");

    private final String fileExtension;

    AudioFileType(final String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String fileExtension() {
        return fileExtension;
    }
}

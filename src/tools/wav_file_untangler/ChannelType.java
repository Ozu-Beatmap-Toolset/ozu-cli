package tools.wav_file_untangler;

public enum ChannelType {
    LEFT(0),
    RIGHT(1),
    MONO(0);

    private final int value;

    ChannelType(final int value) {
        this.value = value;
    }

    public int asValue() {
        return value;
    }
}

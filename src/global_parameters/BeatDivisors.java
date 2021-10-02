package global_parameters;

import java.util.List;

public class BeatDivisors {

    private final List<Integer> allowedBeatDivisors;

    public BeatDivisors(final List<Integer> beatDivisors) {
        this.allowedBeatDivisors = beatDivisors;
    }

    public List<Integer> getAllowed() {
        return allowedBeatDivisors;
    }

}

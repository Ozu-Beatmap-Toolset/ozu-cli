package global_parameters;

import java.util.List;
import java.util.Optional;

public class GlobalCliParameters {

    private final Optional<ExportInPlace> exportInPlace;
    private final Optional<TimeInterval> timeInterval;
    private final Optional<BeatDivisors> beatDivisors;

    public GlobalCliParameters(final Optional<ExportInPlace> exportInPlace, final Optional<TimeInterval> timeInterval, Optional<BeatDivisors> beatDivisors) {
        this.exportInPlace = exportInPlace;
        this.timeInterval = timeInterval;
        this.beatDivisors = beatDivisors;
    }

    public boolean timeIntervalContains(final int time) {
        return timeInterval.map(interval -> interval.contains(time))
                .orElse(true);
    }

    public List<Integer> getBeatDivisors() {
        return beatDivisors.map(BeatDivisors::getAllowed)
                .orElse(List.of());
    }

    public boolean isExportingInPlace() {
        return exportInPlace.isPresent();
    }
}

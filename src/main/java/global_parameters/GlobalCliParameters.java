package global_parameters;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class GlobalCliParameters {

    private final Optional<ExportInPlace> exportInPlace;
    private final Optional<TimeInterval> timeInterval;
    private final Optional<BeatDivisors> beatDivisors;
    private final Optional<File> beatmapFile;

    public GlobalCliParameters(
            final Optional<ExportInPlace> exportInPlace,
            final Optional<TimeInterval> timeInterval,
            final Optional<BeatDivisors> beatDivisors,
            final Optional<File> beatmapFile) {
        this.exportInPlace = exportInPlace;
        this.timeInterval = timeInterval;
        this.beatDivisors = beatDivisors;
        this.beatmapFile = beatmapFile;
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

    public File getBeatmapFile() {
        return beatmapFile.get();
    }
}

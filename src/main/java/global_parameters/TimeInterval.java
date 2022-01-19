package global_parameters;

import util.data_structure.tupple.Tuple2;

public class TimeInterval {

    private Tuple2<Integer, Integer> values;

    public TimeInterval(final int min, final int max) {
        this.values = new Tuple2<>(min, max);
    }

    public boolean contains(final int time) {
        return values.value1 <= time && values.value2 >= time;
    }

    public int min() {
        return values.value1;
    }

    public int max() {
        return values.value2;
    }
}

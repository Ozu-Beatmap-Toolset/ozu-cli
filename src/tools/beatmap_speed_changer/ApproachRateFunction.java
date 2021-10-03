package tools.beatmap_speed_changer;

public class ApproachRateFunction {

    private static final double A = -2.78547272202974;
    private static final double B = -107.145272779703;
    private static final double C = 1791.1684129578;

    public static double getMillis(double approachRate) {
        return (A*approachRate + B)*approachRate + C;
    }

    public static double getApproachRate(double millis) {
        final double preciseValue = (-B - Math.sqrt(B*B - 4*A*(C-millis))) / (2*A);
        return ((int)(preciseValue*10 + 0.5))/10.0;
    }
}

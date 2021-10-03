package tools.timing_snapper;

import global_parameters.GlobalCliParameters;
import osu.beatmap.Beatmap;
import util.data_structure.tupple.Tuple2;

public class NoteSnapper {

    public static void execute(final Beatmap beatmap, final GlobalCliParameters globalParameters) {
        final CircleSnapper circleSnapper = new CircleSnapper();
        final SliderSnapper sliderSnapper = new SliderSnapper();
        final SpinnerSnapper spinnerSnapper = new SpinnerSnapper();

        circleSnapper.snap(beatmap, globalParameters);
        sliderSnapper.snap(beatmap, globalParameters);
        spinnerSnapper.snap(beatmap, globalParameters);

        displayAmountsOfFixedObject(
                circleSnapper.getAmountOfSnapsDone(),
                sliderSnapper.getAmountOfSnapsDone(),
                spinnerSnapper.getAmountOfSnapsDone());
    }

    private static void displayAmountsOfFixedObject(int amountOfFixedCircles, Tuple2<Integer, Integer> amountsOfFixedSliders, Tuple2<Integer, Integer> amountsOfFixedSpinners) {
        System.out.println("Snapped:");
        System.out.println("  " + amountOfFixedCircles + " circle" + (amountOfFixedCircles > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSliders.value1 + " slider head" + (amountsOfFixedSliders.value1 > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSliders.value2 + " slider tail" + (amountsOfFixedSliders.value2 > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSpinners.value1 + " spinner head" + (amountsOfFixedSpinners.value1 > 1 ? "s" : ""));
        System.out.println("  " + amountsOfFixedSpinners.value2 + " spinner tail" + (amountsOfFixedSpinners.value2 > 1 ? "s" : ""));
    }
}

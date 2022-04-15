package AlgorithmsPackage;

import ModelPackage.Specimen;
import SetupPackage.Constants;

import java.util.Arrays;

public class RandomMethod {
    public static int RandomMethod() {
        Specimen currentBest = new Specimen(Constants.WIDTH, Constants.HEIGHT, Constants.MACHINE_COUNT);

        Specimen current = currentBest;
        for (int i = 0; i < Constants.GENERATION_MAX_NUMBER * Constants.SPECIMEN_QUANTITY; i++) {
            if (currentBest.getFitnessValue() > current.getFitnessValue()) {
                currentBest = current;
            }
            current = new Specimen(Constants.WIDTH, Constants.HEIGHT, Constants.MACHINE_COUNT);
        }

        return currentBest.getFitnessValue();
    }
}

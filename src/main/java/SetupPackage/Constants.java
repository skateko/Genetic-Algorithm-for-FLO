package SetupPackage;

import DataLoaderPackage.JSONloader;
import org.json.simple.JSONArray;

public class Constants {
    public static final String PATH = "FLOGeneticAlgorithmMaven\\src\\main\\resources\\";
    public static final String SOURCE = "source";
    public static final String DEST = "dest";
    public static final String COST = "cost";
    public static final String AMOUNT = "amount";
    public static final String COST_FILENAME = "hard_cost";
    public static final String FLOW_FILENAME = "hard_flow";
    public static final JSONArray DATA_FROM_JSON = JSONloader.getJson(COST_FILENAME + FLOW_FILENAME, PATH);
    public static final int DRAW_ALGORITHM_THRESHOLD = 7;
    public static final int ACCEPTABLE_SOLUTION = 6400; //  4818 11055
    public static final double CROSSOVER_PROBABILITY = 0.75;
    public static final int SPECIMEN_QUANTITY = 500;
    public static final int WIDTH = 6;
    public static final int HEIGHT = 5;
    public static final int MACHINE_COUNT = 24;
    public static final double TOURNAMENT_SIZE_MULTIPLIER = 0.01; //0.15 //0.01
    public static final double ELITIZM_FRACT = 0.66;         //0.67
    public static final double MUTATION_PROBABILITY = 1/((double)(WIDTH * HEIGHT)); //8 * 1/((double)(WIDTH * HEIGHT))
    public static final int GENERATION_MAX_NUMBER = 500;
    public static final int AMOUNT_OF_SAMPLES = 10;
}

package SimulationPackage;

import AlgorithmsPackage.*;
import DataPackage.DataPerGeneration;
import SetupPackage.Constants;
import com.github.sh0nk.matplotlib4j.Plot;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation {
    public static List<List<DataPerGeneration>> dataFromGenetic = new ArrayList<>();
    public static void Simulation() {
        Integer[] geneticOutputs = new Integer[Constants.AMOUNT_OF_SAMPLES];
        Integer[] randomOutputs = new Integer[Constants.AMOUNT_OF_SAMPLES];

        Integer geneticBest = 0;
        Integer geneticWorst = 0;
        double geneticAVG = 0.0;
        double geneticSTD = 0.0;

        Integer randomBest = 0;
        Integer randomWorst = 0;
        double randomAVG = 0.0;
        double randomSTD = 0.0;

        for (int i = 0; i < Constants.AMOUNT_OF_SAMPLES; i++) {
            geneticOutputs[i] = GeneticAlgorithm.GeneticAlgorithm();
            randomOutputs[i] = RandomMethod.RandomMethod();
        }

        for (int i = 0; i < Constants.AMOUNT_OF_SAMPLES; i++) {
            System.out.println("[" + i + "] " + "GENETIC: The best solution is: " + geneticOutputs[i]);
            System.out.println("[" + i + "] " + "RANDOM: The best solution is: " + randomOutputs[i]);
        }

        geneticBest = findBest(geneticOutputs);
        geneticWorst = findWorst(geneticOutputs);
        geneticAVG = averageOutput(geneticOutputs);
        geneticSTD = stdev(geneticOutputs);

        randomBest = findBest(randomOutputs);
        randomWorst = findWorst(randomOutputs);
        randomAVG = averageOutput(randomOutputs);
        randomSTD = stdev(randomOutputs);

        System.out.println("OVERALL GENETIC: BEST: " +  geneticBest + " WORST: " + geneticWorst + " AVG: " + geneticAVG + " STD: " + geneticSTD);
        System.out.println("OVERALL RANDOM: BEST: " +  randomBest + " WORST: " + randomWorst + " AVG: " + randomAVG + " STD: " + randomSTD);

        drawChartOfBest(findBestIndex(geneticOutputs));
    }

    public static Integer findBest(Integer[] list) {
        Integer best = Integer.MAX_VALUE;
        for (int i = 0; i < list.length; i++) {
            if (list[i] < best) {
                best = list[i];
            }
        }
        return best;
    }

    public static Integer findBestIndex(Integer[] list) {
        Integer best = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] < best) {
                best = list[i];
                index = i;
            }
        }
        return index;
    }

    public static Integer findWorst(Integer[] list) {
        Integer worst = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] > worst) {
                worst = list[i];
            }
        }
        return worst;
    }

    public static double averageOutput(Integer[] list) {
        double sum = 0.0;
        double mean = 0.0;

        for (int i = 0; i < list.length; i++) {         //suma do sredniej
            sum += list[i];
        }
        mean = sum / list.length;

        return mean;
    }

    public static double stdev(Integer[] list){
        double sum = 0.0;
        double mean = 0.0;
        double temp = 0.0;
        double currentElem = 0.0;
        double variation = 0.0;

        mean = averageOutput(list);

        for (int i = 0; i < list.length; i++) {         //liczenie wariancji
            currentElem = Math.pow((list[i] - mean), 2);
            temp += currentElem;
        }

        variation = temp/list.length;                   //wariancja

        return Math.sqrt(variation);                    //odchylenie standardowe
    }

    public static void drawChartOfBest(int indexOfBest) {
        List<DataPerGeneration> data = dataFromGenetic.get(indexOfBest);
        Integer[] generations = new Integer[data.size()];
        Integer[] bestValues = new Integer[data.size()];
        Integer[] worstValues = new Integer[data.size()];
        Integer[] averageValues = new Integer[data.size()];

        System.out.println("SIZE: " + data.size());
        for (int i = 0; i < data.size(); i++) {
            generations[i] = data.get(i).getGenerationNumber();
            bestValues[i] = data.get(i).getBestSolutionForGen();
            worstValues[i] = data.get(i).getWorstSolutionForGen();
            averageValues[i] = data.get(i).getAverageSolutionForGen();
        }
        Plot plt = Plot.create();
        plt.plot()
                .add(Arrays.asList(generations), Arrays.asList(bestValues))
                .label("Best score")
                .linestyle("-");
        plt.plot()
                .add(Arrays.asList(generations), Arrays.asList(worstValues))
                .label("Worst score")
                .linestyle("-");
        plt.plot()
                .add(Arrays.asList(generations), Arrays.asList(averageValues))
                .label("Average score")
                .linestyle("-");
        plt.xlabel("Generation");
        plt.ylabel("Fitness Function Value");
        //plt.text(0.5, 0.2, "text");
        plt.title("Best flow from Genetic Algorithm");
        plt.legend();
        try {
            plt.show();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

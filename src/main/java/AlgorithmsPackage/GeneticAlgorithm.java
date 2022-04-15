package AlgorithmsPackage;

import DataPackage.DataPerGeneration;
import ModelPackage.Population;
import ModelPackage.Specimen;
import OperatorPackage.Operators;
import SetupPackage.Constants;
import SimulationPackage.Simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithm {
    public static int GeneticAlgorithm() {
        int generationNumber = 0;
        double probability;
        Specimen currentBest = null;
        Specimen currentWorst = null;
        int average = 0;
        Specimen child = null;
        List<Specimen> newPopulation = null;
        List<DataPerGeneration> dataFromAlgorithm = new ArrayList<>();
        Population population = new Population(Constants.SPECIMEN_QUANTITY,Constants.WIDTH,Constants.HEIGHT,Constants.MACHINE_COUNT);
        Collections.sort(population.getSpecimens());
        currentBest = population.getSpecimen(0);
        currentWorst = population.getSpecimen(population.getSpecimens().size() - 1);

        while(currentBest.getFitnessValue() > Constants.ACCEPTABLE_SOLUTION) {
            newPopulation = new ArrayList<Specimen>();
            List<Specimen> populationList = population.getSpecimens();
            Collections.sort(populationList);

            currentWorst = populationList.get(populationList.size() - 1);
            for (int i = 0; i < populationList.size(); i++) {
                average += populationList.get(i).getFitnessValue();
            }

            average /= Constants.SPECIMEN_QUANTITY;
            dataFromAlgorithm.add(new DataPerGeneration(generationNumber, currentBest.getFitnessValue(), currentWorst.getFitnessValue(), average));
            average = 0;

            if (generationNumber == Constants.GENERATION_MAX_NUMBER - 1) {
                break;
            }

            for (int i = 0; i < Constants.ELITIZM_FRACT * Constants.SPECIMEN_QUANTITY; i++) {
                newPopulation.add(populationList.get(i));
            }

            while(newPopulation.size() != Constants.SPECIMEN_QUANTITY) {
                Specimen parent1 = population.getSpecimen(Operators.tournamentOperator(population,(int)(Constants.TOURNAMENT_SIZE_MULTIPLIER * Constants.SPECIMEN_QUANTITY)));
                Specimen parent2 = population.getSpecimen(Operators.tournamentOperator(population,(int)(Constants.TOURNAMENT_SIZE_MULTIPLIER * Constants.SPECIMEN_QUANTITY)));
                probability = Math.random();
                if (probability < Constants.CROSSOVER_PROBABILITY) {
                    child = Operators.onePointCrossover(parent1,parent2);
                } else {
                    child = new Specimen(parent1);
                }

                Operators.mutate(child);
                child.recalculateFitness();
                newPopulation.add(child);

                if (currentBest.getFitnessValue() > child.getFitnessValue()) {
                    currentBest = child;
                }
                if (currentWorst.getFitnessValue() < child.getFitnessValue()) {
                    currentWorst = child;
                }
            }
            population.setSpecimens(newPopulation);
            generationNumber++;
        }
        Simulation.dataFromGenetic.add(dataFromAlgorithm);

        System.out.println("GENETIC: The best solution is: " + currentBest.getFitnessValue() + " AT GENERATION (INCLUDING GEN 0): " + (generationNumber + 1));
        System.out.println("GENETIC: His board looks like that: " + Arrays.toString(currentBest.getBoard()));

        return currentBest.getFitnessValue();
    }
}

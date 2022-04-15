package OperatorPackage;

import ModelPackage.Population;
import ModelPackage.Specimen;
import SetupPackage.Constants;

import java.util.*;

public class Operators {
    public static int tournamentOperator(Population population, int tournamentSize) {
        int numberOfSpecimens = population.getSpecimens().size();
        if (tournamentSize < 0 || tournamentSize > numberOfSpecimens) {
            throw new IllegalArgumentException("Tournament size was not in range of specimens in population, tournament size was: " + tournamentSize + " population size: " + numberOfSpecimens);
        }
        Integer[] selectedSpecimenIndexes = new Integer[tournamentSize];
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < numberOfSpecimens; i++) {
            indexes.add(i);
        }

        if (numberOfSpecimens < Constants.DRAW_ALGORITHM_THRESHOLD) {
            Collections.shuffle(indexes);
            for (int i = 0; i < tournamentSize; i++) {
                selectedSpecimenIndexes[i] = indexes.get(i);      //selectedPopulationIndexes = indexes.subList(0,tournamentSize);
            }
        } else {
            Random randomGenerator = new Random();
            boolean indexSelected;
            for (int i = 0; i < tournamentSize; i++) {
                indexSelected = false;
                while(!indexSelected) {
                    indexSelected = true;
                    int randomIndex = randomGenerator.nextInt(numberOfSpecimens);
                    for (int k = 0; k < i; k++) { // k <= i????
                        if (selectedSpecimenIndexes[k] == randomIndex) {
                            indexSelected = false;
                            break;
                        }
                    }
                    if (indexSelected) {
                        selectedSpecimenIndexes[i] = randomIndex;
                    }
                }
            }
        }

        int minFitnessValue = Integer.MAX_VALUE;
        int minSpecimenIndex = 0;
        int currFitnessValue;
        int currentSpecimenIndex;

        for (int i = 0; i < tournamentSize; i++) {
            currentSpecimenIndex = selectedSpecimenIndexes[i];
            currFitnessValue = population.getSpecimen(currentSpecimenIndex).getFitnessValue();

            if (minFitnessValue > currFitnessValue) {
                minFitnessValue = currFitnessValue;
                minSpecimenIndex = currentSpecimenIndex;
            }
        }
        return minSpecimenIndex;
    }

    public static double rouletteMinFunction(int value) {
        return  1/(double)(value - 1);
    }

    public static int rouletteOperator(Population population) {
        int numberOfSpecimens = population.getSpecimens().size();
        double fitnessSum = 0;
        for (int i = 0; i < numberOfSpecimens; i++) {
            //System.out.println(population.getSpecimen(i).getFitnessValue());
            fitnessSum += rouletteMinFunction(population.getSpecimen(i).getFitnessValue());
        }

        double[] endOfEachPeriod = new double[numberOfSpecimens];
        endOfEachPeriod[0] = rouletteMinFunction(population.getSpecimen(0).getFitnessValue())/fitnessSum;

        for (int i = 1; i < numberOfSpecimens; i++) {
            endOfEachPeriod[i] = endOfEachPeriod[i - 1] + rouletteMinFunction(population.getSpecimen(i).getFitnessValue())/fitnessSum;
        }

        double fortuneWinner = Math.random();
        int i = 0;
        while (i < numberOfSpecimens) {
            if (endOfEachPeriod[i] > fortuneWinner) {
                return i;
            }
            i++;
        }
        return numberOfSpecimens - 1;
    }

    public static Specimen onePointCrossover(Specimen fstParent, Specimen sndParent) {
        int width = fstParent.getWidth();
        int height = fstParent.getHeight();
        int machineCount = fstParent.getMachineCount();
        int size = width * height;

        Random random = new Random();
        int randomPointIndex = random.nextInt(size);
        Specimen child = new Specimen(width,height,machineCount);

        for (int i = 0; i < randomPointIndex; i++) {
            child.setGeneBoardIndex(i, fstParent.getGeneFromBoardIndex(i));
        }

        boolean swappable = true;
        for (int i = randomPointIndex; i < size; i++) {
            swappable = true;
            for (int j = 0; j < randomPointIndex; j++) {
                if (fstParent.getGeneFromBoardIndex(j) == sndParent.getGeneFromBoardIndex(i)) {
                    swappable = false;
                }
            }
            if (swappable) {
                child.setGeneBoardIndex(i, sndParent.getGeneFromBoardIndex(i));
            }
            else {
                child.setGeneBoardIndex(i, fstParent.getGeneFromBoardIndex(i));
            }
        }

        ArrayList<Integer> duplicatesList = new ArrayList<Integer>();
        ArrayList<Integer> machinesToSet = new ArrayList<Integer>();

        for (int i = 0 ; i < machineCount; i++) {
            machinesToSet.add(i);                            //LISTA WSZYSTKICH MASZYN
        }

        for (int i = 0; i < child.getBoard().length; i++) { //LISTA POZOSTALYCH MASZYN
            Integer item = child.getBoard()[i];
            if (machinesToSet.contains(item) && item != null) {
                machinesToSet.remove(item);
            }
        }

        for (int i = 0; i < child.getBoard().length; i++) {
            Integer item = child.getBoard()[i];
            for (int j = i + 1; j < child.getBoard().length; j++) {
                if (item != null) {
                    if (child.getBoard()[i] == child.getBoard()[j]) {
                        duplicatesList.add(item);               //JEZELI MAMY DUPLIKAT TO GO DODAJEMY DO LISTY DUPLIKATOW
                    }
                }
            }
        }

        int i = 0;
        while(!duplicatesList.isEmpty()) {
            Integer item = child.getBoard()[i];
            if (duplicatesList.contains(item) && item != null) {
                if (machinesToSet.isEmpty()) {
                    child.setGeneBoardIndex(i,null);
                } else {
                    Integer set = machinesToSet.get(0);
                    child.setGeneBoardIndex(i, set);
                    machinesToSet.remove(set);
                }
                duplicatesList.remove(item);
            }
            i++;
        }

        i = 0;
        while(!machinesToSet.isEmpty()) {
            Integer item = child.getBoard()[i];
            if (item == null) {
                Integer set = machinesToSet.get(0);
                child.setGeneBoardIndex(i, set);
                machinesToSet.remove(set);
            }
            i++;
        }

        return child;
    }

    public static void mutate(Specimen specimen) {
        int size = specimen.getHeight() * specimen.getWidth();

        for (int i = 0; i < size; i++) {
            double probability = Math.random();
            Integer valueAtIndexI = specimen.getGeneFromBoardIndex(i);
            Random random = new Random();
            if (probability < Constants.MUTATION_PROBABILITY) {
                int indexToSwap = i;
                if (size == specimen.getMachineCount()) {
                    while (indexToSwap == i) {
                        indexToSwap = random.nextInt(0, size);
                    }
                } else {
                    if (valueAtIndexI == null) {
                        ArrayList<Integer> machineIndexes = new ArrayList<>();
                        for (int j = 0; j < size; j++) {
                            if (specimen.getGeneFromBoardIndex(j) != null) {
                                machineIndexes.add(j);
                            }
                        }
                        indexToSwap = machineIndexes.get(random.nextInt(0,machineIndexes.size()));
                    } else {
                        ArrayList<Integer> nullIndexes = new ArrayList<>();
                        for (int j = 0; j < size; j++) {
                            if (specimen.getGeneFromBoardIndex(j) == null) {
                                nullIndexes.add(j);
                            }
                        }
                        indexToSwap = nullIndexes.get(random.nextInt(0,nullIndexes.size()));
                    }
                }
                Integer temp = specimen.getGeneFromBoardIndex(indexToSwap);

                specimen.setGeneBoardIndex(indexToSwap, valueAtIndexI);
                specimen.setGeneBoardIndex(i, temp);
                return;
            }
        }
    }
}


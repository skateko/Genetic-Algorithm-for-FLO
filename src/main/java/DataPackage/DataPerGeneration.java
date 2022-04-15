package DataPackage;

public class DataPerGeneration {
    private int generationNumber;
    private int bestSolutionForGen;
    private int worstSolutionForGen;
    private int averageSolutionForGen;

    public DataPerGeneration(int generationNumber, int bestSolutionForGen, int worstSolutionForGen, int averageSolutionForGen) {
        this.generationNumber = generationNumber;
        this.bestSolutionForGen = bestSolutionForGen;
        this.worstSolutionForGen = worstSolutionForGen;
        this.averageSolutionForGen = averageSolutionForGen;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public void setGenerationNumber(int generationNumber) {
        this.generationNumber = generationNumber;
    }

    public int getBestSolutionForGen() {
        return bestSolutionForGen;
    }

    public void setBestSolutionForGen(int bestSolutionForGen) {
        this.bestSolutionForGen = bestSolutionForGen;
    }

    public int getWorstSolutionForGen() {
        return worstSolutionForGen;
    }

    public void setWorstSolutionForGen(int worstSolutionForGen) {
        this.worstSolutionForGen = worstSolutionForGen;
    }

    public int getAverageSolutionForGen() {
        return averageSolutionForGen;
    }

    public void setAverageSolutionForGen(int averageSolutionForGen) {
        this.averageSolutionForGen = averageSolutionForGen;
    }
}

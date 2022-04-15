package ModelPackage;

import SetupPackage.Constants;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

public class Specimen implements Comparable<Specimen>{
    private Integer[] board;
    private int width;
    private int height;
    private int machineCount;
    private int fitnessValue;

    public Specimen (int width, int height, int machineCount) {
        if (machineCount > width * height) throw new IllegalArgumentException("Too many machines to allocate");
        this.width = width;
        this.height = height;
        this.machineCount = machineCount;
        this.board = new Integer[width*height];

        //Arrays.fill(board, -1);
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);
        for (int i = 0; i < machineCount; i++) {
            this.board[indexes.get(i)] = i;
        }
        fitnessValue = calculateFitness();
    }

    public Specimen(Specimen parent) {
        this.width = parent.width;
        this.height = parent.height;
        this.machineCount = parent.machineCount;
        this.board = parent.board.clone();
        fitnessValue = parent.fitnessValue;
    }

    public Integer getBoardValue(int x, int y){
        if (x >= width || y >= height){
            throw new IllegalArgumentException("Invalid coordinates");
        }
        return board[width*y+x];                            //
    }

    private int getManhattanDistance(int source, int destination) {
        if (source > machineCount || destination > machineCount || source < 0 || destination < 0) {
            throw new IllegalArgumentException("Invalid machine numbers");
        }
        int sourceBoardIndex = -1;
        int destinationBoardIndex = -1;

        for (int i = 0; i < board.length; i++) {
            if (board[i] != null) {
                if (board[i] == source) {
                    sourceBoardIndex = i;
                }
                if (board[i] == destination) {
                    destinationBoardIndex = i;
                }
            }
        }

        if (sourceBoardIndex == -1 || destinationBoardIndex == -1) {
            System.out.println(Arrays.toString(board));
            throw new NoSuchElementException(this.hashCode() + " Coordinates could not be found source:" + source + " destination: " + destination);
        }

        int sourceX = sourceBoardIndex % width;
        int sourceY = sourceBoardIndex / width;
        int destX = destinationBoardIndex % width;
        int destY = destinationBoardIndex / width;
        return Math.abs(sourceX - destX) + Math.abs(sourceY - destY);
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public int calculateFitness() {
        int result = 0;
        for (int i = 0; i < Constants.DATA_FROM_JSON.size(); i++) {
            JSONObject jsonObject = (JSONObject) Constants.DATA_FROM_JSON.get(i);
            int source = ((Long) jsonObject.get(Constants.SOURCE)).intValue();
            int dest = ((Long) jsonObject.get(Constants.DEST)).intValue();
            int cost = ((Long) jsonObject.get(Constants.COST)).intValue();
            int amount = ((Long) jsonObject.get(Constants.AMOUNT)).intValue();
            int manhattan = getManhattanDistance(source, dest);
            result += cost * amount * manhattan;
        }
        return result;
    }

    public Integer[] getBoard() {
        return board;
    }

    public Integer getGeneFromBoardIndex(int index) {
        return board[index];
    }

    public void setGeneBoardIndex(int index, Integer value) {
        board[index] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMachineCount() {
        return machineCount;
    }

    public void recalculateFitness() {
        fitnessValue = calculateFitness();
    }

    public int compareTo(Specimen specimen) {
        return Integer.compare(getFitnessValue(), specimen.getFitnessValue());
    }
}

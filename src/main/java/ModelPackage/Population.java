package ModelPackage;

import java.util.ArrayList;
import java.util.List;

public class Population {
    private List<Specimen> specimens;

    public Population(int specimenQuantity, int width, int height, int machineCount) {
        this.specimens = new ArrayList<>();
        for (int i = 0; i < specimenQuantity; i++) {
            this.specimens.add(new Specimen(width, height, machineCount));
        }
    }

    public List<Specimen> getSpecimens() {
        return specimens;
    }

    public Specimen getSpecimen(int index){
        return specimens.get(index);
    }

    public void setSpecimens(List<Specimen> specimens) {
        this.specimens = specimens;
    }
}

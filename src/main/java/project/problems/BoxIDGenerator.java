package project.problems;

public class BoxIDGenerator {
    private int ID = 0;


    public BoxIDGenerator(int start){
        this.ID = start;
    }
    public int nextID() {
        return ID++;
    }
    public int currID(){
        return ID;
    }

    public BoxIDGenerator copy(){
        return new BoxIDGenerator(currID());
    }

}

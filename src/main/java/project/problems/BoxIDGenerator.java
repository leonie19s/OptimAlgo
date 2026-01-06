package project.problems;

public class BoxIDGenerator {
    private int ID = 0;


    public BoxIDGenerator(int start){
        this.ID = start;
    }
    public int nextID() {
        return ID++;
    }
    public int lastIssuedID(){

        return ID-1;
    }

    public BoxIDGenerator copy(){
        return new BoxIDGenerator(lastIssuedID());
    }

}

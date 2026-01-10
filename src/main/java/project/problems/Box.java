package project.problems;

public class Box {
    public final int L;
    private final int iD;


    public Box(int L, int iD) {
        this.iD = iD;
        this.L = L;
    }

    public int getL(){
        return L;
    }

    public int getID(){
        return iD;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // same reference
        if (obj == null || getClass() != obj.getClass()) return false;
        Box other = (Box) obj;
        return this.getID() == other.getID();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID()); // consistent with equals
    }

    @Override
    public String toString() {
        return "Box-" + getID();
    }


    public Box copy(){
        return new Box(this.L, this.iD);
    }

}


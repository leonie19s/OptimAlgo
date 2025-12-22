package main.java.problems;

public class Placement {
    Box box;
    // describes lower left corner
    int x;
    int y;
    boolean rotated; // implemented as bool so that 2x rotation is original rectangle again

    public Placement(int x, int y){
        this.x = x;
        this.y = y;
        this.rotated = false; // initially not rotated
    }

    public void rotate(){
        // this only rotates, it's up to the calling function to check if
        // the resulting rectangle is within valid bounds
        this.rotated = !this.rotated;
        // we do not implement a change in origin point, so that the rotated rectangle still has the same
        // origin, just height and width are switched
    }
    public int getWidth(Rectangle r){
        return rotated ? r.getHeight() : r.getWidth();
    }

    public int getHeight(Rectangle r){
        return rotated ? r.getHeight() : r.getWidth();
    }

    public boolean isValid(Rectangle r){
        int L = box.L;
        if (x + getWidth(r) > L){
            return false;
        }
        return y + getHeight(r) <= L;
    }

}

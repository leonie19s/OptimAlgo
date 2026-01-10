package project.problems;

public class Placement {
    Box box;
    // describes lower left corner
    int x;
    int y;
    boolean rotated; // implemented as bool so that 2x rotation is original rectangle again

    public Placement(Box box, int x, int y, boolean rotated){
        this.x = x;
        this.y = y;
        this.rotated = rotated;
        this.box = box;
    }
    public Box getBox(){
        return this.box;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void rotate(){
        // this only rotates, it's up to the calling function to check if
        // the resulting rectangle is within valid bounds
        this.rotated = !this.rotated;
        // we do not implement a change in origin point, so that the rotated rectangle still has the same
        // origin, just height and width are switched
    }
    public int getWidth(PackingRectangle r){
        return rotated ? r.getHeight() : r.getWidth();
    }

    public int getHeight(PackingRectangle r){
        return rotated ? r.getWidth() : r.getHeight();
    }

    public boolean isValid(PackingRectangle r){
       return x >= 0 && y >= 0 &&
               x + getWidth(r) <= box.L &&
               y + getHeight(r) <= box.L;}

}

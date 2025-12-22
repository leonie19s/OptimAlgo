package main.java.problems;

import main.java.algorithms.Solution;

import java.util.*;


/**
 * This class represents the current placement of Rectangles.
 */
public class PackingSolution implements Solution {
    private final int boxSize;
    private List<Box> boxes;
    private Map<Rectangle, Placement> placements;
    private int nextBoxID;

    @Override
    public Solution copy() {

        HashMap<Rectangle, Placement> newPlacements = new HashMap<>();
        for (Map.Entry<Rectangle, Placement> entry: placements.entrySet()){
            Rectangle rec = entry.getKey();
            Placement oldPlace = entry.getValue();

            Placement newP = new Placement(oldPlace.getBox(), oldPlace.x, oldPlace.y, oldPlace.rotated);
            newPlacements.put(rec, newP);
        }

        PackingSolution copy = new PackingSolution(boxSize, (ArrayList<Box>) boxes, newPlacements);
        copy.nextBoxID = this.nextBoxID;
        return copy;
    }

    public PackingSolution(int boxSize, ArrayList<Box> boxes, HashMap<Rectangle, Placement> placements ){
        this.boxSize = boxSize;
        this.boxes = boxes;
        this.placements = placements;
        this.nextBoxID = 0; // start at 0
    }

    public Placement getPlacement(Rectangle rect) {
        return placements.get(rect);
    }

    public void setPlacement(Rectangle rect, Placement placement) {
        placements.put(rect, placement);
    }

    /*
    Creates a new box with increasing box id and automatically appends it to the current
    * list of active boxes
     */
    public Box createNewBox(){
        Box box = new Box(boxSize, nextBoxID++);
        boxes.add(box);
        return box;

    }

    public int getNumberOfBoxes(){
        return boxes.size();
    }

    public boolean areRectanglesInSolution(List<Rectangle> rectangles){
        // List<Boxes>
        Set<Integer> boxIds = new HashSet<>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        for (Rectangle rec : rectangles) {
            Placement placement = placements.get(rec);
            if (placement == null || !boxIds.contains(placement.getBox().getID())) {
                return false;
            }
        }
        return true;
    }

    public boolean areRectanglesWithinBoxLength(List<Rectangle> rectangles) {
        for (Rectangle rec : rectangles) {
            Placement placement = placements.get(rec);
            boolean check = placement.isValid(rec);
            if (!check) {
                return false;
            }
        }
        return true;
    }
    //func move rectangle

    // func rotate rectangle
    // func getBox
    // func getBoxes
    // func getNumber of boxes
    // func get placements

}

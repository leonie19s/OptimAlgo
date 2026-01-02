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


    public PackingSolution(int boxSize, ArrayList<Box> boxes, HashMap<Rectangle, Placement> placements ){
        this.boxSize = boxSize;
        this.boxes = boxes;
        this.placements = placements;
        this.nextBoxID = 0; // start at 0
    }

    @Override
    public PackingSolution copy() {

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

    public Map<Rectangle, Placement> getPlacements(){
        return placements;
    }

    public boolean boxExists(int boxID){
        Set<Integer> boxIds = new HashSet<>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        return boxIds.contains(boxID);
    }
    public void addBox(Box box){
        boxes.add(box);
    }
    /*
    First time placement of the rectangles!
     */
    public PackingSolution placeRectangle(Rectangle rec, int boxID, int x, int y, boolean rotated){
        PackingSolution copy = this.copy();
        Box newBox = new Box(boxSize, boxID);
        if (!copy.boxExists(boxID)){
            copy.addBox(newBox);
        }
        Placement placement = new Placement(
                newBox,
                x,
                y,
                rotated
        );
        copy.placements.put(rec, placement);

        return copy;

    }
    public boolean areRectanglesInSolution(Collection<Rectangle> rectangles){
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
    public boolean doesSolutionContainThisRectangle(Rectangle r){
        Set<Integer> boxIds = new HashSet<>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        Placement placement = placements.get(r);
        return placement != null && boxIds.contains(placement.getBox().getID());
    }


    public PackingSolution createNeighborByMove(Rectangle rec, int dx, int dy){
        // copy current solution
        PackingSolution copy = this.copy();
        // get old placement of rec
        Placement p = copy.getPlacement(rec);
        // create new placement and place in copy
        copy.placements.put(
                rec,
                new Placement(
                        p.getBox(),
                        p.x + dx,
                        p.y +dy,
                        p.rotated
                )
        );
        return copy;
    }

    public PackingSolution createNeighborByRotate(Rectangle rec){
        // copy current solution
        PackingSolution copy = this.copy();
        // get old placement of rec
        Placement p = copy.getPlacement(rec);
        // create new placement and place in copy
        copy.placements.put(
                rec,
                new Placement(
                        p.getBox(),
                        p.x ,
                        p.y,
                        !p.rotated
                )
        );
        return copy;
    }

}

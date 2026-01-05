package project.problems;

import project.algorithms.Solution;

import java.util.*;


/**
 * This class represents the current placement of Rectangles.
 */
public class PackingSolution implements Solution {
    private final int boxSize;
    private List<Box> boxes;
    private Map<Rectangle, Placement> placements;
    private BoxIDGenerator IDgenerator;



    public PackingSolution(int boxSize ){
        this.boxSize = boxSize;
        this.boxes = new ArrayList<>();
        this.placements = new HashMap<>();
        this.IDgenerator = new BoxIDGenerator(0); // start at 0
    }


    @Override
    public PackingSolution copy() {

        HashMap<Rectangle, Placement> newPlacements = new HashMap<>();
        PackingSolution copy = new PackingSolution(boxSize);
        for (Map.Entry<Rectangle, Placement> entry: placements.entrySet()){
            Rectangle rec = entry.getKey();
            Placement oldPlace = entry.getValue();

            Placement newP = new Placement(oldPlace.getBox(), oldPlace.x, oldPlace.y, oldPlace.rotated);
            newPlacements.put(rec, newP);
            copy.addBox(oldPlace.getBox());
        }
        copy.setPlacements(newPlacements);

        copy.IDgenerator = this.IDgenerator.copy();
        return copy;
    }

    // ------ Placement related methods -------
    public Placement getPlacement(Rectangle rect) {
        return placements.get(rect);
    }

    public void setPlacements(HashMap<Rectangle, Placement> placements){
        this.placements = placements;
    }

    public void addPlacement(Rectangle rect, Placement placement) {
        placements.put(rect, placement);
    }

    public Map<Rectangle, Placement> getPlacements(){
        return placements;
    }


    // ------- Box related methods ------
    public List<Box> getBoxes(){
        return this.boxes;
    }

    public Box createNewBox()
    {
        Box box = new Box(boxSize, this.IDgenerator.nextID());
        boxes.add(box);
        return box;

    }

    public int getNumberOfBoxes(){
        return boxes.size();
    }

    public boolean boxExists(int boxID){
        Set<Integer> boxIds = new HashSet<>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        return boxIds.contains(boxID);
    }

    public void addBox(Box box){
        if(boxExists(box.getID())){
            return;
        }
        else boxes.add(box);

    }

    public Box getBoxByBoxID(int boxID) throws Exception {
        for (Box box : boxes) {
            if (box.getID() == boxID){
                return box;
            }
        }
        throw new Exception("There is no Box with this Box ID.");
    }


    // --- Rectangle and Solution related methods ----

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


    public List<Rectangle> getRectangleByBoxID(int boxID) throws Exception{
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        Box thisBox = getBoxByBoxID(boxID);
        for (Map.Entry<Rectangle, Placement> entry : this.getPlacements().entrySet())
        {
            Rectangle r = entry.getKey();
            Placement p = entry.getValue();
            // if the box connected with this rectangle-placement pair is the same box given by the ID, save the rec
            if (p.getBox().equals(thisBox))
            {
                rectangles.add(r);
            }

        }
        return rectangles;
    }

}

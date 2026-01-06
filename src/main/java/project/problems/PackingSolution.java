package project.problems;

import project.algorithms.Solution;

import java.util.*;


/**
 * This class represents the current placement of Rectangles.
 */
public class PackingSolution implements Solution<Rectangle> {
    private final int boxSize;
    private List<Box> boxes;
    private Map<Rectangle, Placement> placements;
    private BoxIDGenerator IDgenerator;
    private static final char[] SYMBOLS =
            ("ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                "abcdefghijklmnopqrstuvwxyz" +
                                "0123456789" +
                                "!@#$%^&*+=?-_<>~").toCharArray();




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
    public BoxIDGenerator getGen(){return this.IDgenerator;}
    @Override
    public void applyChange(Rectangle elem) {
        placeFirstPlacementInPlace(elem);
    }



    // ------ Placement related methods -------
    public void placeFirstPlacementInPlace(Rectangle rec){
        Placement bestPlace = findFirstPlacement(rec);
        this.placements.put(rec,bestPlace);
    }
    public Placement findPlacementInBox(Rectangle rec, Box box)
    {
        int boxID = box.getID();
        try
        {
            List<Rectangle> recsOfBox = getRectangleByBoxID(boxID);
            // unrotated
            for (int x = 0; x<= boxSize-rec.getWidth(); x++)
            {
                for (int y = 0; y<= boxSize- rec.getHeight(); y++)
                {
                    Placement placementCandidate1 = new Placement(box, x,y,false);
                    // Placement placementCandidate2 = new Placement(box, x,y,true);
                    boolean conflict1 = false;
                    //boolean conflict2 = false;
                    for (Rectangle recInBox : recsOfBox)
                    {
                        Placement activePlacement = placements.get(recInBox);
                        if (rectangleOverlap(recInBox, rec, activePlacement, placementCandidate1))
                        {
                            conflict1 = true;
                        }
                    }
                    if (!conflict1)
                    {
                        // placement is possible
                        return placementCandidate1;

                    }
                }
            }

            // rotated
            for (int x = 0; x<= boxSize-rec.getHeight(); x++)
            {
                for (int y = 0; y<= boxSize- rec.getWidth(); y++)
                {
                    Placement placementCandidate2 = new Placement(box, x,y,true);
                    boolean conflict = false;

                    for (Rectangle recInBox : recsOfBox)
                    {
                        Placement activePlacement = placements.get(recInBox);
                        if (rectangleOverlap(recInBox, rec, activePlacement, placementCandidate2))
                        {
                            conflict = true;
                        }
                    }
                    if (!conflict)
                    {
                        // placement is possible
                        return placementCandidate2;
                    }
                }
    }} catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }
    public Placement findFirstPlacement(Rectangle rec){
        for (Box box : this.boxes)
        {
            // for each box, get rectangles and placements
            int boxID = box.getID();
            try {
                List<Rectangle> recsOfBox = getRectangleByBoxID(boxID);
                // unrotated
                for (int x = 0; x<= boxSize-rec.getWidth(); x++)
                {
                    for (int y = 0; y<= boxSize- rec.getHeight(); y++)
                    {
                        Placement placementCandidate1 = new Placement(box, x,y,false);
                       // Placement placementCandidate2 = new Placement(box, x,y,true);
                        boolean conflict1 = false;
                        //boolean conflict2 = false;
                        for (Rectangle recInBox : recsOfBox)
                        {
                            Placement activePlacement = placements.get(recInBox);
                            if (rectangleOverlap(recInBox, rec, activePlacement, placementCandidate1))
                            {
                                conflict1 = true;
                            }
                        }
                        if (!conflict1)
                        {
                                // placement is possible
                            return placementCandidate1;

                        }
                    }
                }

                // rotated
                for (int x = 0; x<= boxSize-rec.getHeight(); x++)
                {
                    for (int y = 0; y<= boxSize- rec.getWidth(); y++)
                    {
                        Placement placementCandidate2 = new Placement(box, x,y,true);
                        boolean conflict = false;

                        for (Rectangle recInBox : recsOfBox)
                        {
                            Placement activePlacement = placements.get(recInBox);
                            if (rectangleOverlap(recInBox, rec, activePlacement, placementCandidate2))
                            {
                                conflict = true;
                            }
                        }
                        if (!conflict)
                        {
                            // placement is possible
                            return placementCandidate2;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        // went through all boxes, didnt find a valid placement
        // create new box, place at (0,)
        Box newBox = createNewBox();
        return new Placement(newBox,0,0,false);
    }
    public Placement getPlacement(Rectangle rect) {
        return placements.get(rect);
    }

    public void setPlacements(Map<Rectangle, Placement> placements){
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

    public List<Integer> getBoxIds(){
        List<Integer> boxIds = new ArrayList<Integer>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        return  boxIds;
    }

    public Box getBoxOfRectangle(Rectangle rec){
        Placement p = placements.get(rec);
        return p.getBox();
    }
    public void addBox(Box box){
        if(boxExists(box.getID())){
            System.out.println("weird, this box already exists.");
            System.out.println(box.getID());
            return;
        }
        else boxes.add(box);

    }
    public void removeBox(Box box){
        if(!boxExists(box.getID())){
            System.out.println("tried to delete non-existing box lol");
            System.out.println(box.getID());
            return;
        }
        else boxes.remove(box);
    }

    public Box getBoxByBoxID(int boxID) {
        for (Box box : boxes) {
            if (box.getID() == boxID){
                return box;
            }
        }
        System.out.println("There is no Box with this Box ID.");
        System.out.println(boxID);
        return null;
    }


    // --- Rectangle and Solution related methods ----

    /*
    First time placement of the rectangles!
     */
    public PackingSolution placeRectangleInCopy(Rectangle rec, int boxID, int x, int y, boolean rotated){
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

    public PackingSolution createNeighborByBoxSwitch(Rectangle rec, int targetBoxID, boolean fallbackRandom, Random random){
        PackingSolution copy = this.copy();
        Box targetBox = getBoxByBoxID(targetBoxID);
        assert targetBox != null;
        if (!fallbackRandom) {
            Placement placement = copy.findPlacementInBox(rec, targetBox);
            if (placement != null) {
                copy.placements.put(rec, placement);
                return copy;
            }

            // fallback to random placement
            return createNeighborByBoxSwitch(rec, targetBoxID, true, random);
        }

        Placement oldPlacement = copy.placements.get(rec);

        int maxX = copy.boxSize - oldPlacement.getWidth(rec);
        int maxY = copy.boxSize - oldPlacement.getHeight(rec);

        int x = random.nextInt(maxX);
        int y = random.nextInt(maxY);
        boolean rotated = random.nextBoolean();

        Placement randomPlacement = new Placement(targetBox, x, y, rotated);
        copy.placements.put(rec, randomPlacement);
        removeBoxIfEmpty(targetBox);
        return copy;
    }
    private boolean removeBoxIfEmpty(Box box){
        List<Rectangle> activeRecs = getRectangleByBoxID(box.getID());
        if (activeRecs.isEmpty()){
            this.boxes.remove(box);
            return true;
        }
        return false;
    }
    public Rectangle getRandomRectangle(Random random){
        int index = random.nextInt(this.getNumberOfBoxes());
        List<Rectangle> rectangles = new ArrayList<Rectangle>(this.placements.keySet());
        return rectangles.get(index);

    }
    public List<Rectangle> getRectangleByBoxID(int boxID) {
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
    public static boolean rectangleOverlap(Rectangle r1,Rectangle r2,Placement p1,Placement p2){
        int x1 = p1.x;
        int y1 = p1.y;
        int w1 = p1.getWidth(r1);
        int h1 = p1.getHeight(r1);

        int x2 = p2.x;
        int y2 = p2.y;
        int w2 = p2.getWidth(r2);
        int h2 = p2.getHeight(r2);
        return overlaps(x1,y1,w1,h1,x2,y2,w2,h2);
    }
    public static boolean overlaps(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2){
        // offen disjunkt, ansonsten <= und >=
        return x1 < x2 + w2 &&
                x1 + w1 > x2 &&
                y1 < y2 + h2 &&
                y1 + h1 > y2;
    }
    @Override
    public String toString() {
        StringBuilder pr = new StringBuilder();
        for (Box box : this.getBoxes()) {
            pr.append("Box----").append(box.getID());
        }
            return pr.toString();

    }

    @Override
    public void printSolution(Solution<Rectangle> sol)
    {
        PackingSolution solution = (PackingSolution) sol;
        int size = solution.boxSize;

        // assign a unique character to each rectangle
        Map<Rectangle, Character> rectSymbols = new HashMap<>();
        int symbolIndex = 0;

        for (Rectangle r : solution.getPlacements().keySet()) {
            if (symbolIndex >= SYMBOLS.length) {
                throw new IllegalStateException(
                        "Too many rectangles to visualize (" + symbolIndex + ")"
                );
            }
            rectSymbols.put(r, SYMBOLS[symbolIndex++]);
        }

        int boxIndex = 0;
        for (Box box : solution.getBoxes()) {
            System.out.println(box.getID());
            // create empty grid
            char[][] grid = new char[size][size];
            for (int y = 0; y < size; y++) {
                Arrays.fill(grid[y], '.');
            }

            // fill grid with rectangles placed in this box
            for (Map.Entry<Rectangle, Placement> entry : solution.getPlacements().entrySet()) {
                Rectangle r = entry.getKey();
                Placement p = entry.getValue();

                if (!p.getBox().equals(box)) continue;

                int w = p.getWidth(r);
                int h = p.getHeight(r);

                char symbol = rectSymbols.get(r);

                for (int dx = 0; dx < w; dx++) {
                    for (int dy = 0; dy < h; dy++) {
                        int x = p.x + dx;
                        int y = p.y + dy;
                        grid[y][x] = symbol;
                    }
                }
            }

            // print box
            System.out.println("Box " + boxIndex++ + ":");
            for (int y = size - 1; y >= 0; y--) {
                for (int x = 0; x < size; x++) {
                    System.out.print(grid[y][x]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }


}

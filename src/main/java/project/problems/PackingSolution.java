package project.problems;

import project.algorithms.Solution;

import java.util.*;


/**
 * This class represents the current placement of Rectangles.
 */
public class PackingSolution implements Solution<PackingRectangle> {
    private final int boxSize;
    private List<Box> boxes;
    private Map<PackingRectangle, Placement> placements;
    private BoxIDGenerator IDgenerator;
    private PackingRectangle lastRec;
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
        this.lastRec = null;
    }

    public int getBoxSize(){
        return this.boxSize;
    }
    @Override
    public PackingSolution copy() {

        PackingSolution copy = new PackingSolution(this.boxSize);

        // 1️⃣ Copy boxes
        Map<Integer, Box> boxMap = new HashMap<>();
        for (Box b : this.boxes) {
            Box bCopy = b.copy();
            boxMap.put(b.getID(), bCopy);
            copy.addBox(bCopy);
        }

        // 2️⃣ Copy rectangles + placements
        Map<PackingRectangle, Placement> newPlacements = new HashMap<>();

        for (Map.Entry<PackingRectangle, Placement> e : this.placements.entrySet()) {

            PackingRectangle oldRec = e.getKey();
            Placement oldPl = e.getValue();


            Box boxCopy = boxMap.get(oldPl.getBox().getID());

            Placement plCopy = new Placement(
                    boxCopy,
                    oldPl.x,
                    oldPl.y,
                    oldPl.rotated
            );

            newPlacements.put(oldRec, plCopy);
        }

        copy.setPlacements(newPlacements);

        copy.IDgenerator = this.IDgenerator.copy();
        copy.lastRec = this.lastRec == null ? null : this.lastRec.copy();

        return copy;
    }
    public BoxIDGenerator getGen(){return this.IDgenerator;}
    @Override
    public void applyChange(PackingRectangle elem) {

        placeFirstPlacementInPlace(elem);
        this.lastRec = elem;
    }

    public PackingRectangle getLastRec(){
        return this.lastRec;
    }
    public void setLastRec(PackingRectangle rec){
        this.lastRec = rec;
    }


    // ------ Placement related methods -------
    public void placeFirstPlacementInPlace(PackingRectangle rec){
        Placement bestPlace = findFirstPlacement(rec);
        this.placements.put(rec,bestPlace);
    }
    public Placement findPlacementInBox(PackingRectangle rec, Box box)
    {
        int boxID = box.getID();
        try
        {
            List<PackingRectangle> recsOfBox = getRectangleByBoxID(boxID);
            // unrotated
            for (int x = 0; x<= boxSize-rec.getWidth(); x++)
            {
                for (int y = 0; y<= boxSize- rec.getHeight(); y++)
                {
                    Placement placementCandidate1 = new Placement(box, x,y,false);
                    // Placement placementCandidate2 = new Placement(box, x,y,true);
                    boolean conflict1 = false;
                    //boolean conflict2 = false;
                    for (PackingRectangle recInBox : recsOfBox)
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

                    for (PackingRectangle recInBox : recsOfBox)
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
    public Placement findFirstPlacement(PackingRectangle rec){
        for (Box box : this.boxes)
        {
            // for each box, get rectangles and placements
            int boxID = box.getID();
            try {
                List<PackingRectangle> recsOfBox = getRectangleByBoxID(boxID);
                // unrotated
                for (int x = 0; x<= boxSize-rec.getWidth(); x++)
                {
                    for (int y = 0; y<= boxSize- rec.getHeight(); y++)
                    {
                        Placement placementCandidate1 = new Placement(box, x,y,false);
                       // Placement placementCandidate2 = new Placement(box, x,y,true);
                        boolean conflict1 = false;
                        //boolean conflict2 = false;
                        for (PackingRectangle recInBox : recsOfBox)
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

                        for (PackingRectangle recInBox : recsOfBox)
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
    public Placement getPlacement(PackingRectangle rect) {
        return placements.get(rect);
    }

    public void setPlacements(Map<PackingRectangle, Placement> placements){
        this.placements = placements;
    }

    public void addPlacement(PackingRectangle rect, Placement placement) {
        placements.put(rect, placement);
    }

    public Map<PackingRectangle, Placement> getPlacements(){
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

    public List<Integer> getBoxIds(PackingSolution sol){
        List<Integer> boxIds = new ArrayList<Integer>();
        for (Box box : sol.boxes) {
            boxIds.add(box.getID());
        }
        return  boxIds;
    }

    public Box getBoxOfRectangle(PackingRectangle rec){
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
    public PackingSolution placeRectangleInCopy(PackingRectangle rec, int boxID, int x, int y, boolean rotated){
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
    public boolean areRectanglesInSolution(Collection<PackingRectangle> packingRectangles){
        // List<Boxes>
        Set<Integer> boxIds = new HashSet<>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        for (PackingRectangle rec : packingRectangles) {
            Placement placement = placements.get(rec);
            if (placement == null || !boxIds.contains(placement.getBox().getID())) {
                return false;
            }
        }
        return true;
    }
    public boolean doesSolutionContainThisRectangle(PackingRectangle r){
        Set<Integer> boxIds = new HashSet<>();
        for (Box box : boxes) {
            boxIds.add(box.getID());
        }
        Placement placement = placements.get(r);
        return placement != null && boxIds.contains(placement.getBox().getID());
    }


    public PackingSolution createNeighborByMove(PackingRectangle rec, int dx, int dy){
        // copy current solution

        PackingSolution copy = this.copy();
        copy.setLastRec(rec);
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

    public PackingSolution createNeighborByRotate(PackingRectangle rec){
        // copy current solution
        PackingSolution copy = this.copy();
        copy.setLastRec(rec);
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

    public PackingSolution createNeighborByBoxSwitch(PackingRectangle rec,  boolean tryValid, Random random){
        PackingSolution copy = this.copy();
        copy.setLastRec(rec);
        List<Box> boxes = copy.getBoxes();
        int idx = random.nextInt(boxes.size());

        Box targetBox = boxes.get(idx);
        if(boxes.size()==2){
            Box currentBox = copy.placements.get(rec).getBox();
            // pick the other box
            targetBox = (boxes.get(0).getID() == currentBox.getID()) ? boxes.get(1) : boxes.get(0);
        }


        if (tryValid) {
            Placement placement = copy.findPlacementInBox(rec, targetBox);
            if (placement != null) {
                copy.placements.put(rec, placement);
                return copy;
            }

        }

        Placement oldPlacement = copy.placements.get(rec);
        boolean rotated = random.nextBoolean();
        int width = oldPlacement.getWidth(rec);
        int height = oldPlacement.getHeight(rec);
        if(rotated){
            int heightCopy = height;
            height = width;
            width=heightCopy;
        }
        int maxX = copy.boxSize - width;
        int maxY = copy.boxSize - height;

        int x = random.nextInt(maxX);
        int y = random.nextInt(maxY);



        Placement randomPlacement = new Placement(targetBox, x, y, rotated);
        copy.placements.put(rec, randomPlacement);
        return copy;
    }
    public boolean clearEmptyBoxes(PackingSolution s){
        List<Box> recList = s.boxes;
        List<Box> boxesToRemove = new ArrayList<>();
        for (Box box : recList){
            boolean isActive=false;
            for (Map.Entry<PackingRectangle, Placement> e : s.placements.entrySet())
            {

                PackingRectangle r = e.getKey();
                Placement p = e.getValue();
                if (p.getBox().getID() == box.getID())
                {
                    isActive=true;
                    break;
                }

            }
            if (!isActive){
                boxesToRemove.add(box);
            }
        }

        return recList.removeAll(boxesToRemove);
    }
    public PackingRectangle getRandomRectangle(Random random){

        List<PackingRectangle> packingRectangles = new ArrayList<PackingRectangle>(this.placements.keySet());
        int index = random.nextInt(packingRectangles.size());
        return packingRectangles.get(index);

    }
    public List<PackingRectangle> getRectangleByBoxID(int boxID) {
        List<PackingRectangle> packingRectangles = new ArrayList<PackingRectangle>();
        Box thisBox = getBoxByBoxID(boxID);
        for (Map.Entry<PackingRectangle, Placement> entry : this.getPlacements().entrySet())
        {
            PackingRectangle r = entry.getKey();
            Placement p = entry.getValue();
            // if the box connected with this rectangle-placement pair is the same box given by the ID, save the rec
            if (p.getBox().getID()==thisBox.getID())
            {
                packingRectangles.add(r);
            }

        }
        return packingRectangles;
    }
    public static boolean rectangleOverlap(PackingRectangle r1, PackingRectangle r2, Placement p1, Placement p2){
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
    public void printSolution(Solution<PackingRectangle> sol)
    {
        PackingSolution solution = (PackingSolution) sol;
        int size = solution.boxSize;

        // assign a unique character to each rectangle
        Map<PackingRectangle, Character> rectSymbols = new HashMap<>();
        int symbolIndex = 0;

        for (PackingRectangle r : solution.getPlacements().keySet()) {
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
            for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
                PackingRectangle r = entry.getKey();
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

    @Override
    public List<PackingRectangle> getPerm() {
        List<PackingRectangle> perm = new ArrayList<>();

        for (Map.Entry<PackingRectangle, Placement> entry : this.getPlacements().entrySet()) {
            PackingRectangle r = entry.getKey();
            perm.add(r);
        }
        return perm;
    }

    @Override
    public void applyPerm(List<PackingRectangle> perm) {
        for (PackingRectangle rec : perm){
            applyChange(rec);
        }
    }

    public static Map<Box, Double> computeCoverage(PackingSolution solution, boolean allowOverlap) {
        Map<Box, Double> coverageMap = new HashMap<>();
        int boxSize = solution.getBoxSize(); // assume square boxes

        for (Box box : solution.getBoxes()) {

            if (!allowOverlap) {
                // simple sum of rectangle areas (faster)
                int totalArea = 0;
                for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
                    Placement p = entry.getValue();
                    if (!p.getBox().equals(box)) continue;

                    int w = p.getWidth(entry.getKey());
                    int h = p.getHeight(entry.getKey());
                    totalArea += w * h;
                }
                double fraction = Math.min(1.0, (double) totalArea / (boxSize * boxSize));
                coverageMap.put(box, fraction);
            } else {
                // exact overlap-aware calculation using sweep-line
                Map<Integer, List<int[]>> rowIntervals = new HashMap<>();

                for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
                    Placement p = entry.getValue();
                    if (!p.getBox().equals(box)) continue;

                    int w = p.getWidth(entry.getKey());
                    int h = p.getHeight(entry.getKey());
                    int x0 = p.getX();
                    int y0 = p.getY();

                    for (int y = y0; y < y0 + h; y++) {
                        rowIntervals.computeIfAbsent(y, k -> new ArrayList<>())
                                .add(new int[]{x0, x0 + w});
                    }
                }

                int coveredCells = 0;

                for (int y : rowIntervals.keySet()) {
                    List<int[]> intervals = rowIntervals.get(y);
                    if (intervals.isEmpty()) continue;

                    intervals.sort(Comparator.comparingInt(a -> a[0]));

                    int currentStart = intervals.get(0)[0];
                    int currentEnd = intervals.get(0)[1];

                    for (int i = 1; i < intervals.size(); i++) {
                        int[] interval = intervals.get(i);
                        if (interval[0] <= currentEnd) {
                            currentEnd = Math.max(currentEnd, interval[1]);
                        } else {
                            coveredCells += currentEnd - currentStart;
                            currentStart = interval[0];
                            currentEnd = interval[1];
                        }
                    }
                    coveredCells += currentEnd - currentStart;
                }

                double fraction = (double) coveredCells / (boxSize * boxSize);
                coverageMap.put(box, fraction);
            }
        }

        return coverageMap;
    }

    /**
     * Total coverage across all boxes
     */
    public static double totalCoverage(Map<Box, Double> coverageMap) {
        return coverageMap.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Compare total coverage between two coverage maps
     */
    public static double compareTotalCoverage(Map<Box, Double> coverage1, Map<Box, Double> coverage2) {
        return totalCoverage(coverage2) - totalCoverage(coverage1);
    }

    public List<PackingRectangle> getPermutation(){
        return null;
    }

    public PackingSolution copyEmpty(){
        return new PackingSolution(this.boxSize);

    }
    public boolean isEmpty(){
        return this.IDgenerator.lastIssuedID() == -1;
    }
    public PackingSolution createNeighborFromPermutation(List<PackingRectangle> perm){
        PackingSolution neighbor = this.copyEmpty();

        if (!neighbor.isEmpty()){
            return null;
        }

        for (PackingRectangle rec : perm){
            neighbor.placeFirstPlacementInPlace(rec);
        }

        return neighbor;

    }

}

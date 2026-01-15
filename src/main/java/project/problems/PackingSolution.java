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



    private List<PackingRectangle> allRectangles;
    private double totalOverlap = 0;

    public PackingSolution(int boxSize ){
        this.boxSize = boxSize;
        this.boxes = new ArrayList<>();
        this.placements = new HashMap<>();
        this.IDgenerator = new BoxIDGenerator(0); // start at 0
        this.lastRec = null;
        this.allRectangles = new ArrayList<>();

    }
    public void setAllRectangles(List<PackingRectangle> recs){
        this.allRectangles = recs;
    }
    public List<PackingRectangle> getAllRectangles(){
        return this.allRectangles;
    }
    public int getBoxSize(){
        return this.boxSize;
    }
    @Override
    public PackingSolution copy() {

        PackingSolution copy = new PackingSolution(this.boxSize);
        Map<Integer, Box> boxMap = new HashMap<>();
        for (Box b : this.boxes) {
            Box bCopy = b.copy();
            boxMap.put(b.getID(), bCopy);
            copy.addBox(bCopy);
        }

        if (!this.allRectangles.isEmpty()){
            copy.allRectangles = new ArrayList<>(this.allRectangles);
        }

        //
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

    public Placement findPlacmentInBoxOverlap( PackingRectangle rec,
                                               Box box,
                                               PackingSolution sol,
                                               boolean rotate,
                                               Random rng){

        int boxSize = sol.boxSize;
        int w = rotate ? rec.getHeight() : rec.getWidth();
        int h = rotate ? rec.getWidth()  : rec.getHeight();
        int maxX = boxSize - w;
        int maxY = boxSize - h;
        int x = rng.nextInt(maxX + 1);
        int y = rng.nextInt(maxY + 1);

        return new Placement(box, x, y, rotate);
    }
    public Placement findPlacementInBox(PackingRectangle rec, Box box, PackingSolution sol, boolean rotate)
    {
        double area = PackingSolution.computeStatsPerBox(sol,false,box, true);
        double freeArea = box.getL()* box.getL() -area;
        if (freeArea < rec.getWidth()*rec.getHeight()){
            return null;
        }
        int boxID = box.getID();

            List<PackingRectangle> recsOfBox = sol.getRectangleByBoxID(boxID);
            // unrotated
            for (int x = 0; x<= sol.boxSize-rec.getWidth(); x++)
            {
                for (int y = 0; y<= sol.boxSize- rec.getHeight(); y++)
                {
                    int tempy = y;
                    int tempx = x;
                    if (rotate){
                        tempy = x;
                        tempx = y;
                    }
                    Placement placementCandidate1 = new Placement(box, tempx,tempy,rotate);
                    // Placement placementCandidate2 = new Placement(box, x,y,true);
                    boolean conflict1 = false;
                    //boolean conflict2 = false;
                    for (PackingRectangle recInBox : recsOfBox)
                    {
                        Placement activePlacement = sol.placements.get(recInBox);
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

        return null;
    }

    public Placement findFirstPlacement(PackingRectangle rec)
    {
        Placement candidate;
        for (Box box : this.boxes)
        {
            candidate = findPlacementInBox(rec, box, this, false);
            if (candidate != null)
            {

                return candidate;
            } else
            {
                candidate = findPlacementInBox(rec, box, this, true);
                if (candidate != null)
                {

                    return candidate;
                }
            }
        }
        // didnt find a box at all
        Box newBox = createNewBox();

        return new Placement(newBox, 0, 0, false);

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
        assert 1==2;
        System.out.println(boxID);
        return null;
    }


    // --- Rectangle and Solution related methods ----


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





    public PackingSolution createNeighborByBoxSwitch(PackingRectangle rec,  boolean doFlip, Random random){
        List<Box> boxes = this.getBoxes();
        int idx = random.nextInt(boxes.size());

        Box targetBox = boxes.get(idx);
        if (this.getBoxOfRectangle(rec).getID() == targetBox.getID()){
            return null;
        }

        if(boxes.size()==2){
            Box currentBox = this.placements.get(rec).getBox();
            // pick the other box
            targetBox = (boxes.get(0).getID() == currentBox.getID()) ? boxes.get(1) : boxes.get(0);
        }

        Placement placement = this.findPlacementInBox(rec, targetBox, this, doFlip);


        if (placement != null) {
            PackingSolution copy = this.copy();
            copy.setLastRec(rec);
            targetBox = copy.boxes.get(idx);
            placement.setBox(targetBox);
            Box src = copy.getBoxOfRectangle(rec);
            copy.placements.put(rec, placement);
            //clearEmptyBoxes(copy);
            clearSourceBox(copy,src);

            return copy;
        }

        return null;
    }
    public void clearSourceBox(PackingSolution s, Box source){
        if (s.getRectangleByBoxID(source.getID()).isEmpty()){
            s.removeBox(source);
        }
    }
    public void clearEmptyBoxes(PackingSolution s){

        List<Integer> boxIDsToRemove = new ArrayList<>(s.getBoxIds(s));

        for (Map.Entry<PackingRectangle, Placement> e : s.placements.entrySet())
        {
            boxIDsToRemove.remove((Integer)e.getValue().getBox().getID());
        }
        for (Integer i : boxIDsToRemove) {
            s.removeBox(s.getBoxByBoxID(i));
        }

    }

    public static Map<Box, Integer> getNumberActiveRectangles(PackingSolution sol){
        Map<Box,Integer> nRecsPerBox = new HashMap<>();
        for (Box box : sol.boxes){
            List<PackingRectangle> recs = sol.getRectangleByBoxID(box.getID());
            nRecsPerBox.put(box,recs.size());
        }
        return  nRecsPerBox;
    }

    public double getMaxOverlapInBox(PackingSolution sol){
        List<PackingRectangle> recsInBox = sol.getRectangleByBoxID(0);
        double maxOverlap = 0.0;
        int n = recsInBox.size();
        for (int i = 0; i < n; i++) {
            PackingRectangle r1 = recsInBox.get(i);
            Placement p1 = sol.placements.get(r1);
            for (int j = i + 1; j < n; j++) {
                PackingRectangle r2 = recsInBox.get(j);
                Placement p2 = sol.placements.get(r2);
                double overlap = PackingSolution.rectangleOverlapRatio(r1,r2,p1,p2);
                if (overlap == 1.0){
                    return 1.0;
                }
                if (overlap > maxOverlap){
                    maxOverlap = overlap;
                }
            }
        }
        return maxOverlap;
    }
    public PackingRectangle getRandomRectangle(Random random){
        List<PackingRectangle> packingRectangles = new ArrayList<PackingRectangle>(this.placements.keySet());
        int index = random.nextInt(packingRectangles.size());
        return packingRectangles.get(index);

    }
    public List<PackingRectangle> getRectangleByBoxID(int boxID) {
        List<PackingRectangle> packingRectangles = new ArrayList<PackingRectangle>();
        Box thisBox = getBoxByBoxID(boxID);
        if (thisBox==null)return null;
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


    public static double computeStatsPerBox(PackingSolution solution, boolean allowOverlap, Box targetBox, boolean area) {

        int boxSize = solution.getBoxSize(); // assume square boxes

            if (!allowOverlap)
            {
                // simple sum of rectangle areas (faster)
                int totalArea = 0;
                for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
                    Placement p = entry.getValue();
                    if (!p.getBox().equals(targetBox)) continue;

                    int w = p.getWidth(entry.getKey());
                    int h = p.getHeight(entry.getKey());
                    totalArea += w * h;
                }
                if (area){
                    return totalArea;
                }
                double fraction = Math.min(1.0, (double) totalArea / (boxSize * boxSize));
                return fraction;
            } else
            {
                // exact overlap-aware calculation using sweep-line
                Map<Integer, List<int[]>> rowIntervals = new HashMap<>();

                for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
                    Placement p = entry.getValue();
                    if (!p.getBox().equals(targetBox)) continue;

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
                return fraction;
            }
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


    public PackingSolution createNeighborFromPermutation(List<PackingRectangle> perm){
        PackingSolution neighbor = this.copyEmpty();

        for (PackingRectangle rec : perm){
            neighbor.placeFirstPlacementInPlace(rec);
        }
        return neighbor;

    }
    public PackingSolution createNeighborByNewBox(PackingRectangle rec,  boolean doFlip, Random random){
        PackingSolution copy = this.copy();
        copy.setLastRec(rec);
        int maxX = doFlip ? copy.boxSize - rec.getHeight() : copy.boxSize - rec.getWidth();
        int maxY  = doFlip ? copy.boxSize - rec.getWidth() : copy.boxSize - rec.getHeight();
        Box box = copy.createNewBox();
        Placement newPlacement = new Placement(box, random.nextInt(maxX),random.nextInt(maxY),doFlip );
        copy.placements.put(rec, newPlacement);
        return copy;
    }
    public PackingSolution createNeighborFromBoxSwitchOverlap(PackingRectangle rec,  boolean doFlip, Random random){
        List<Box> boxes = this.getBoxes();
        int idx = random.nextInt(boxes.size());

        Box targetBox = boxes.get(idx);
        if (this.getBoxOfRectangle(rec).getID() == targetBox.getID()){
            return null;
        }

        if(boxes.size()==2){
            Box currentBox = this.placements.get(rec).getBox();
            // pick the other box
            targetBox = (boxes.get(0).getID() == currentBox.getID()) ? boxes.get(1) : boxes.get(0);
        }

        Placement placement = this.findPlacmentInBoxOverlap(rec, targetBox, this, doFlip, random);
        PackingSolution copy = this.copy();
        copy.setLastRec(rec);
        targetBox = copy.boxes.get(idx);
        placement.setBox(targetBox);
        Box src = copy.getBoxOfRectangle(rec);
        copy.placements.put(rec, placement);
        clearSourceBox(copy,src);

        return copy;



    }
    public PackingSolution createNeighborByPlacement(PackingRectangle rec, PackingSolution sol){

        PackingSolution copy = sol.copy();
        assert (sol.getGen().lastIssuedID() == copy.getGen().lastIssuedID());

        //copy.placements.remove(rec);
        Placement placement = copy.findFirstPlacement(rec);


        //copy.updateOverlap(copy,placement, oldPlacement,rec);
        copy.placements.put(rec, placement);
        copy.setLastRec(rec);
        return copy;
    }


    public static double getOverlapPerSolution(PackingSolution sol) {
        List<PackingRectangle> list = sol.allRectangles;
        int n = sol.allRectangles.size();
        double sum = 0;
        int len = 0;
        for (int i = 0; i < n; i++) {
            PackingRectangle ri = list.get(i);
            Placement pi = sol.placements.get(ri);
            for (int j = i+1; j < n; j++) {
                PackingRectangle rj = list.get(j);
                Placement pj = sol.placements.get(rj);
                double value = PackingSolution.rectangleOverlapRatio(ri,rj,pi,pj);
                if ( value ==0)continue;
                sum += value;
                len++;
            }
        }
        if(len ==0)return 0;
        return sum/len;
    }


    public static double  rectangleOverlapRatio(PackingRectangle r1, PackingRectangle r2,
                                       Placement p1, Placement p2) {
        /*
        die Überlappung zweier Rechtecke ist dabei die gemeinsame
        Fläche geteilt durch das Maximum der beiden Rechteckflächen.
         */
        if (p1.getBox().getID() != p2.getBox().getID()) return 0;
        double x1 = p1.getX();
        double y1 = p1.getY();
        double w1 = p1.getWidth(r1);
        double h1 = p1.getHeight(r1);

        double x2 = p2.getX();
        double y2 = p2.getY();
        double w2 = p2.getWidth(r2);
        double h2 = p2.getHeight(r2);

        double overlapWidth = Math.min(x1 + w1, x2 + w2) - Math.max(x1, x2);
        double overlapHeight = Math.min(y1 + h1, y2 + h2) - Math.max(y1, y2);

        if (overlapWidth <= 0 || overlapHeight <= 0) {
            return 0.0; // no overlap
        }

        double overlap_area = overlapWidth * overlapHeight;
        double max_area = Math.max((w1*h1),(w2*h2));
        return overlap_area/max_area;
    }
    public static List<PackingRectangle> getOnlyUnfrozenRectangles(PackingSolution sol){
        // get all rectangles
        List<PackingRectangle> allRecs = new ArrayList<>();
        List<Integer> frozenBoxIds = new ArrayList<>();
        for (Map.Entry<PackingRectangle, Placement> entry : sol.getPlacements().entrySet()) {
            PackingRectangle r = entry.getKey();
            allRecs.add(r);
        }
        List<Box> allBoxes = sol.getBoxes();
        for (Box box : allBoxes){
            if(PackingSolution.computeStatsPerBox(sol, false,box, false) > 0.95){
                frozenBoxIds.add(box.getID());
            }
        }

        allRecs.removeIf(rec -> frozenBoxIds.contains(sol.getPlacement(rec).getBox().getID()));
        return allRecs;
    }

}

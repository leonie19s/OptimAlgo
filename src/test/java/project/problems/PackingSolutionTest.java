package project.problems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PackingSolutionTest {
    int boxSize = 15;

    private PackingSolution solution;
    private Box box1;
    private Box box2;
    private PackingRectangle rect1;
    private PackingRectangle rect2;
    private PackingRectangle rect3;

    @BeforeEach
    void setUp() {
        solution = new PackingSolution(boxSize);

        // Create some boxes
        box1 = solution.createNewBox() ;// 0
        box2 = solution.createNewBox(); // 1

        // Create some rectangles
        rect1 = new PackingRectangle(2, 3);
        rect2 = new PackingRectangle(4, 5);
        rect3 = new PackingRectangle(6, 7);

        // Set up placements
        Map<PackingRectangle, Placement> placements = new HashMap<>();
        placements.put(rect1, new Placement(box1, 0, 0, false));
        placements.put(rect2, new Placement(box1, 1, 1, true));
        placements.put(rect3, new Placement(box2, 2, 2, false));
        solution.setPlacements(placements);  // assuming a setter

    }
    boolean samePlacement(Placement p1, Placement p2){
        if (p1.x != p2.x){
            return false;
        }
        if (p1.y != p2.y){
            return false;
        }
        return p1.rotated == p2.rotated;
    }

    @Test
    void testGetRectangleByBoxID() {
        List<PackingRectangle> box1Rects = solution.getRectangleByBoxID(0);
        List<PackingRectangle> box2Rects = solution.getRectangleByBoxID(1);
        List<PackingRectangle> box3Rects = solution.getRectangleByBoxID(3); // non-existent box

        // Check box1 returns the correct rectangles
        assertEquals(2, box1Rects.size());
        assertTrue(box1Rects.contains(rect1));
        assertTrue(box1Rects.contains(rect2));

        // Check box2 returns the correct rectangle
        assertEquals(1, box2Rects.size());
        assertTrue(box2Rects.contains(rect3));

        // Non-existent box should return empty list
        assertTrue(box3Rects.isEmpty());
    }

    @Test
    void copyTest() {
        PackingSolution copy = solution.copy();

        // Ensure the copy is a different object
        assertNotSame(solution, copy);

        // Placements should be the same (content-wise)
        assertTrue(samePlacement(solution.getPlacement(rect1), copy.getPlacement(rect1)));
        assertTrue(samePlacement(solution.getPlacement(rect2), copy.getPlacement(rect2)));

        // Boxes should be equal
        assertEquals(solution.getBoxes(), copy.getBoxes());

        // Original placement object matches copy's placement content
        assertTrue(samePlacement(new Placement(box1, 0, 0, false),
                copy.getPlacement(rect1)));
    }

    @Test
    void incrementalBoxIDsTest() {
        Box lastBox = null;

        // Create multiple boxes
        for (int i = 0; i < 10; i++) {

            lastBox = solution.createNewBox();
            System.out.println(solution.getGen().lastIssuedID());
        }

        // Check the current ID
        assertEquals(11, solution.getGen().lastIssuedID());

        // Remove last box and create a new one
        solution.removeBox(lastBox);
        Box newBox = solution.createNewBox();

        // ID should continue incrementing
        assertEquals(12, solution.getGen().lastIssuedID());
    }
}

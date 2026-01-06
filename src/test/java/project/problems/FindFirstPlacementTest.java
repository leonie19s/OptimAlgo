package project.problems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindFirstPlacementTest {

    private PackingSolution solution;
    private int boxSize = 10;

    @BeforeEach
    void setUp() {
        solution = new PackingSolution(boxSize);

        // Add one box initially
        Box box = solution.createNewBox();
    }

    @Test
    void testFindFirstPlacement_NoConflict() {
        Rectangle rec = new Rectangle(2, 3);

        // Should place in the existing box at (0,0) since it's empty
        Placement placement = solution.findFirstPlacement(rec);

        assertNotNull(placement);
        assertEquals(0, placement.x);
        assertEquals(0, placement.y);
        assertFalse(placement.rotated);
        assertTrue(solution.getBoxes().contains(placement.getBox()));
    }

    @Test
    void testFindFirstPlacement_WithConflict() {
        // Add a rectangle that fills the top-left corner
        Rectangle existing = new Rectangle(2, 3);
        Placement existingPlacement = new Placement(solution.getBoxes().get(0), 0, 0, false);
        solution.addPlacement(existing, existingPlacement);

        Rectangle newRec = new Rectangle(2, 3);
        Placement placement = solution.findFirstPlacement(newRec);

        // Should not overlap with existing rectangle
        assertNotNull(placement);
        assertFalse(solution.getPlacements().get(existing).equals(placement));
        assertTrue(placement.x >= 0 && placement.y >= 0);

        solution.addPlacement(newRec, placement);

        solution.printSolution(solution);
    }

    @Test
    void testFindFirstPlacement_NewBoxCreated() {
        // Fill the existing box completely
        Box box = solution.getBoxes().get(0);
        for (int x = 0; x <= boxSize - 2; x += 2) {
            for (int y = 0; y <= boxSize - 3; y += 3) {
                Rectangle rec = new Rectangle(2, 3);
                solution.addPlacement(rec, new Placement(box, x, y, false));
            }
        }

        // Now adding a new rectangle should force creation of a new box
        Rectangle newRec = new Rectangle(2, 3);
        Placement placement = solution.findFirstPlacement(newRec);

        assertNotNull(placement);
        assertEquals(0, placement.x);
        assertEquals(0, placement.y);
        assertNotEquals(box, placement.getBox()); // must be a new box
    }
}
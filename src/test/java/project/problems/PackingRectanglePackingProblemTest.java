package project.problems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackingRectanglePackingProblemTest {
    private PackingSolution solution;
    private int boxSize = 10;
    private PackingRectangle rect1;
    private PackingRectangle rect2;
    private PackingRectangle rect3;
    private Box box1;
    private RectanglePackingProblem problem;


    @BeforeEach
        void setUp() {
            // Create rectangles
            rect1 = new PackingRectangle(2, 3);
            rect2 = new PackingRectangle(3, 2);
            rect3 = new PackingRectangle(4, 4);

            List<PackingRectangle> packingRectangles = new ArrayList<>();
            packingRectangles.add(rect1);
            packingRectangles.add(rect2);
            packingRectangles.add(rect3);

            // Create the problem with all rectangles
            problem = new RectanglePackingProblem(packingRectangles, boxSize);

            // Create a solution and box
            solution = new PackingSolution(boxSize);
            box1 = new Box(boxSize, 0);
            solution.addBox(box1);

            // Place rectangles without overlapping
            solution.addPlacement(rect1, new Placement(box1, 0, 0, false));
            solution.addPlacement(rect2, new Placement(box1, 2, 0, false));
            solution.addPlacement(rect3, new Placement(box1, 0, 3, false));
        }

        @Test
        void testIsFeasible_ValidSolution() {
            // All rectangles present, no overlaps
            assertTrue(problem.isFeasible(solution));
        }

        @Test
        void testIsFeasible_MissingRectangle() {
            // Remove a rectangle from solution
            solution.getPlacements().remove(rect3);
            assertFalse(problem.isFeasible(solution));
        }

        @Test
        void testIsFeasible_OverlappingRectangles() {
            // Force overlap between rect1 and rect2
            solution.addPlacement(rect2, new Placement(box1, 1, 1, false));
            assertFalse(problem.isFeasible(solution));
        }

        @Test
        void testIsFeasible_InvalidPlacement() {
            // Force an invalid placement (e.g., negative coordinates)
            solution.addPlacement(rect1, new Placement(box1, -1, -1, false) {
                @Override
                public boolean isValid(PackingRectangle r) {
                    return false;
                }
            });
            assertFalse(problem.isFeasible(solution));
        }
}

package project.problems;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class SanityTest {


    @Test
    void createRectangleList() {
        // Liste erzeugen
        List<PackingRectangle> packingRectangles = new ArrayList<>();

        // Rechtecke hinzufügen
        packingRectangles.add(new PackingRectangle(3, 4));
        packingRectangles.add(new PackingRectangle(5, 2));
        packingRectangles.add(new PackingRectangle(1, 6));

        // Optional: assert, dass alles drin ist
        assertEquals(3, packingRectangles.size());
        assertEquals(3, packingRectangles.get(0).getWidth());
        assertEquals(4, packingRectangles.get(0).getHeight());
    }
    @Test
    void minimalTest() {
        List<PackingRectangle> packingRectangles = new ArrayList<>();

        packingRectangles.add(new PackingRectangle(3, 4));
        packingRectangles.add(new PackingRectangle(5, 2));
        packingRectangles.add(new PackingRectangle(1, 6));
        RectanglePackingProblem problem = new RectanglePackingProblem(packingRectangles,10);
        PackingSolution sol = problem.createInitialSolution();

        assertNotNull(sol);
        assertTrue(problem.isFeasible(sol));
    }
}

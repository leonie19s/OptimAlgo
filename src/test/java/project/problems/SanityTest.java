package project.problems;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class SanityTest {


    @Test
    void createRectangleList() {
        // Liste erzeugen
        List<Rectangle> rectangles = new ArrayList<>();

        // Rechtecke hinzufügen
        rectangles.add(new Rectangle(3, 4));
        rectangles.add(new Rectangle(5, 2));
        rectangles.add(new Rectangle(1, 6));

        // Optional: assert, dass alles drin ist
        assertEquals(3, rectangles.size());
        assertEquals(3, rectangles.get(0).getWidth());
        assertEquals(4, rectangles.get(0).getHeight());
    }
    @Test
    void minimalTest() {
        List<Rectangle> rectangles = new ArrayList<>();

        rectangles.add(new Rectangle(3, 4));
        rectangles.add(new Rectangle(5, 2));
        rectangles.add(new Rectangle(1, 6));
        RectanglePackingProblem problem = new RectanglePackingProblem(rectangles,10);
        PackingSolution sol = problem.createInitialSolution();

        assertNotNull(sol);
        assertTrue(problem.isFeasible(sol));
    }
}

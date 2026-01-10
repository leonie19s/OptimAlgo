package project.problems;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GeometryInstancesTest {
    final int boxSize = 15;

    public PackingRectangle getRandomRectangle(){
        Random rand = new Random();
        int min = 1;
        int max = boxSize+1;

        int rW = rand.nextInt(max-min+1)+min;
        int rH = rand.nextInt(max-min+1)+min;
        return new PackingRectangle(rW, rH);
    }

    public ArrayList<PackingRectangle> getRandomRectangles(int n){
        ArrayList<PackingRectangle> packingRectangles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            packingRectangles.add(getRandomRectangle());
        }
        return packingRectangles;
    }
    @Test
    void RectangleCreationTest(){

        PackingRectangle testRec = new PackingRectangle(5,10);
        assert testRec.height == 10;
        assert  testRec.width == 5;


        int nRecs = 5;
        ArrayList<PackingRectangle> recs = getRandomRectangles(nRecs);
        for (PackingRectangle rect : recs)
        {
            assertTrue(rect.width <= boxSize,
                    "Rectangle width too large: " + rect.width);
            assertTrue(rect.height <= boxSize,
                    "Rectangle height too large: " + rect.height);
        }
    }
    @Test
    void BoxCreationTest(){
        Box box1 = new Box(boxSize, 0);
        Box box2 = new Box(boxSize, 1);
        Box box3 = new Box(boxSize, 0);
        assertEquals(box1, box3);
        assertNotEquals(box1,box2);
    }

    @Test
    void PlacementCorrectnessTest(){
       PackingRectangle rec = getRandomRectangle();
       // h1 w7
       int h = rec.height;
       int w = rec.width;
       Box box = new Box(boxSize, 0);
       Placement placement = new Placement(box, 0,0,false);

       // correct rotation
       placement.rotate();
       assertTrue(placement.rotated);
       int rotatedHeight = placement.getHeight(rec);
       assertEquals(h, placement.getWidth(rec));
       assertEquals(w, placement.getHeight(rec));

       Box box2small = new Box(0,1);
       Placement wrongPlacement = new Placement(box2small, 0,0, false);
       assertFalse(wrongPlacement.isValid(rec));

    }
}

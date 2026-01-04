package project.problems;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PackingSolutionTest {
    int boxSize = 15;
    boolean samePlacement(Placement p1, Placement p2){
        if (p1.x != p2.x){
            return false;
        }
        if (p1.y != p2.y){
            return false;
        }
        if (p1.rotated != p2.rotated ){
            return false;
        }
        return true;
    }
    @Test
    void copyTest(){
        Box box1 = new Box(boxSize,3);
        Rectangle r1 = new Rectangle(2,3);
        Placement p1 = new Placement(box1, 0,0,false);

        PackingSolution sol = new PackingSolution(boxSize);
        sol.addBox(box1);
        sol.addPlacement(r1,p1);
        PackingSolution copy = sol.copy();
        assertNotSame(sol, copy);
        assertTrue(samePlacement(sol.getPlacement(r1), copy.getPlacement(r1)));
        assertEquals(sol.getBoxes(), copy.getBoxes());

        assertTrue(samePlacement( p1, copy.getPlacement(r1)));


    }

    @Test
    void placementModificationsTest(){

    }

    @Test
    void incrementalBoxIDsTest(){

    }

    @Test
    void boxModificationsTest(){

    }
}

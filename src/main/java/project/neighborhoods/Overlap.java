package project.neighborhoods;

import project.problems.PackingRectangle;
import project.problems.PackingSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/*
Die geometriebasierte Nachbarschaft wird an-
gepasst auf die Situation, dass Rechtecke sich zu einem gewissen Prozentsatz
überlappen dürfen. Die Überlappung zweier Rechtecke ist dabei die gemeinsame
Fläche geteilt durch das Maximum der beiden Rechteckflächen. Dieser Prozent-
satz ist zu Beginn 100 (so dass eine Optimallösung einfach zu finden ist). Im Lau-
fe der Zeit reduziert sich der Prozentsatz, und Verletzungen werden hart in der
Zielfunktion bestraft. Am Ende müssen Sie natürlich dafür sorgen, dass schluss-
endlich eine garantiert überlappungsfreie Lösung entsteht.
 */


public class Overlap implements Neighborhood<PackingSolution> {
    Random random = new Random();
    @Override
    public List<PackingSolution> generateNeighbors(PackingSolution solution, int n) {
        List<PackingSolution> neighbors = new ArrayList<>();

        for (int i = 0; i <n; i++){

            PackingSolution newSol = generateNeighbor(solution);
            if (newSol == null) continue;
            neighbors.add(newSol);

        }
        return neighbors;

    }

    @Override
    public PackingSolution generateNeighbor(PackingSolution solution) {


        int chance = random.nextInt(100);
        if (chance > 50) {

            List<PackingRectangle> recs = solution.getRectangleByBoxID(0);
            if(recs ==null){
                return null;
            }

            int randint = random.nextInt(recs.size());
            PackingRectangle randRec = recs.get(randint);

            return solution.createNeighborByPlacement(randRec, solution);
        }

        List<PackingRectangle> recs = solution.getAllRectangles();
        int randint = random.nextInt(recs.size());
        PackingRectangle randRec = solution.getRandomRectangle(random);

        return solution.createNeighborByBoxSwitch(randRec, random.nextBoolean(), random);

    }
    @Override
    public boolean allowsOverlap() {
        return true;
    }
}

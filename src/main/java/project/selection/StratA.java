package project.selection;

import project.problems.PackingSolution;
import project.problems.Rectangle;

import java.util.List;

public class StratA implements SelectionStrategy<Rectangle>{
    @Override
    public List<Rectangle> orderElements(List<Rectangle> elements) {
        return List.of();
    }
}

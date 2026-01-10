package project.selection;

import project.problems.PackingRectangle;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BiggestFirst implements SelectionStrategy<PackingRectangle>{
    @Override
    public List<PackingRectangle> orderElements(List<PackingRectangle> elements) {
        return elements.stream()
                .sorted(Comparator.<PackingRectangle>comparingInt(r -> r.height * r.width).reversed())
                .collect(Collectors.toList());


    }
}

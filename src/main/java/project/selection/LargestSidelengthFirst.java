package project.selection;

import project.problems.PackingRectangle;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LargestSidelengthFirst  implements  SelectionStrategy<PackingRectangle>{
    @Override
    public List<PackingRectangle> orderElements(List<PackingRectangle> elements) {
        return elements.stream()
                .sorted(
                        Comparator.<PackingRectangle>comparingInt(
                                r -> Math.max(r.width, r.height)  // sort by biggest side
                        ).reversed()  // descending: largest first
                )
                .collect(Collectors.toList());
    }


}

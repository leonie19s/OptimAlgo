package project.selection;

import project.problems.Rectangle;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LargestSidelengthFirst  implements  SelectionStrategy<Rectangle>{
    @Override
    public List<Rectangle> orderElements(List<Rectangle> elements) {
        return elements.stream()
                .sorted(
                        Comparator.<Rectangle>comparingInt(
                                r -> Math.max(r.width, r.height)  // sort by biggest side
                        ).reversed()  // descending: largest first
                )
                .collect(Collectors.toList());
    }


}

package project.selection;

import java.util.List;

public interface SelectionStrategy<T> {
    List<T> orderElements(List<T> elements);
}

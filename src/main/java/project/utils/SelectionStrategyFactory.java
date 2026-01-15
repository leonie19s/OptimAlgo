package project.utils;

import project.problems.PackingRectangle;
import project.selection.BiggestFirst;
import project.selection.LargestSidelengthFirst;
import project.selection.SelectionStrategy;

public  class SelectionStrategyFactory {

    private SelectionStrategyFactory() {
        // prevent instantiation
    }

    public static SelectionStrategy<PackingRectangle> create(String type) {
        if (type == null) {
            throw new IllegalArgumentException("SelectionStrategy type cannot be null");
        }

        return switch (type.toLowerCase()) {
            case "biggest", "biggestfirst", "bf" ->
                    new BiggestFirst();

            case "largestsidelength", "largestsidelengthfirst", "msl" ->
                    new LargestSidelengthFirst();

            default -> throw new IllegalArgumentException(
                    "Unknown SelectionStrategy type: " + type
            );
        };
    }
}

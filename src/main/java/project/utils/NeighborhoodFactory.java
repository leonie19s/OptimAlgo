package project.utils;

import project.neighborhoods.GeometryBased;
import project.neighborhoods.Neighborhood;
import project.neighborhoods.Overlap;
import project.neighborhoods.RuleBased;
import project.problems.PackingSolution;

public class NeighborhoodFactory {

    private NeighborhoodFactory() {
        // prevent instantiation
    }

    public static Neighborhood<PackingSolution> create(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Neighborhood type cannot be null");
        }

        return switch (type.toLowerCase()) {
            case "geometry", "geometrybased", "geo" -> new GeometryBased();
            case "rule", "rulebased" -> new RuleBased();
            case "overlap" -> new Overlap();
            default -> throw new IllegalArgumentException(
                    "Unknown neighborhood type: " + type
            );
        };
    }
}

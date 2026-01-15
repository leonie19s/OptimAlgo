package project.utils;

import project.algorithms.Solution;
import project.algorithms.stoppingCriteria.MaxIterations;
import project.algorithms.stoppingCriteria.MaxTime;
import project.algorithms.stoppingCriteria.NoImprovement;
import project.algorithms.stoppingCriteria.StoppingCriterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CriteriaFactory {

    private CriteriaFactory() {}

    public static <T extends Solution<?>>
    List<StoppingCriterion<T>> create(String input) {

        if (input == null || input.isBlank()) {
            return List.of();
        }

        List<StoppingCriterion<T>> criteria = new ArrayList<>();

        for (String token : input.split(",")) {
            criteria.add(parse(token.trim()));
        }

        return List.copyOf(criteria);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Solution<?>>
    StoppingCriterion<T> parse(String token) {

        String[] parts = token.split(":", 2);
        String key = parts[0].toLowerCase();

        return switch (key) {
            case "maxiterations", "iterations" ->
                    new MaxIterations<>(parseInt(parts, 1000));

            case "maxtime", "time" ->
                    new MaxTime<>(parseDurationMillis(parts, 60_000));

            case "noimprovement", "stagnation" ->
                    new NoImprovement<>(parseInt(parts, 100));

            default -> throw new IllegalArgumentException(
                    "Unknown stopping criterion: " + key
            );
        };
    }

    private static int parseInt(String[] parts, int defaultValue) {
        return parts.length > 1
                ? Integer.parseInt(parts[1])
                : defaultValue;
    }

    private static long parseDurationMillis(String[] parts, long defaultValue) {
        if (parts.length == 1) {
            return defaultValue;
        }

        String value = parts[1].toLowerCase(Locale.ROOT);

        if (value.endsWith("ms")) {
            return Long.parseLong(value.substring(0, value.length() - 2));
        }
        if (value.endsWith("s")) {
            return Long.parseLong(value.substring(0, value.length() - 1)) * 1_000;
        }
        if (value.endsWith("m")) {
            return Long.parseLong(value.substring(0, value.length() - 1)) * 60_000;
        }

        return Long.parseLong(value);
    }}
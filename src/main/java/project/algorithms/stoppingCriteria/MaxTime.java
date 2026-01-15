package project.algorithms.stoppingCriteria;
import project.algorithms.Solution;
import project.algorithms.stoppingCriteria.StoppingCriterion;

public class MaxTime<T extends Solution<?>>
        implements StoppingCriterion<T> {

    private final long maxMillis;
    private long start = -1;

    public MaxTime(long maxMillis) {
        this.maxMillis = maxMillis;
    }

    @Override
    public void init(T initialSolution) {
        // optional
    }

    @Override
    public void update(T previous, T current, boolean wasBetter) {
        if (start < 0) {
            start = System.currentTimeMillis();
        }
    }

    @Override
    public boolean shouldStop() {
        if (start < 0) {
            start = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - start >= maxMillis;
    }
}
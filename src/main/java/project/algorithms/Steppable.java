package project.algorithms;

public interface Steppable<O> {
    boolean hasNext();
    O next();
    O getCurrent();
}

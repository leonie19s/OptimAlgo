package project.algorithms;

public interface Solution <S>{
    // copy um aktuelle Lösung zu kopieren wenn man neue ausprobiert
    Solution<S> copy();
    void applyChange(S elem);
    void printSolution(Solution<S> sol);
}

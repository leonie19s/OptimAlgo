package project.algorithms;

import java.util.List;

public interface Solution <S>{
    // copy um aktuelle Lösung zu kopieren wenn man neue ausprobiert
    Solution<S> copy();
    Solution<S> copyEmpty();
    void applyChange(S elem);
    void printSolution(Solution<S> sol);
    List<S> getPerm();
    void applyPerm(List<S> perm);
}

package utilities;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import algorithms.UDG;
@FunctionalInterface

public interface ShouldContinueGreedy extends Serializable {
  boolean method(UDG currentSolution, UDG rest); 
}
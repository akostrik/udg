package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.UDG;
@FunctionalInterface

public interface ShouldContinueGreedy extends Serializable {
  boolean method(UDG currentSolution, UDG rest); 
}
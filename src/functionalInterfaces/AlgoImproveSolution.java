package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.UDG;
@FunctionalInterface

public interface AlgoImproveSolution extends Serializable {
  UDG method(UDG firstSolution); 
}
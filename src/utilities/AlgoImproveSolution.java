package utilities;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import algorithms.UDG;
@FunctionalInterface

public interface AlgoImproveSolution extends Serializable {
  UDG method(UDG firstSolution); 
}
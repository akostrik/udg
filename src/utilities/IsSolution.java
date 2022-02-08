package utilities;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import algorithms.UDG;
@FunctionalInterface

public interface IsSolution extends Serializable {
  boolean method(UDG solutionCandidat); 
}
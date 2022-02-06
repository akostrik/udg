package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.UDG;
@FunctionalInterface

public interface IsSolution extends Serializable {
  boolean method(UDG solutionCandidat); 
}
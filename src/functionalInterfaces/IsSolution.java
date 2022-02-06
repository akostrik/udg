package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udp.UDP;
@FunctionalInterface

public interface IsSolution extends Serializable {
  boolean method(UDP solutionCandidat); 
}
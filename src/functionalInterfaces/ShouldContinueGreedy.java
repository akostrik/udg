package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udp.UDP;
@FunctionalInterface

public interface ShouldContinueGreedy extends Serializable {
  boolean method(UDP currentSolution, UDP rest); 
}
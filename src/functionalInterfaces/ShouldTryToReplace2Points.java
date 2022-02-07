package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.Vertex;
@FunctionalInterface

public interface ShouldTryToReplace2Points extends Serializable {
  boolean method(Vertex p1, Vertex p2); 
}
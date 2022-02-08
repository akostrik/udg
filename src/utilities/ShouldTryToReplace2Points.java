package utilities;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import algorithms.Vertex;
@FunctionalInterface

public interface ShouldTryToReplace2Points extends Serializable {
  boolean method(Vertex p1, Vertex p2); 
}
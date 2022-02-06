package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.Vertex;
@FunctionalInterface

public interface WillTryToReplace3Points extends Serializable {
  boolean method(Vertex p1, Vertex p2, Vertex p3); 
}
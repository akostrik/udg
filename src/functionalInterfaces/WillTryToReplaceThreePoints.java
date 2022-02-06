package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udp.Vertex;
@FunctionalInterface

public interface WillTryToReplaceThreePoints extends Serializable {
  boolean method(Vertex p1, Vertex p2, Vertex p3); 
}
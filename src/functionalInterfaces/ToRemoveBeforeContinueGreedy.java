package functionalInterfaces;
import java.io.Serializable;

import udp.UDP;
import udp.Vertex;
@FunctionalInterface

public interface ToRemoveBeforeContinueGreedy extends Serializable {
  UDP method(Vertex p); 
}
package functionalInterfaces;
import java.io.Serializable;

import udg.UDG;
import udg.Vertex;
@FunctionalInterface

public interface ToRemoveBeforeContinueGreedy extends Serializable {
  UDG method(Vertex p); 
}
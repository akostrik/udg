package utilities;
import java.io.Serializable;

import algorithms.UDG;
import algorithms.Vertex;
@FunctionalInterface

public interface ToRemoveBeforeContinueGreedy extends Serializable {
  UDG method(Vertex p); 
}
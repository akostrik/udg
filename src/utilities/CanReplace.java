package utilities;
import java.io.Serializable;

import algorithms.UDG;
import algorithms.Vertex;
@FunctionalInterface

public interface CanReplace extends Serializable {
  boolean method(Vertex p, UDG solution); 
}

package functionalInterfaces;
import java.io.Serializable;

import udg.UDG;
import udg.Vertex;
@FunctionalInterface

public interface CanReplace extends Serializable {
  boolean method(Vertex p, UDG solution); 
}

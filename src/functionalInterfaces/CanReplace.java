package functionalInterfaces;
import java.io.Serializable;
import udp.UDP;
import udp.Vertex;
@FunctionalInterface

public interface CanReplace extends Serializable {
  boolean method(Vertex p, UDP solution); 
}

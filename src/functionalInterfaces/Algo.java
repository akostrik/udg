package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udp.UDP;
@FunctionalInterface

public interface Algo extends Serializable {
  UDP method(); 
}
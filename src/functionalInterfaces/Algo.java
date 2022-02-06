package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.UDG;
@FunctionalInterface

public interface Algo extends Serializable {
  UDG method(); 
}
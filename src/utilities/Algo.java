package utilities;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import algorithms.UDG;
@FunctionalInterface

public interface Algo extends Serializable {
  UDG method(); 
}
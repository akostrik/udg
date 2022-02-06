package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udg.UDG;
@FunctionalInterface

public interface ToReturnGreedy extends Serializable {
  UDG method(UDG initiallyAllPoints,UDG initiallyEmpty); 
}
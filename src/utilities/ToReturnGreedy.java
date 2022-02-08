package utilities;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import algorithms.UDG;
@FunctionalInterface

public interface ToReturnGreedy extends Serializable {
  UDG method(UDG initiallyAllPoints,UDG initiallyEmpty); 
}
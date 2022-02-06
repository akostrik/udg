package functionalInterfaces;
import java.io.Serializable;
import java.lang.FunctionalInterface;

import udp.UDP;
@FunctionalInterface

public interface ToReturnGreedy extends Serializable {
  UDP method(UDP initiallyAllPoints,UDP initiallyEmpty); 
}
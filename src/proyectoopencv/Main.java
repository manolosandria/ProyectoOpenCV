package proyectoopencv;

import org.opencv.core.Core;

/**
 * @author Ángel Manuel Sandria Pérez
 */
public class Main {

  public static void main(String[] args) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    VentanaCV ventana = new VentanaCV();
    ventana.setSize(600, 600);
    ventana.setVisible(true);
  }

}
  
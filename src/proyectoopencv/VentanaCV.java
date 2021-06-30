package proyectoopencv;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * @author Ángel Manuel Sandria Pérez
 */
public class VentanaCV extends javax.swing.JFrame {

  private DaemonThread hilo = null;
  private VideoCapture webcam = null;
  Mat frame = new Mat();
  MatOfByte mem = new MatOfByte();
  CascadeClassifier detector;
  MatOfRect detecciones = new MatOfRect();

  BufferedImage buff;
  String base_path = "C:\\Users\\PC\\Desktop\\Cara";
  String faceFile = "C:\\Users\\PC\\Desktop\\Cara\\haarcascade_frontalface_alt.xml";

  public VentanaCV() {
    initComponents();
  }

  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panelBotones = new javax.swing.JPanel();
    botonIniciar = new javax.swing.JButton();
    botonDetener = new javax.swing.JButton();
    panelCentro = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    botonIniciar.setText("Iniciar");
    botonIniciar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        botonIniciarActionPerformed(evt);
      }
    });
    panelBotones.add(botonIniciar);

    botonDetener.setText("Detener");
    botonDetener.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        botonDetenerActionPerformed(evt);
      }
    });
    panelBotones.add(botonDetener);

    getContentPane().add(panelBotones, java.awt.BorderLayout.SOUTH);
    getContentPane().add(panelCentro, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void botonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIniciarActionPerformed
    webcam = new VideoCapture(0);
    hilo = new DaemonThread();
    Thread t = new Thread(hilo);
    t.setDaemon(true);
    hilo.runnable = true;
    t.start();
    botonIniciar.setEnabled(false);  //start button
    botonDetener.setEnabled(true);  // stop button
  }//GEN-LAST:event_botonIniciarActionPerformed

  private void botonDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDetenerActionPerformed
    hilo.runnable = false;
    botonDetener.setEnabled(false);
    botonIniciar.setEnabled(true);

    webcam.release();
  }//GEN-LAST:event_botonDetenerActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton botonDetener;
  private javax.swing.JButton botonIniciar;
  private javax.swing.JPanel panelBotones;
  private javax.swing.JPanel panelCentro;
  // End of variables declaration//GEN-END:variables

  class DaemonThread implements Runnable {

    protected volatile boolean runnable = false;

    @Override
    public void run() {
      detector = new CascadeClassifier(faceFile);
      detecciones = new MatOfRect();
      synchronized (this) {
        while (runnable) {
          if (webcam.grab()) {
            try {
              webcam.retrieve(frame);
              Graphics g = panelCentro.getGraphics();
              detector.detectMultiScale(frame, detecciones);
              for (Rect rect : detecciones.toArray()) {
                Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                  new Scalar(0, 255, 0));
              }
              Imgcodecs.imencode(".bmp", frame, mem);
              Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
              BufferedImage buff = (BufferedImage) im;
              if (g.drawImage(buff, 0, 0, getWidth(), getHeight() - 150, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                if (runnable == false) {
                  System.out.println("Pausado ..... ");
                  this.wait();
                }
              }
            } catch (Exception ex) {
              System.out.println("Error!");
              ex.printStackTrace();
            }
          }
        }
      }
    }
  }

}
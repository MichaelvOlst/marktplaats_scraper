package nl.michaelvanolst.app.Tasks2;

import java.util.TimerTask;

abstract class Task2 extends TimerTask {

  public void mail(String text) {
    System.out.println(text);
  }
}

package nl.michaelvanolst.app.Tasks2;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BicycleDiscountShop extends Task2 {

  private final String url = "https://www.fietsvoordeelshop.nl/gazelle-orange-c7-plus-2022-grijs-heren";

  private Document doc;

  public void run() {

    try {
      this.doc = Jsoup.connect(url).get();
      this.handle();
    } catch(IOException ex) {
      System.out.println(ex.getStackTrace());
      return;
    }

  }

  private void handle() {
    Element element = this.doc.select("#productSelector115915Label").first();
    String text = element.text();

    this.mail(text);

    // if(text.contains("op voorraad")) {
    //   System.out.println("Op voorraad");
    // } else {
    //   System.out.println("Niet op voorraad");
    // }

    
  }
}

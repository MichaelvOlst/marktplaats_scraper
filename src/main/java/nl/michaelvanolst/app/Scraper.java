package nl.michaelvanolst.app;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import nl.michaelvanolst.app.Exceptions.ScraperException;

public class Scraper {

  private final String url;
  private final String[] selectors;

  public Scraper(String url, String[] selectors) {
    this.url = url;
    this.selectors = selectors;
  }

  public String[] get() throws ScraperException {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch();
      Page page = browser.newPage();
      page.navigate(this.url);

      List<String> contents = new ArrayList<String>();
      for(String selector : this.selectors) {
        contents.add(page.locator(selector).textContent().trim());
      }

      page.close();
      browser.close();
      playwright.close();

      return contents.toArray(new String[contents.size()]);
      
    } catch(Exception ex) {
      throw new ScraperException(ex.getMessage());
    }

  }


  // private Page getPage() throws ScraperException {
  //   try (Playwright playwright = Playwright.create()) {
  //     Browser browser = playwright.chromium().launch();
  //     Page page = browser.newPage();
  //     page.navigate(this.url);

  //     return page;
      
  //   } catch(Exception ex) {
  //     throw new ScraperException(ex.getMessage());
  //   }
  // }
}

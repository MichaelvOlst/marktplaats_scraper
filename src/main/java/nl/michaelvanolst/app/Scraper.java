package nl.michaelvanolst.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import lombok.AllArgsConstructor;
import nl.michaelvanolst.app.Dto.ScraperResult;
import nl.michaelvanolst.app.Dto.TaskDto;
import nl.michaelvanolst.app.Exceptions.ScraperException;

@AllArgsConstructor
public class Scraper {

  private final TaskDto taskDto;

  public List<ScraperResult> get() throws ScraperException {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch();
      Page page = browser.newPage();
      page.navigate(this.taskDto.getUrl());

      // if(this.taskDto.getType() == "collect") {

      // }




      List<ScraperResult> results = new ArrayList<ScraperResult>();

      Locator items = page.locator(this.taskDto.getItemSelector()).;
      System.out.println("items: " + items.count());

      for(int i = 0; i < items.count(); ++i) {

        System.out.println("i: " + i);


        Map<String, String> contents = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : this.taskDto.getSelectors().entrySet()) {
          System.out.println(entry.getKey() + ":" + entry.getValue());

          contents.put(entry.getKey(), items.nth(i).locator(entry.getValue()).textContent());

          System.out.println(items.nth(i).locator(entry.getValue()).textContent());
        }

        results.add(new ScraperResult(contents));
      }

      page.close();
      browser.close();
      playwright.close();

      System.out.println("Finished scraping");

      System.out.println(results.toString());

      return results;
      
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

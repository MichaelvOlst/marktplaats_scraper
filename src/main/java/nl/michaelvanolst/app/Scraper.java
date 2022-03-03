package nl.michaelvanolst.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;


import lombok.AllArgsConstructor;
import nl.michaelvanolst.app.Dto.ScraperResultDto;
import nl.michaelvanolst.app.Dto.TaskDto;
import nl.michaelvanolst.app.Exceptions.ScraperException;
import nl.michaelvanolst.app.store.JsonStore;

@AllArgsConstructor
public class Scraper {

  private final TaskDto taskDto;
  private final JsonStore jsonStore;

  public List<ScraperResultDto> get() throws ScraperException,IOException {

    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions().setHeadless(true).setSlowMo(100)
      );
      Page page = browser.newPage();
      page.navigate(this.taskDto.getUrl());

      URL netUrl = new URL(this.taskDto.getUrl());
      String host = netUrl.getHost();

      List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

      Locator items = page.locator(this.taskDto.getItemHolder());
      System.out.println("items: " + items.count());

      for(int i = 0; i < items.count(); ++i) {
        
        String uri = items.nth(i).locator(this.taskDto.getItemHref()).getAttribute("href");
        String url = host + uri;

        if(this.jsonStore.exists(url)) {
          System.out.println("File exists: "+ url);
          continue;
        }

        // System.out.println(url);
        ScraperResultDto scraperResultDto = new ScraperResultDto();
        Map<String, String> contents = new HashMap<String, String>();
        
        contents.put("url", url);

        for (Map.Entry<String, String> entry : this.taskDto.getSelectors().entrySet()) {
          // System.out.println(entry.getKey() + ":" + entry.getValue());

          contents.put(entry.getKey(), items.nth(i).locator(entry.getValue()).textContent());

          // System.out.println(items.nth(i).locator(entry.getValue()).textContent());
        }

        scraperResultDto.setUrl(url);
        scraperResultDto.setContents(contents);

        results.add(scraperResultDto);
      }

      page.close();
      browser.close();
      playwright.close();


      System.out.println("Finished scraping");

      // store the data for the first time of scraping, so we don't notify all the results for the time
      if(!this.jsonStore.exists(this.taskDto.getTitle())){
        try {
          for(ScraperResultDto scraperDto: results) {
            if(!this.jsonStore.exists(scraperDto.getUrl())){
              this.jsonStore.put(this.taskDto.getTitle(), scraperDto);
            }
          }
        } catch(IOException ex) {
          System.out.println(ex.getMessage());
        }
        return new ArrayList<ScraperResultDto>();
      }

      return results;
      
    } catch(Exception ex) {
      throw new ScraperException(ex.getMessage());
    }

  }


}

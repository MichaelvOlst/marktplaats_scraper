package nl.michaelvanolst.app.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;


import lombok.AllArgsConstructor;
import nl.michaelvanolst.app.dto.ScraperResultDto;
import nl.michaelvanolst.app.dto.TaskDto;
import nl.michaelvanolst.app.exceptions.ScraperException;

@AllArgsConstructor
public class Scraper {

  private final TaskDto taskDto;

  public List<ScraperResultDto> get() throws ScraperException,IOException {

    Logger.info("Started Scraping: " + this.taskDto.getTitle());


    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions()
        .setHeadless(Config.getBoolean("scraper.headless"))
        .setSlowMo(100)
      );
      Page page = browser.newPage();
      page.navigate(this.taskDto.getUrl());

      URL netUrl = new URL(this.taskDto.getUrl());
      String host = netUrl.getProtocol() + "://" +netUrl.getHost();

      List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

      Locator items = page.locator(this.taskDto.getItemHolder());
      // Logger.info("Total number of items: " + items.count());

      for(int i = 0; i < items.count(); ++i) {
        
        String uri = items.nth(i).locator(this.taskDto.getItemHref()).getAttribute("href");
        String url = host + uri;

        ScraperResultDto scraperResultDto = new ScraperResultDto();
        Map<String, String> contents = new HashMap<String, String>();
        
        contents.put("url", url);

        for (Map.Entry<String, String> entry : this.taskDto.getSelectors().entrySet()) {
          Locator selector = items.nth(i).locator(entry.getValue());
          if(selector.isVisible()) {
            contents.put(entry.getKey(), selector.textContent());
          } else {
            contents.put(entry.getKey(), "");
          }
        }

        scraperResultDto.setUrl(url);
        scraperResultDto.setContents(contents);

        results.add(scraperResultDto);
      }

      page.close();
      browser.close();
      playwright.close();

      Logger.info("Finished Scraping: " + this.taskDto.getTitle());

      return results;
      
    } catch(Exception ex) {
      throw new ScraperException(ex.getMessage());
    }

  }


}

package nl.michaelvanolst.app.services;

import java.net.MalformedURLException;
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
import nl.michaelvanolst.app.dtos.ScraperResultDto;
import nl.michaelvanolst.app.dtos.TaskDto;
import nl.michaelvanolst.app.exceptions.ScraperException;

@AllArgsConstructor
public class Scraper {

  private final int MAX_PAGE = 3;
  private final TaskDto taskDto;
  private List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

  public Scraper(TaskDto taskDto) {
    this.taskDto = taskDto;
  }

  public List<ScraperResultDto> get() throws ScraperException {

    Logger.info("Started Scraping: " + this.taskDto.getTitle());

    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions()
        .setHeadless(Config.getBoolean("scraper.headless"))
        .setSlowMo(300)
      );
      Page page = browser.newPage();

      page.navigate(this.taskDto.getUrl());

      Locator navLinks = page.locator(".mp-PaginationControls-pagination-pageList .mp-TextLink");

      List<String> urls = new ArrayList<>();
      urls.add(this.taskDto.getUrl());

      for(int i = 0; i < navLinks.count(); ++i) {
        navLinks.nth(i).waitFor();

        String uri = navLinks.nth(i).getAttribute("href");
        String url = this.getHost() + uri;

        urls.add(url);

        if(urls.size() >= MAX_PAGE) {
          break;
        }
      }

      Logger.info("urls to scrape: "+urls.toString());

      for (String url : urls) {
        this.scrape(page, url);
//        TimeUnit.SECONDS.sleep(1);
      }

      page.close();
      browser.close();
      playwright.close();

      Logger.info("Finished Scraping: " + this.taskDto.getTitle());
    } catch(Exception ex) {
      throw new ScraperException(ex.getMessage());
    }

    return this.results;
  }

  private void scrape(Page page, String url) throws MalformedURLException {
    page.navigate(url);

    Logger.info("navigated to :" + url);

    Locator items = page.locator(this.taskDto.getItemHolder());

    Logger.info("items: "+ items.count());

    for(int i = 0; i < items.count(); ++i) {

      String uri = items.nth(i).locator(this.taskDto.getItemHref()).getAttribute("href");
      String itemUrl = this.getHost() + uri;

      Logger.info("itemurl : " +itemUrl);

      Map<String, String> contents = new HashMap<String, String>();

      contents.put("url", itemUrl);

      for (Map.Entry<String, String> entry : this.taskDto.getSelectors().entrySet()) {
        Locator selector = items.nth(i).locator(entry.getValue());
        if(selector.isVisible()) {
          contents.put(entry.getKey(), selector.textContent());
        } else {
          contents.put(entry.getKey(), "");
        }
      }

      ScraperResultDto scraperResultDto = ScraperResultDto.builder()
              .url(itemUrl)
              .contents(contents)
              .build();

      Logger.info("scraper result: "+ scraperResultDto);

      this.results.add(scraperResultDto);
    }
  }


  private String getHost() throws MalformedURLException {
    URL netUrl = new URL(this.taskDto.getUrl());
    return  netUrl.getProtocol() + "://" +netUrl.getHost();
  }
}

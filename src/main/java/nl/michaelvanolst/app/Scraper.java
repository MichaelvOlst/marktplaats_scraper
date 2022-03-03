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

@AllArgsConstructor
public class Scraper {

  private final TaskDto taskDto;

  public List<ScraperResultDto> get() throws ScraperException,IOException {

    List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

    if(!this.storageFileExists()){
      return results;
    }
    

    // if(directory_not_exists) {
    //   create_directory
    // }


    // if(file_not_exists) {
    //   create_file
    //   and do not for the first time just return
    // }


    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions().setHeadless(true).setSlowMo(100)
      );
      Page page = browser.newPage();
      page.navigate(this.taskDto.getUrl());

      URL netUrl = new URL(this.taskDto.getUrl());
      String host = netUrl.getHost();

      // List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

      Locator items = page.locator(this.taskDto.getItemHolder());
      System.out.println("items: " + items.count());

      for(int i = 0; i < items.count(); ++i) {
        
        String uri = items.nth(i).locator(".mp-Listing-coverLink").getAttribute("href");
        String url = host + uri;

        // System.out.println(url);

        Map<String, String> contents = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : this.taskDto.getSelectors().entrySet()) {
          // System.out.println(entry.getKey() + ":" + entry.getValue());

          contents.put(entry.getKey(), items.nth(i).locator(entry.getValue()).textContent());

          // System.out.println(items.nth(i).locator(entry.getValue()).textContent());
        }

        results.add(new ScraperResultDto(contents));
      }

      page.close();
      browser.close();
      playwright.close();

      System.out.println("Finished scraping");

      return results;
      
    } catch(Exception ex) {
      throw new ScraperException(ex.getMessage());
    }

  }


  public boolean storageFileExists() throws IOException {

    String storageDirectory = System.getProperty("user.dir") + "/storage";
    String filename = this.taskDto.getTitle() + ".json";
    filename = filename.toLowerCase().replace(" ", "_");

    File directory = new File(storageDirectory);
    if (! directory.exists()){
      directory.mkdir();
    }
    

    File storageFile = new File(storageDirectory + "/" + filename);
    if (! storageFile.exists()){
      storageFile.createNewFile();
      return false;
    }

    return true;
  }

}

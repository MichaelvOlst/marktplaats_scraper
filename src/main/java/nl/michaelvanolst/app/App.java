package nl.michaelvanolst.app;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import nl.michaelvanolst.app.Tasks.TaskCollector;

public class App 
{
    public static void main( String[] args ) throws IOException {

        String basePath = StringUtils.stripEnd(System.getProperty("user.dir"), "/");
        String taskDirectory = basePath + "/tasks";

        TaskCollector taskCollector = new TaskCollector(taskDirectory);
        
        // try {
        //     Logger.info(taskCollector.get());
        // } catch(IOException ex) {
        //     Logger.info(ex.getStackTrace());
        // }

        try {
            ScrapeScheduler scheduler = new ScrapeScheduler(taskCollector.get());
            scheduler.run();
        } catch(IOException ex) {
            Logger.fatal(ex.getMessage());
        }
    }

    
}
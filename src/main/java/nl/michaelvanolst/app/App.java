package nl.michaelvanolst.app;

import java.io.IOException;
import nl.michaelvanolst.app.Tasks.TaskCollector;

public class App 
{
    public static void main( String[] args ) {
        String taskDirectory = System.getProperty("user.dir") + "/tasks";
        TaskCollector taskCollector = new TaskCollector(taskDirectory);

        try {
            ScrapeScheduler scheduler = new ScrapeScheduler(taskCollector.get());
            scheduler.run();
        } catch(IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    
}
package nl.michaelvanolst.app;

import nl.michaelvanolst.app.Watcher.Watcher;

public class App 
{
    public static void main( String[] args ) {
        String taskDirectory = System.getProperty("user.dir") + "/tasks";
        Watcher watcher = new Watcher(taskDirectory);
        watcher.run();
    }

    
}
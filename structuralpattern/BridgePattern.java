/*The Bridge Pattern is a structural design pattern that is used to decouple an abstractions = 
from its implementation so that the two can vary independently. */
package structuralpattern;
interface VideoQuality {
    void load(String title);
}

class HDQuality implements VideoQuality {
    @Override
    public void load(String title) {
        System.out.println("Loading in HD Quality: " + title);
    }
}

class SDQuality implements VideoQuality {
    @Override
    public void load(String title) {
        System.out.println("Loading in SD Quality: " + title);
    }
}

class K4Quality implements VideoQuality {
    @Override
    public void load(String title) {
        System.out.println("Loading in 4K Quality: " + title);
    }
}

// ==========================================
// 2. Base Abstraction (Holds the Bridge reference)
// ==========================================
abstract class App {
    // The "Bridge" link to the implementor
    protected VideoQuality videoQuality;

    public App(VideoQuality videoQuality) {
        this.videoQuality = videoQuality;
    }

    public abstract void loadVideo(String title);
}

// ==========================================
// 3. Refined Abstractions (Platform Specifics)
// ==========================================
class MobileApp extends App {
    public MobileApp(VideoQuality videoQuality) {
        super(videoQuality);
    }

    @Override
    public void loadVideo(String title) {
        System.out.print("[Mobile] ");
        videoQuality.load(title);
    }
}

class TabApp extends App {
    public TabApp(VideoQuality videoQuality) {
        super(videoQuality);
    }

    @Override
    public void loadVideo(String title) {
        System.out.print("[Tablet] ");
        videoQuality.load(title);
    }
}

// ==========================================
// 4. Client Code
// ==========================================
public class BridgePattern {
    public static void main(String[] args) {
        App mobileApp = new MobileApp(new SDQuality());
        mobileApp.loadVideo("Interstellar");

        App tabApp = new TabApp(new K4Quality());
        tabApp.loadVideo("Inception");
    }
}
/*The Proxy Pattern is a structural design pattern 
that provides a surrogate or placeholder for another object to control access to it.
A proxy acts as an intermediary that implements the same interface as the original object, 
allowing it to intercept and manage requests to the real object. */
package structuralpattern;

import java.util.*;

interface VideoService {
    String playVideo(String videoId);
}

class ActualVideoService implements VideoService {
    @Override
    public String playVideo(String videoId) {
        System.out.println("Playing video with ID: " + videoId);
        return "Video content for " + videoId;
    }
}

class VideoServiceProxy implements VideoService {
    private ActualVideoService actualVideoService;
    private Map<String,String> videoAccessMap;

    public VideoServiceProxy() {
        this.videoAccessMap = new HashMap<>();
        this.actualVideoService = new ActualVideoService();
    }

    @Override
    public String playVideo(String videoId) {
        if (videoAccessMap.containsKey(videoId)) {
            System.out.println("Returning cached video for ID: " + videoId);
            return videoAccessMap.get(videoId);
        } else {
            String videoContent = actualVideoService.playVideo(videoId);
            videoAccessMap.put(videoId, videoContent);
            System.out.println("Video with ID: " + videoId + " has been cached.");
            return videoContent;
        }
    }
}

public class ProxyPattern {
    public static void main(String[] args) {
        VideoService videoService = new VideoServiceProxy();

        // First time playing the video, it will be fetched and cached
        videoService.playVideo("video123");

        // Second time playing the same video, it will be returned from cache
        videoService.playVideo("video123");
    }
}

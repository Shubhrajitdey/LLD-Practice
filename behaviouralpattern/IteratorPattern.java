/*The Iterator Pattern is a behavioral design pattern that provides a way to access 
the elements of a collection sequentially without exposing the underlying representation. */
package behaviouralpattern;

import java.util.ArrayList;
import java.util.List;

class Video {
    private String name;

    public Video(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

interface Playlist {
    PlaylistIterator createIterator();
    void addVideo(Video video);
}

class YoutubePlaylist implements Playlist{
    private List<Video> videos = new ArrayList<>();
    @Override
    public void addVideo(Video video) {
        videos.add(video);
    }
    @Override
    public PlaylistIterator createIterator() {
        return new YoutubePlaylistIterator(videos);
    }
}

interface PlaylistIterator {
    boolean hasNext();
    Video next();
}
class YoutubePlaylistIterator implements PlaylistIterator {
    private List<Video> videos;
    private int position = 0;   

    public YoutubePlaylistIterator(List<Video> videos) {
        this.videos = videos;
    }

    public boolean hasNext() {
        return position < videos.size();
    }

    public Video next() {
        if (hasNext()) {
            return videos.get(position++);
        }
        return null;
    }
}

public class IteratorPattern {
    public static void main(String[] args) {
        Playlist playlist = new YoutubePlaylist();
        playlist.addVideo(new Video("Video 1"));
        playlist.addVideo(new Video("Video 2"));
        
        PlaylistIterator iterator = playlist.createIterator();
        while (iterator.hasNext()) {
            Video video = iterator.next();
            System.out.println(video.getName());
        }
    }
}

package mixpattern;

import java.util.ArrayList;
import java.util.List;

interface Player{
    int playTime();
}
class Song implements Player{
    private String name;
    private int duration;
    Song(String name, int duration){
        this.name = name;
        this.duration = duration;
    }
    @Override 
    public int playTime(){
        return duration;
    }
}

class Playlist implements Player{
    List<Player> playerList = new ArrayList<>();
    private int totalDuration = 0;
    public void addToPlaylist(Player play){
        playerList.add(play);
    }
    @Override 
    public int playTime(){
        for(Player play : playerList){
            totalDuration += play.playTime();
        }
        return totalDuration;
    }
}
public class MusicApp {
    public static void main(String[] args) {
        Song bindas = new Song("Banzara",10);
        Song cold = new Song("cold",18);
        Song rabindra = new Song("radindrasong",20);

        System.out.println(cold.playTime());
        System.out.println(rabindra.playTime());

        Playlist mPlaylist = new Playlist();
        mPlaylist.addToPlaylist(bindas);
        mPlaylist.addToPlaylist(cold);

        System.out.println(mPlaylist.playTime());

    }
}

package mixpattern;

interface Stream{
    public void play(String userName,String type);
}

class VideoStream implements Stream{

    @Override 
    public void play(String username, String type){
        System.out.println("Playing video for subscribed user "+ username);
    }
}

class VideoStreamProxy implements Stream{
    private VideoStream videostream;

    @Override 
    public void play(String userName, String type){
        if(type.equals("subscriped")){
            if(videostream != null){
                videostream.play(userName, type);
            }else{
                videostream = new VideoStream();
                videostream.play(userName, type);
            }
        }else{
            System.out.println("Playing video with Add for non subscribed user "+ userName);
        }
        
    }
}   

public class VideoStreamService {
    public static void main(String[] args) {
        Stream stream = new VideoStreamProxy();
        stream.play("deyBittu","subscriped");

        Stream streamUn = new VideoStreamProxy();
        streamUn.play("Bittu","unsubscriped");
    }
}

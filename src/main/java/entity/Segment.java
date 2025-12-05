package entity;

public class Segment {

    private int x;
    private int y;


    public Segment(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set_Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}

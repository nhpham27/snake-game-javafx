package nguyenhoa.app;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.shape.Circle;

public class Snake extends Circle {
    private List<Circle> tails;
    private Direction direction;
    private final int STEP = 10;

    Snake(int x, int y, int r) {
        super(x, y, r);
        direction = Direction.UP;
        tails = new ArrayList<Circle>();
    }

    // handle moves of snake
    public void move() {
        Platform.runLater(() -> {
            step();
        });
    }

    // get direction of snake
    public Direction getDirection() {
        return direction;
    }

    // set direction od snake
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // get length
    public int getLength(){
        return tails.size();
    }

    // some object collides the snake
    public boolean isCollided(Circle obj){
        if(this.intersects(obj.getBoundsInLocal())){
            return true;
        }

        for(Circle c : tails){
            if(c.intersects(obj.getBoundsInLocal())){
                return true;
            }
        }

        return false;
    }

    // moving the tails of the snake
    private void step() {
        adjustLocation();

        for (int i = tails.size() - 1; i >= 0; i--) {
            if (i == 0) {
                tails.get(i).setCenterX(this.getCenterX());
                tails.get(i).setCenterY(this.getCenterY());
            } else {
                tails.get(i).setCenterX(tails.get(i - 1).getCenterX());
                tails.get(i).setCenterY(tails.get(i - 1).getCenterY());
            }
        }

        switch (direction) {
            case UP:
                this.setCenterY(this.getCenterY() - STEP);
                break;
            case DOWN:
                this.setCenterY(this.getCenterY() + STEP);
                break;
            case LEFT:
                this.setCenterX(this.getCenterX() - STEP);
                break;
            case RIGHT:
                this.setCenterX(this.getCenterX() + STEP);
                break;
            default:
                break;
        }
    }

    // adjust the location of the snake so that the snake
    // comes out from the other side of the screen if it hits the screen
    private void adjustLocation() {
        if (this.getCenterX() < 0) {
            this.setCenterX(App.WIDTH);
        }
        if (this.getCenterX() > App.WIDTH) {
            this.setCenterX(0);
        }
        if (this.getCenterY() < 0) {
            this.setCenterY(App.HEIGHT);
        }
        if (this.getCenterY() > App.HEIGHT) {
            this.setCenterY(0);
        }
    }

    // get the circle at the tail of the snake
    private Circle endTail() {
        if (tails.size() == 0)
            return this;
        return tails.get(tails.size() - 1);
    }

    // handle eating food
    public void eat(Circle tail) {
        Circle endtail = endTail();

        tail.setCenterX(endtail.getCenterX());
        tail.setCenterY(endtail.getCenterY());
        tail.setRadius(App.RADIUS/1.5);
        tails.add(tail);
    }

    // check if the snake it itself
    public boolean eatItSelf() {
        int length = tails.size();
        for(int i = 3; i < length; i++){
            if(tails.get(i).intersects(this.getBoundsInLocal())){
                return true;
            }
        }

        return false;
    }
}

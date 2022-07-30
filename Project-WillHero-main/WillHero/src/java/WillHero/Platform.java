package WillHero;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Objects;

public class Platform implements Serializable {

    private transient ImageView isLand;

    private final int speed;
    private final int id;
    private double x;
    private double y;
    private final int size;

    public Platform(int id, double x, double y, int size, int speed) {

        this.id = id;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.size = size;

        setIsLnd();
        motion();
    }

    public void setIsLnd() throws NullPointerException {
//        ImageView isLand;
        try {
            this.isLand = new ImageView(new Image(new FileInputStream(Objects.requireNonNull(getClass().getResource("island" + id + ".png")).getPath())));
            this.isLand.setPreserveRatio(true);
            this.isLand.setFitWidth(size);
            this.isLand.setX(x);
            this.isLand.setY(y);
//            return isLand;
        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("Failed to load Platform");
        }
//        return null;
    }

    public ImageView getIsLand() throws NullPointerException {
        return isLand;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }

    public int getSize() {
        return size;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    private void motion() {
        StaticFunction.setTranslation(this.getIsLand(),0,speed,2000,-1);
    }
}
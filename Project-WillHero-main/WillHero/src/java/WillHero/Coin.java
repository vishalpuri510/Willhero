package WillHero;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.Objects;

public class Coin implements Serializable {

    private transient ImageView coin;
    private double x;
    private final double y;

    public Coin(double x, double y) {
        this.x = x;
        this.y = y;
        this.setCoin();
        this.motion();
    }

    public void setCoin() throws NullPointerException {
        try {
            this.coin = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("coin.png"))));
            this.coin.setFitWidth(30);
            this.coin.setPreserveRatio(true);
            this.coin.setX(x);
            this.coin.setY(y);
        }  catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }

    public ImageView getCoin() {
        return coin;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public boolean collision(Hero hero) {
        if (hero.getHero().getBoundsInParent().intersects(coin.getBoundsInParent())) {
            increaseReward(hero);
            this.coin.setVisible(false);
            return true;
        }
        return false;
    }

    private void increaseReward(Hero hero) {
        hero.setReward(hero.getReward() + 1);
    }

    private void motion() {
        RotateTransition r=new RotateTransition();
        r.setNode(coin);
        r.setDuration(Duration.millis(500));
        r.setCycleCount(TranslateTransition.INDEFINITE);
        r.setInterpolator(Interpolator.LINEAR);
        r.setByAngle(360);
        r.setAxis(Rotate.Y_AXIS);
        r.play();
    }
}
package WillHero;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.util.Objects;

public class Coin {

    private final ImageView coin;

    public Coin(int x, int y) {
        this.coin = setCoin(x,y);
        this.motion();
    }

    private ImageView setCoin(int x, int y) throws NullPointerException {
        ImageView coin;
        try {
            coin = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("coin.png"))));
            coin.setFitWidth(35);
            coin.setPreserveRatio(true);
            coin.setX(x);
            coin.setY(y);
            return coin;
        }  catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }

    public ImageView getCoin() {
        return coin;
    }

    public void collision(Hero hero) {
        increaseReward(hero);
        this.coin.setVisible(false);
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
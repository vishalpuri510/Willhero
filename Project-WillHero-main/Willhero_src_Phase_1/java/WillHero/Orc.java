package WillHero;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Objects;

public abstract class Orc {
    private final ImageView orc;
    public Orc(int x, int y) {
        this.orc = setOrc(x,y);
        this.motion();
    }

    private void motion() {
        TranslateTransition r=new TranslateTransition();
        r.setNode(orc);
        r.setDuration(Duration.millis(1000));
        r.setCycleCount(TranslateTransition.INDEFINITE);

        r.setByY(50);
        r.setAutoReverse(true);
        r.play();
    }

    public ImageView getOrc() {
        return orc;
    }

    public abstract  ImageView setOrc(int x,int y);



}


class RedOrc extends Orc {
    private  ImageView redOrc;
    public RedOrc(int x, int y){
        super(x,y);
//        super.
        this.redOrc=super.getOrc();
    }
    @Override
    public ImageView setOrc(int x, int y) throws NullPointerException {
        ImageView redorc;
        try {
            redorc = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("red.png"))));
            redorc.setFitWidth(50);
            redorc.setPreserveRatio(true);
            redorc.setX(x);
            redorc.setY(y);
            return redorc;
        }  catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }


    public void collision(Hero hero) {
        increaseReward(hero);

       this.redOrc.setVisible(false);
    }

    private void increaseReward(Hero hero) {
        hero.setReward(hero.getReward() + 1);
    }



}




class GreenOrc extends Orc {

    private  ImageView greenOrc;
    public GreenOrc(int x, int y) {
        super(x,y);

    }
    @Override
    public ImageView setOrc(int x, int y) throws NullPointerException {
        ImageView greenorc;
        try {
            greenorc = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("green.png"))));
            greenorc.setFitWidth(50);
            greenorc.setPreserveRatio(true);
            greenorc.setX(x);
            greenorc.setY(y);
            return greenorc;
        }  catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }


    public void collision(Hero hero) {
        increaseReward(hero);
        this.greenOrc.setVisible(false);
    }

    private void increaseReward(Hero hero) {
        hero.setReward(hero.getReward() + 1);
    }


}



class BossOrc extends Orc {

    public BossOrc(int x, int y) {
        super(x, y);
    }

    @Override
    public ImageView setOrc(int x, int y) {
        return null;
    }
}


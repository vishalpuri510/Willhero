package WillHero;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Chest  {
    ImageView chest;

    public ImageView setChest(int x, int y) throws NullPointerException {
        try {
            chest = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("chestClosed.png"))));
            chest.setFitWidth(70);
            chest.setPreserveRatio(true);
            chest.setX(x);
            chest.setY(y);
            return chest;
        }  catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }
    public ImageView getChest() {
        return chest;
    }
    public void motion(){
        StaticFunction.setTranslation(chest,0,5,100,-1,true);
    }

    abstract  public  void collision(ImageView object);
    abstract public void getContent(Hero hero);
}


class CoinChest extends Chest{
    ArrayList<Coin> coins = new ArrayList<>();

    @Override
    public void collision(ImageView object) {

    }

    @Override
    public void getContent(Hero hero) {

    }
    private final ImageView coinchest;

    public CoinChest(int x, int y) {
        this.coinchest = super.setChest(x,y);
        super.motion();
    }


    public void collision(Hero hero) {
        increaseReward(hero);
        this.coinchest.setVisible(false);
    }


    private void increaseReward(Hero hero) {
        hero.setReward(hero.getReward() + 1);
    }


}


class WeaponChest extends  Chest{
    Weapon weapon;

    @Override
    public void collision(ImageView object) {

    }

    @Override
    public void getContent(Hero hero) {

    }
}
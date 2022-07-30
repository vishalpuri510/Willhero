package WillHero;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public abstract class Chest implements Serializable {
    private transient ImageView chest;
    private boolean isClose;
    private final double jumpHeight;
    private final double dy;
    private double jumpSpeed;
    private double x;
    private double y;

    public Chest(double x, double y) {
        this.x = x;
        this.y = y;
        setChest();
        this.isClose = true;
        this.jumpHeight = 0.4;
        this.jumpSpeed = 0;
        this.dy = 0.01;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new javafx.animation.KeyFrame(Duration.millis(5), e -> motion()));
        timeline.play();
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setChest() throws NullPointerException {
        try {
            this.chest = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("chestClosed.png"))));
            this.chest.setFitHeight(50);
            this.chest.setPreserveRatio(true);
            this.chest.setX(this.x);
            this.chest.setY(this.y);

        }  catch (Exception e) {
            throw new NullPointerException("Closed chest couldn't be loaded");
        }
    }

    public void setOpenChest(ImageView imageView) throws NullPointerException {
        try {
            imageView.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("chestOpen.png"))));

        }  catch (Exception e) {
            throw new NullPointerException("OpenChest couldn't be loaded");
        }
    }

    public ImageView getChest() {
        return chest;
    }

    public boolean collision(Object object) {

        if (object instanceof Hero hero) {

            if (hero.getHero().getBoundsInParent().intersects(chest.getBoundsInParent())) {
                if(isClose) {
                    isClose = false;
                    setOpenChest(chest);
                    this.getContent(hero);
                    return true;
                }
            }
        }

        if(object instanceof Platform platform) {
            return StaticFunction.bottomCollision(chest, platform.getIsLand(), 4+jumpSpeed*4);
        }

        if(object instanceof Orc orc) {
            return StaticFunction.bottomCollision(chest, orc.getOrc(), 4+jumpSpeed*4);
        }

        if (object instanceof Obstacle obstacle) {
            return StaticFunction.bottomCollision(chest, obstacle.getObstacle(), 4);
        }

        return false;
    }

    public void motion(){
        if(!StaticFunction.getStartit()){
            return;
        }
        this.chest.setY(this.y+=jumpSpeed);
        jumpSpeed += dy;

        for(Platform object : Game.getPlatformList()){
            if(collision(object)){
                jumpSpeed = jumpHeight;
                if (isClose) {
                    jumpSpeed = jumpSpeed > 0 ? -jumpSpeed : jumpSpeed;
                }
                break;
            }
        }
    }

    abstract public void getContent(Hero hero);
}


class CoinChest extends Chest{
    private final Coin [] coins;

    public CoinChest(double x, double y) {
        super(x, y);
        Random random = new Random();
        this.coins = new Coin[random.nextInt(0, 12) + 2];
    }

    @Override
    public void getContent(Hero hero) {
        for(int i = 0; i < coins.length; i++) increaseReward(hero);
    }

    private void increaseReward(Hero hero) {
        hero.setReward(hero.getReward() + 1);
    }
}


class WeaponChest extends  Chest{

    private final Weapon weapon;

    public WeaponChest(double x, double y) {
        super(x, y);
        Random random = new Random();
        Weapon[] weaponList = {new Weapon1(5,100,false, false,0, 0), new Weapon2(3,100,false, false,0 ,0)};
        weapon = weaponList[random.nextInt(0,2)];
    }

    @Override
    public void getContent(Hero hero) {

        if(weapon instanceof Weapon1){
            hero.getHelmet().setChoice(0);
            if(hero.getHelmet().getWeapon(0).isActive()) {
                hero.getHelmet().getWeapon(0).upgrade();
            } else {
                hero.getHelmet().getWeapon(0).setActivate(true, true);
                hero.getHelmet().getWeapon(0).setX(hero.getX() + 5);
                hero.getHelmet().getWeapon(0).setY(hero.getY() + 5);
            }
        }

        if(weapon instanceof Weapon2){
            hero.getHelmet().setChoice(1);
            if(hero.getHelmet().getWeapon(0).isActive()) {
                hero.getHelmet().getWeapon(0).upgrade();
            } else {
                hero.getHelmet().getWeapon(1).setActivate(true, true);
                hero.getHelmet().getWeapon(1).setX(hero.getX());
                hero.getHelmet().getWeapon(1).setY(hero.getHero().getFitHeight() + hero.getY() - 15);
            }
        }
    }
}
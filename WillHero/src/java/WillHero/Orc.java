package WillHero;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public abstract class Orc implements Serializable {

    private transient final ImageView orc;
    private transient Timeline tl;

    private final double jumpHeight;
    private final double dy;
    private double jumpSpeed;
    private double hSpeed;
    private int hp;
    private boolean isAlive;
    private double x;
    private double y;
    private boolean rewarded;


    public Orc(double x, double y, int hp) {
        Random random = new Random();
        this.hp = hp;
        this.isAlive = true;
        this.orc = setOrc(x, y);
        this.jumpHeight = random.nextDouble(0.8, 1.2);
        this.jumpSpeed = 0;
        this.hSpeed = 0;
        this.dy = random.nextDouble(0.008, 0.01);
        this.x = x;
        this.y = y;

        this.tl = new Timeline(new KeyFrame(Duration.millis(5), e -> jump()));
        this.tl.setCycleCount(Timeline.INDEFINITE);
        this.tl.play();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void jump() {
        ArrayList<Object> objects = new ArrayList<>();
        try {

            objects.addAll(Game.getPlatformList());
            objects.addAll(Game.getChestList());
            objects.addAll(Game.getOrcList());
            objects.addAll(Game.getHeroWrap());
        }
        catch (NullPointerException e) {
            System.out.println("One of the arraylist is null in orc");
        }


        if (!StaticFunction.getStartit()) {
            return;
        }

        this.orc.setY(y += jumpSpeed);

        if (hSpeed > 0) {
            hSpeed -= 0.01;
            this.orc.setX(x += hSpeed);
        } else if (hSpeed < 0) {
            hSpeed += 0.01;
            this.orc.setX(x -= this.dy);
        }

        jumpSpeed += dy;
        if (hp <= 0) {
            this.setAlive(false);
        }

        for (Object object : objects) {
            if (collision(object)) {
                jumpSpeed = jumpHeight;
                if (isAlive) {
                    jumpSpeed = jumpSpeed > 0 ? -jumpSpeed : jumpSpeed;
                    break;
                }
            }
        }
    }

    public void destroy() {
        if (!isAlive) {
            if (tl != null && tl.getStatus() == Animation.Status.RUNNING) this.tl.stop();

            Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("marneKeBad.png")));
            this.orc.setImage(image);
            StaticFunction.setRotation(this.orc, 500, -1, false);
            tl = new Timeline(new KeyFrame(Duration.millis(5), e -> {
                this.orc.setY(this.orc.getY() + 2);
                this.orc.setX(this.orc.getX() + 0.5);
            }));
            tl.setCycleCount(Timeline.INDEFINITE);
            tl.play();
        }
    }

    public ImageView getOrc() {
        return orc;
    }

    public abstract ImageView setOrc(double x, double y);

    public boolean collision(Object object) {

        if (object instanceof Platform platform) {
//            top side collision with platform
            if (StaticFunction.bottomCollision(orc, platform.getIsLand(), 8 * jumpSpeed + 3)) {
                return true;
            }
        }

        if (object instanceof Orc orci) {
            if (StaticFunction.bottomCollision(orc, orci.getOrc(), 4 * jumpSpeed + 3)) {
                return true;
            }
        }

        if (object instanceof Obstacle tnt) {

            if (orc.getBoundsInParent().intersects(tnt.getObstacle().getBoundsInParent()) && ((Tnt) tnt).isBlast()) {
                isAlive = false;
                destroy();
            }

            // Bottom side collision of orc with tnt top
            if (StaticFunction.bottomCollision(orc, tnt.getObstacle(), 4 * jumpSpeed + 3)) {

                if ((((Tnt) tnt).getHitCount()) == 0) {
                    ((Tnt) tnt).setHitCount(1);
                }
                if (((Tnt) tnt).isBlast()) {
                    isAlive = false;
                    destroy();
                }
                return true;
            }
        }


        if (object instanceof Chest chest) {
            // Bottom side collision of hero with orc top
            if (StaticFunction.bottomCollision(orc, chest.getChest(), 4 * jumpSpeed + 3)) {
                return true;
            }

            // Right side collision of hero with orc left
            if (StaticFunction.rightCollision(orc, chest.getChest(), 12 * Game.getHSpeed() + 10)) {
                chest.setX(chest.getX() + 5);
                chest.getChest().setX(chest.getX());
            }

            // Left side collision of hero with orc right
            if (StaticFunction.leftCollision(orc, chest.getChest(), 12 * Game.getHSpeed() + 10)) {
                chest.setX(chest.getX() - 5);
                chest.getChest().setX(chest.getX());
            }
        }

        if (object instanceof Hero hero) {
//             Left side collision of orc with hero right
            if (StaticFunction.leftCollision(orc, hero.getHero(), 12 * Game.getHSpeed() + 10)) {
                Game.sethSpeed(0);
                hSpeed = 1.5;
            }

//             right side collision of orc with hero right
            if (StaticFunction.rightCollision(orc, hero.getHero(), 12 * Game.getHSpeed() + 10)) {
                Game.sethSpeed(0);
                hSpeed = -1.5;
            }

            // Bottom side collision of orc with hero top
            return StaticFunction.bottomCollision(orc, hero.getHero(), 4 * jumpSpeed + 3);
        }
        return false;
    }

    public abstract void increaseReward(Hero hero);

    public boolean isRewarded() {
        return rewarded;
    }

    public void setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
    }
}


class RedOrc extends Orc {

    public RedOrc(double x, double y) {
        super(x, y, 100);
    }

    @Override
    public ImageView setOrc(double x, double y) throws NullPointerException {
        ImageView redOrc;
        try {
            redOrc = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("red.png"))));
            redOrc.setFitHeight(40);
            redOrc.setPreserveRatio(true);
            redOrc.setX(x);
            redOrc.setY(y);
            return redOrc;

        } catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }

    public void increaseReward(Hero hero) {
        if (super.isRewarded()) return;
        super.setRewarded(true);
        hero.setReward(hero.getReward() + 2);
    }
}


class GreenOrc extends Orc {

    public GreenOrc(double x, double y) {
        super(x, y, 80);
    }

    @Override
    public ImageView setOrc(double x, double y) throws NullPointerException {
        ImageView greenOrc;
        try {
            greenOrc = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("green.png"))));
            greenOrc.setFitHeight(40);
            greenOrc.setPreserveRatio(true);
            greenOrc.setX(x);
            greenOrc.setY(y);
            return greenOrc;
        } catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }

    @Override
    public void increaseReward(Hero hero) {
        if (super.isRewarded()) return;
        super.setRewarded(true);
        hero.setReward(hero.getReward() + 1);

    }
}


class BossOrc extends Orc {

    private double hSpeed;

    public BossOrc(double x, double y) {
        super(x, y, 450);

        this.hSpeed = 0;
    }

    public void sethSpeed(double hSpeed) {
        this.hSpeed = hSpeed;
    }

    @Override
    public ImageView setOrc(double x, double y) throws NullPointerException {
        ImageView redOrc;
        try {
            redOrc = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("bossOrc.png"))));
            redOrc.setFitHeight(80);
            redOrc.setPreserveRatio(true);
            redOrc.setX(x);
            redOrc.setY(y);
            return redOrc;

        } catch (Exception e) {
            throw new NullPointerException("Coin couldn't be loaded");
        }
    }

    @Override
    public void jump() {
        super.jump();
        super.getOrc().setTranslateX(super.getOrc().getTranslateX() + hSpeed);
    }

    @Override
    public void increaseReward(Hero hero) {
        if (super.isRewarded()) return;
        super.setRewarded(true);
        hero.setReward(hero.getReward() + 5);
    }
}
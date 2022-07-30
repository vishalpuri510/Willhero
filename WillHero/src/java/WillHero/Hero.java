package WillHero;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Hero implements Serializable {

    private transient ImageView hero;
    private final transient Timeline tl;
    private final transient MediaPlayer  attackMedia;

    private double jumpSpeed;
    private final Helmet helmet;
    private final String name;
    private boolean isAlive;
    private int location;
    private int reward;
    private boolean resurrected;

    private double x;
    private double y;

    public Hero(String name, int location, int reward, boolean isAlive, boolean isResurrected, Helmet helmet, double jumpSpeed, double x, double y) {
        this.isAlive = isAlive;
        this.name = name;
        this.resurrected = isResurrected;
        this.location = location;
        this.reward = reward;
        this.helmet = helmet;
        this.jumpSpeed = jumpSpeed;
        this.x = x;
        this.y = y;

        Media  attackSound;


        attackSound = new Media(new File(Objects.requireNonNull(getClass().getResource("fukfuk.mp3")).getFile()).toURI().toString());
        attackMedia = new MediaPlayer(attackSound);
        attackMedia.setStartTime(Duration.millis(10));
        attackMedia.setCycleCount(1);
        attackMedia.setOnEndOfMedia(() -> {
            attackMedia.seek(Duration.ZERO);
            if(attackMedia.getStatus().equals(MediaPlayer.Status.PLAYING))
                attackMedia.pause();
        });

        this.setHero();

        tl = new Timeline(new KeyFrame(Duration.millis(2), e -> jump()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    public void setHero() {
        try {
            this.hero = new ImageView(new Image(new FileInputStream(Objects.requireNonNull(getClass().getResource("hero.png")).getPath())));
            this.hero.setPreserveRatio(true);
            this.hero.setFitHeight(40);
            this.hero.setX(x);
            this.hero.setY(y);

        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("Failed to load hero");
            e.printStackTrace();
        }
    }

    public void useWeapon() {
        if (helmet.getWeapon(helmet.getChoice()).isActive() && helmet.getWeapon(helmet.getChoice()).isVisible()) {
            attackMedia.play();
            if(!attackMedia.getStatus().equals(MediaPlayer.Status.PLAYING)){
                attackMedia.play();
            }
            helmet.getWeapon(helmet.getChoice()).attack(this);
        }
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public Timeline getTl() {
        return tl;
    }

    public String getName() {
        return name;
    }

    public ImageView getHero() {
        return hero;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setResurrected(boolean resurrected) {
        this.resurrected = resurrected;
    }

    public boolean isResurrected() {
        return resurrected;
    }

    // Life and Death
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }
    // End of life and death

    public Helmet getHelmet() {
        return helmet;
    }

    // Jumping
    public void jump() throws NullPointerException {
        ArrayList<Object> objects = new ArrayList<>();
        try {
            objects.addAll(Game.getPlatformList());
            objects.addAll(Game.getOrcList());
            objects.addAll(Game.getTntList());
        }
        catch(NullPointerException e){
            System.out.println("One of the arraylist is null in hero");
        }

        if(!StaticFunction.getStartit()){
            return;
        }

        this.hero.setY(y+=jumpSpeed);
        this.helmet.getWeapon(0).setY(hero.getY() + jumpSpeed);
        this.helmet.getWeapon(1).setY(this.helmet.getWeapon(1).getWeaponImage().getY() + jumpSpeed);
        double dy = 0.005;
        jumpSpeed += dy;



        for (Object object : objects) {

            if (collision(object)) {
                jumpSpeed = 0.9;
                if (isAlive) {

                    StaticFunction.setScaling(hero);
                    jumpSpeed = jumpSpeed > 0 ? -jumpSpeed : jumpSpeed;
                } else {
                    jumpSpeed = 0;
                }
                break;
            }
        }
    }

    private boolean collision(Object object) {

        // Hero's Collision with Platform
        if (object instanceof Platform platform) {
            // Hero's base collide with top the top edge of Platform
            if (StaticFunction.bottomCollision(hero, platform.getIsLand(), jumpSpeed*3)) {
                return true;
            }

            // Hero's right collides with the left edge of Platform
            if (StaticFunction.rightCollision(hero, platform.getIsLand(), 4*Game.getHSpeed())) {
                return true;
            }
        }

        if (object instanceof Obstacle tnt) {

            if(hero.getBoundsInParent().intersects(tnt.getObstacle().getBoundsInParent()) && ((Tnt)tnt).isBlast()){
                isAlive = false;
            }

            // Bottom side collision of hero with obstacle top
            if (StaticFunction.bottomCollision(hero, tnt.getObstacle(), 3*jumpSpeed)) {
                if((((Tnt)tnt).getHitCount())==0){
                    ((Tnt)tnt).setHitCount(1);
                }
                if(((Tnt)tnt).isBlast()) isAlive=false;

                return true;
            }
        }

        // Collision with orc
        if (object instanceof Orc orc) {

            // Bottom side collision of hero with orc top
            if (StaticFunction.bottomCollision(hero, orc.getOrc(), Math.abs(3*(jumpSpeed - orc.getJumpSpeed())) + 3)) {
                return true;
            }

            // Top side collision of hero with orc bottom
            if (StaticFunction.topCollision(hero, orc.getOrc(), Math.abs(3*(jumpSpeed - orc.getJumpSpeed())) + 3)) {
                isAlive = false;
                return true;
            }
        }
        return false;
    }
    // End of collision
}
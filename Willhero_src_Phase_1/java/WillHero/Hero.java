package WillHero;

import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Hero {

    private final Label name;
    private final ImageView hero;
    private final Helmet helmet;

    private boolean isAlive;
    private int reward;
    private int location;

    public Hero(Label name, int location) {
        isAlive = true;
        this.name = name;
        this.location=location;
        this.hero = setHero();
        this.helmet = new Helmet();
    }

    private ImageView setHero(){
        ImageView hero;
        try {
            hero = new ImageView(new Image(new FileInputStream(Objects.requireNonNull(getClass().getResource("hero.png")).getPath())));
            hero.setPreserveRatio(true);
            hero.setFitWidth(70);
            hero.setX(410);
            hero.setY(150);

        } catch (FileNotFoundException | NullPointerException e) {
            hero = null;
            System.out.println("Failed to load hero");
            e.printStackTrace();
        }

        return hero;
    }

    public Label getName() {
        return name;
    }

    public ImageView getHero() throws NullPointerException {
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

    public void setScoreLabel(Label reward, Label location) {
        this.location += 1;
        this.location += 1;
        reward.setText(String.valueOf(this.reward));
        location.setText(String.valueOf(this.location));
    }


    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }


    public void jump() {

        float height = (float) hero.getY() - 180;
        int time  = 4;
        hero.setY(height);
    }

    public double getSpeed() {
        return -1;
    }

}
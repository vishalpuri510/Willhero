package WillHero;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.io.Serializable;
import java.util.Objects;

public abstract class Weapon implements Serializable {

    private final transient ImageView weaponImage;
    private boolean active;
    private boolean visible;

    private int damage;
    private int range;

    private double x;
    private double y;


    public Weapon(int damage, int range, boolean active, boolean visible, double x, double y) {
        this.damage = damage;
        this.range = range;
        this.active = active;
        this.visible = visible;
        this.x = x;
        this.y = y;
        this.weaponImage = setWeaponImage();
    }

    public void setX(double x) {
        this.x = x;
        weaponImage.setX(x);
    }

    public void setY(double y) {
        this.y = y;
        weaponImage.setY(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public abstract ImageView setWeaponImage();

    public ImageView getWeaponImage() {
        return weaponImage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range){
        this.range = range;
    }

    public void collision(Orc orc) {
        if (orc.getOrc().getBoundsInParent().intersects(weaponImage.getBoundsInParent()) && visible && active) {
            orc.setHp(orc.getHp() - damage);
        }
    }

    public void setActivate(boolean active, boolean visible) {
        this.visible = visible;
        this.active = active;
        this.getWeaponImage().setVisible(visible);
    }

    public boolean isActive() {
        return active;
    }

    public abstract void attack(Hero hero);

    public abstract void upgrade();
}

class Weapon1 extends Weapon implements Cloneable{

    private transient Timeline fTimeline;
    private transient Timeline bTimeline;
    public Weapon1(int damage, int range, boolean active, boolean visible, double x, double y) {
        super(damage, range, active, visible, x, y);
        this.fTimeline = null;
        this.bTimeline = null;
        StaticFunction.setRotation(this.getWeaponImage(), 100, -1, false);
    }

    @Override
    public Weapon clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Weapon) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public ImageView setWeaponImage() {

        try {
            ImageView weapon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Shuriken.png"))));
            weapon.setFitWidth(25);
            weapon.setX(getX());
            weapon.setY(getY());
            weapon.setPreserveRatio(true);
            weapon.setVisible(isVisible());
            return weapon;

        } catch (Exception e) {
            throw new NullPointerException("Closed chest couldn't be loaded");
        }
    }

    @Override
    public void attack(Hero hero) {
//        System.out.println("Weapon 1 attack");
        Weapon1 c_weapon = (Weapon1) this.clone();

        if (fTimeline != null && fTimeline.getStatus() == Animation.Status.RUNNING) {
            fTimeline.stop();
        }
        if (bTimeline != null && bTimeline.getStatus() == Animation.Status.RUNNING) {
            bTimeline.stop();
        }

        c_weapon.getWeaponImage().setX(super.getX());

        fTimeline = (new Timeline(new KeyFrame(Duration.millis(0.8), fEvent -> attackAnimation(c_weapon, 1))));
        fTimeline.setCycleCount(getRange());
        fTimeline.setOnFinished(e -> {
            bTimeline = new Timeline(new KeyFrame(Duration.millis(0.8), bEvent -> attackAnimation(c_weapon, -1)));
            bTimeline.setCycleCount(getRange());
            bTimeline.play();
        });

        fTimeline.play();
    }

    @Override
    public void upgrade() {
        super.setRange(getRange()+5);
        super.setDamage(getDamage()+10);
    }

    private void attackAnimation(Weapon c_weapon, int i) {
//        System.out.println("Weapon 1 attack in progress");
        c_weapon.getWeaponImage().setX(c_weapon.getWeaponImage().getX() + i);
    }
}

class Weapon2 extends Weapon {
    private transient Timeline fTimeline;
    private transient Timeline bTimeline;

    public Weapon2(int damage, int range, boolean active, boolean visible, double x, double y) {
        super(damage, range, active, visible, x, y);
        this.fTimeline = null;
        this.bTimeline = null;
    }

    @Override
    public ImageView setWeaponImage() {

        try {
            ImageView weapon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Lance.png"))));
            weapon.setFitWidth(50); // 150 the max scaling
            weapon.setFitHeight(10); // 10 the max scaling
            weapon.setX(getX());
            weapon.setY(getY());
            weapon.setVisible(isVisible());
            return weapon;

        } catch (Exception e) {
            throw new NullPointerException("Closed chest couldn't be loaded");
        }
    }

    @Override
    public void attack(Hero hero) {
//        System.out.println("Weapon 2 attack");
        if (fTimeline != null && fTimeline.getStatus() == Animation.Status.RUNNING) {
            fTimeline.stop();
        }
        if (bTimeline != null && bTimeline.getStatus() == Animation.Status.RUNNING) {
            bTimeline.stop();
        }

        this.getWeaponImage().setFitWidth(50);

        fTimeline = (new Timeline(new KeyFrame(Duration.millis(0.8), fEvent -> attackAnimation( 1))));
        fTimeline.setCycleCount(getRange());
        fTimeline.setOnFinished(e -> {
            bTimeline = new Timeline(new KeyFrame(Duration.millis(0.8), bEvent -> attackAnimation( -1)));
            bTimeline.setCycleCount(getRange());
            bTimeline.play();
        });

        fTimeline.play();
    }

    private void attackAnimation(int i) {
        this.getWeaponImage().setFitWidth(this.getWeaponImage().getFitWidth() + i);
    }

    @Override
    public void upgrade() {
        super.setRange(getRange()+10);
        super.setDamage(getDamage()+10);
    }
}
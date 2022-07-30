package WillHero;

import javafx.scene.image.ImageView;

public abstract class Weapon {

    private final ImageView weaponImage;
    private final int damage;
    private final int range;
    private boolean active = false;

    public Weapon(ImageView weaponImage, int damage, int range) {
        this.weaponImage = weaponImage;
        this.damage = damage;
        this.range = range;
    }

    public ImageView getWeaponImage() {
        return weaponImage;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public boolean collided(ImageView object){
        //to  implimented
        return true;
    }

    public void setActivate(){
        active = true;
    }

    public boolean isActive(){
        return active;
    }
    public abstract void attack(ImageView character);
    public abstract void upgrade(ImageView character);
}


class Weapon1 extends Weapon {

    public Weapon1() {
        super(new ImageView("weapon1.png"), 10, 10);
    }

    @Override
    public void attack(ImageView character) {
        System.out.println("Weapon 1 attack");
    }

    @Override
    public void upgrade(ImageView character) {

    }
}


class Weapon2 extends Weapon {

    public Weapon2(ImageView weaponImage, int damage, int range) {
        super(weaponImage, damage, range);
    }

    @Override
    public void attack(ImageView character) {

    }

    @Override
    public void upgrade(ImageView character) {

    }
}

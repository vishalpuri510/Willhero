package WillHero;

import java.io.Serializable;
import java.util.ArrayList;

public class Helmet implements Serializable {

    private final ArrayList<Weapon> weapons;
    private int choice;

    public Helmet(Weapon weapon1, Weapon weapon2, int choice) {
        weapons = new ArrayList<>();
        weapons.add(weapon1);
        weapons.add(weapon2);
        this.choice = choice;

    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public  Weapon getWeapon(int index){
        return weapons.get(index);
    }
}
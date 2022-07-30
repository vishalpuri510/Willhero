package WillHero;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReGenerateHero {
    private Hero generatedHero;

    public Hero getHero(String fileName) throws IOException, ClassNotFoundException {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            try {
                Hero hero = (Hero) in.readObject();
                Weapon weapon1 = new Weapon1(hero.getHelmet().getWeapon(0).getDamage(), hero.getHelmet().getWeapon(0).getRange(), hero.getHelmet().getWeapon(0).isActive(), hero.getHelmet().getWeapon(0).isVisible(), hero.getHelmet().getWeapon(0).getX(), hero.getHelmet().getWeapon(0).getY());
                Weapon weapon2 = new Weapon2(hero.getHelmet().getWeapon(1).getDamage(), hero.getHelmet().getWeapon(1).getRange(), hero.getHelmet().getWeapon(1).isActive(), hero.getHelmet().getWeapon(1).isVisible(), hero.getHelmet().getWeapon(1).getX(), 25 + hero.getY());

                Helmet helmet = new Helmet(weapon1, weapon2, hero.getHelmet().getChoice());
                generatedHero = new Hero(hero.getName(), hero.getLocation(), hero.getReward(), hero.isAlive(), hero.isResurrected(), helmet, hero.getJumpSpeed(), hero.getX(), hero.getY());

            } catch (ClassCastException e) {
                System.out.println("Hero: Invalid Cast Exception");
            }
        }
        return generatedHero;
    }
}

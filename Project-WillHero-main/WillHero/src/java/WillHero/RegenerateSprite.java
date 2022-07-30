package WillHero;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class RegenerateSprite {

    private final ArrayList<Platform> generatedPlatforms;
    private final ArrayList<Coin> generatedCoins;
    private final ArrayList<Obstacle> generatedObstacles;
    private final ArrayList<Chest> generatedChests;
    private final ArrayList<Orc> generatedOrcs;

    private ArrayList<Object> gameObjects;

    public RegenerateSprite(ArrayList<Platform> generatedPlatforms, ArrayList<Coin> generatedCoins,
                            ArrayList<Obstacle> generatedObstacles, ArrayList<Chest> generatedChests,
                            ArrayList<Orc> generatedOrcs) {
        this.generatedPlatforms = generatedPlatforms;
        this.generatedCoins = generatedCoins;
        this.generatedObstacles = generatedObstacles;
        this.generatedChests = generatedChests;
        this.generatedOrcs = generatedOrcs;
    }

    private void deserialize(String fileName) throws IOException, ClassNotFoundException {
        gameObjects = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    Object tmp = in.readObject();
                    gameObjects.add(tmp);
                } catch (EOFException e) {
                    break;
                } catch (ClassCastException e) {
                    System.out.println("Invalid Class Cast Exception");
                }
            }
        }
    }

    //    public int p=0;
    private void getSprites(String fileName) throws IOException, ClassNotFoundException {
        deserialize(fileName);

        for(Object obj : gameObjects) {
            if(obj instanceof Platform platform) {
                Platform _platform = new Platform(platform.getId(), platform.getX(), platform.getY(), platform.getSize(), platform.getSpeed());
                generatedPlatforms.add(_platform);


            } else if(obj instanceof Coin coin) {
                Coin _coin = new Coin(coin.getX(), coin.getY());
                generatedCoins.add(_coin);

            } else if(obj instanceof Chest chest) {
                Chest _chest = null;
                if(chest instanceof CoinChest) {
                    _chest = new CoinChest(chest.getX(), chest.getY());
                } else if(chest instanceof WeaponChest) {
                    _chest = new WeaponChest(chest.getX(), chest.getY());
                }
                generatedChests.add(_chest);
            }

            else if(obj instanceof Obstacle obstacle) {
                Obstacle _obstacle = null;
                if (obstacle instanceof Tnt) {
                    _obstacle = new Tnt(obstacle.getX(), obstacle.getY());
                }
                generatedObstacles.add(_obstacle);

            } else if(obj instanceof Orc orc) {
                Orc _orc = null;
                if (orc instanceof GreenOrc) {
                    _orc  = new GreenOrc(orc.getX(), orc.getY());
                } else if(orc instanceof RedOrc) {
                    _orc  = new RedOrc(orc.getX(), orc.getY());
                } else if (orc instanceof BossOrc) {
                    _orc  = new BossOrc(orc.getX(), orc.getY());
                }
                generatedOrcs.add(_orc);
            }

            else {
                System.out.println("Invalid Object");
            }
        }
    }

    public void regenerate(String fileName) throws IOException, ClassNotFoundException {
        getSprites(fileName);
    }
}

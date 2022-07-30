package WillHero;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LeaderBoard implements Initializable {
    @FXML
    private  TableView<Hero> table;
    @FXML
    private TableColumn<Hero, String> name;
    @FXML
    private TableColumn<Hero, Integer> reward;
    @FXML
    private TableColumn<Hero, Integer> location;
    @FXML
    private ImageView backIcon;

    private ArrayList<Hero> heroes;


    public void start() throws IOException {
        this.heroes = new ArrayList<>();
    }

    private void loadHeroes() {
        heroes=StaticFunction.getHeroes();
    }

    private void printHeroes() {
//        for(Hero hero : heroes)
//            System.out.println("Name: " + hero.getName() + " Location: " + hero.getLocation() + " Reward: " + hero.getReward());

    }

    public void setBack(MouseEvent backIcon) throws IOException {
        StaticFunction.clickResponse(this.backIcon);

        URL toScene = getClass().getResource("MainMenu.fxml");
        StaticFunction.setScene(StaticFunction.getStage(backIcon), toScene, "WillHero: Main Menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new ReGenerateHero();

        loadHeroes();
        printHeroes();
        StaticFunction.setHeroTable(table, heroes, name, reward, location);
    }
}
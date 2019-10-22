package me.tfkjake.cad.creator;

import me.tfkjake.cad.CardsAgainstDiscord;
import me.tfkjake.cad.game.Game;
import me.tfkjake.cad.game.GameManager;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class CreatorInstance {

    private User creator;
    private Category category;
    private TextChannel setup;
    private CreationStage stage;
    private boolean isPrivate;

    private int maxScore, maxPlayers;
    private List<User> players = new ArrayList<>();
    private String joinCode;

    public CreatorInstance(User creator, Category category, TextChannel setup){
        this.creator = creator;
        this.category = category;
        this.setup = setup;
        this.stage = CreationStage.PRIVATE;
        this.players.add(creator);
    }

    public void setIsPrivate(boolean isPrivate){
        this.isPrivate = isPrivate;
    }

    public void addPlayer(User user){
        this.players.add(user);
    }

    public List<User> getPlayers(){
        return players;
    }

    public void setMaxScore(int maxScore){
        this.maxScore = maxScore;
    }

    public void setMaxPlayers(int maxPlayers){
        this.maxPlayers = maxPlayers;
    }

    public CreationStage getStage() {
        return stage;
    }

    public User getCreator() {
        return creator;
    }

    public String getJoinCode(){ return joinCode; }

    public Category getCategory() {
        return category;
    }

    public TextChannel getSetup() {
        return setup;
    }

    public void setStage(CreationStage stage) {
        this.stage = stage;
    }

    public void completeSetup(){
        joinCode = RandomStringUtils.randomAlphanumeric(6);
        setup.sendMessage("Your Game Code is **" + joinCode + "**. People can use this code to join your game using **cad!join " + joinCode + "**!\n\nWhen you're ready to start, reply **start** here.").queue();
    }

    public void startGame(){
        Game game = new Game(category, creator, maxScore, players);
        GameManager.addGame(game);
        CreatorManager.removeCreator(this);
    }

}

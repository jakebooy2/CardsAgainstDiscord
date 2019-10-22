package me.tfkjake.cad.game;

import java.util.List;

public class GameManager {

    private static List<Game> games;

    public static void addGame(Game game){
        if(!games.contains(game))
            games.add(game);
    }

    public void removeGame(Game game){
        if(games.contains(game))
            games.remove(game);
    }

    public void getGame(String channelId){
        for(Game game : games){

        }
    }

}

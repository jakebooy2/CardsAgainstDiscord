package me.tfkjake.cad.command;

import me.tfkjake.cad.CardsAgainstDiscord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {


    List<Command> commands = new ArrayList<>();

    private CardsAgainstDiscord cardsAgainstDiscord;
    public CommandManager(CardsAgainstDiscord cardsAgainstDiscord){
        this.cardsAgainstDiscord = cardsAgainstDiscord;
    }

    public Command getCommand(String name){
        for(Command c : commands){
            if(c.getName().equalsIgnoreCase(name))
                return c;
        }
        return null;
    }

    public List<Command> getCommands(){
        return commands;
    }

    public void registerCommand(String name, AbstractCommand executor){
        if(getCommand(name) != null) return;

        this.commands.add(new Command(name, executor));
    }


}

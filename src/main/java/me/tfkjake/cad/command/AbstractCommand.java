package me.tfkjake.cad.command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public abstract class AbstractCommand {

    private String name;
    public AbstractCommand(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public abstract void execute(Guild server, Message message, User user, String[] args);

}

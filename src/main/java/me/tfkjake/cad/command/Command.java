package me.tfkjake.cad.command;

import java.util.ArrayList;
import java.util.List;

public class Command {


    String name;
    String help;
    List<String> aliases = new ArrayList<>();
    AbstractCommand executor;

    public Command(String name, AbstractCommand executor){
        this.name = name;
        this.executor = executor;
    }

    public String getName(){
        return name;
    }

    public void setHelp(String help){
        this.help = help;
        Sheepy.getStaticSQL().update("UPDATE commands SET help = ? WHERE command = ?", help, name);
    }

    public void addAlias(String alias){
        if(!aliases.contains(alias))
            aliases.add(alias);
    }

    public void removeAlias(String alias){
        if(aliases.contains(alias))
            aliases.remove(alias);
    }

    public List<String> getAliases(){
        return aliases;
    }

    public String getHelp(){
        return help;
    }

    public void setExecutor(AbstractCommand executor){
        this.executor = executor;
    }

    public AbstractCommand getExecutor(){
        return executor;
    }

}

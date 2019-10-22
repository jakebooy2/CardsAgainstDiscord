package me.tfkjake.cad;

import me.tfkjake.cad.command.CommandManager;
import me.tfkjake.cad.commands.Create;
import me.tfkjake.cad.event.MessageEvent;
import me.tfkjake.cad.misc.MySQL;
import me.tfkjake.cad.misc.Properties;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

public class CardsAgainstDiscord {

    private static JDA discord;
    private static CommandManager commandManager;
    private static Properties properties = new Properties();
    private static MySQL mySQL;

    private CardsAgainstDiscord(String token){

        try {
            discord = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setAutoReconnect(true)
                    .setGame(Game.watching("you sleep"))
                    .addEventListener(new MessageEvent())
                    .buildBlocking();
        }catch(Exception e){
            e.printStackTrace();
        }

        commandManager = new CommandManager(this);
        commandManager.registerCommand("create", new Create(this));

        mySQL = new MySQL(this);
        mySQL.connect();

        for(Guild guild : discord.getGuilds()){
            for(Category category : guild.getCategories()){
                if(category.getName().startsWith("[CADBOT] ")){
                    for(Channel channel : category.getChannels()){
                        channel.delete().queue();
                    }
                    category.delete().queue();
                }
            }
        }

    }

    public static void main(String[] args){
        new CardsAgainstDiscord(properties.getString("token"));
    }

    public static JDA getDiscord(){
        return discord;
    }

    public Properties getProperties() {
        return properties;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static MySQL getMySQL(){
        return mySQL;
    }
}

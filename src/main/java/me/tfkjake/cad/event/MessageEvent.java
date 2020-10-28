package me.tfkjake.cad.event;

import me.tfkjake.cad.command.CommandHandler;
import me.tfkjake.cad.creator.CreationStage;
import me.tfkjake.cad.creator.CreatorInstance;
import me.tfkjake.cad.creator.CreatorManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
public class MessageEvent extends CommandHandler {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot())
            return;

        if (CreatorManager.getCreationStage(e.getTextChannel()) != null) {
            CreatorInstance instance = CreatorManager.getCreationStage(e.getTextChannel());
            if(instance.getCreator() != e.getAuthor())
                return;
            if(instance.getStage() == CreationStage.PRIVATE){
                if(e.getMessage().getContentRaw().toLowerCase().equalsIgnoreCase("private")){
                    e.getTextChannel().sendMessage("Sweet! Your game room won't show up in the list of games.\n\nNow.. what score limit do ya want to set? This is the amount of points needed for a player to win.\n\n*Reply with your answer.*").queue();
                    instance.setIsPrivate(true);
                    instance.setStage(CreationStage.MAX_POINTS);
                    return;
                }
                if(e.getMessage().getContentRaw().toLowerCase().equalsIgnoreCase("public")){
                    e.getTextChannel().sendMessage("Sweet! Your game room will show up in the list of games.\n\nNow.. what score limit do ya want to set? This is the amount of points needed for a player to win.\n\n*Reply with your answer.*").queue();
                    instance.setIsPrivate(false);
                    instance.setStage(CreationStage.MAX_POINTS);
                    return;
                }
                e.getTextChannel().sendMessage("Please reply with either **public** or **private**!").queue();
            }
            if (instance.getStage() == CreationStage.MAX_POINTS) {
                int score;
                try {
                    score = Integer.parseInt(e.getMessage().getContentRaw());
                } catch (NumberFormatException e1) {
                    e.getChannel().sendMessage("Please ensure the value entered for **MAXIMUM_SCORE** is an integer!").queue();
                    return;
                }
                if (score > 10 || score < 3) {
                    e.getChannel().sendMessage("Your maximum score must be between 3 and 10!").queue();
                    return;
                }
                instance.setMaxScore(score);
                instance.setStage(CreationStage.PLAYERS);
                //TODO: Donor check
                e.getChannel().sendMessage("Gotcha! The amount of points needed to win is **" + score + "**.\n\n" +
                        "Next up you need to specify the maximum players you want to be able to join the game.\n\nAs you're not a donator, you can set a minimum of 3 and maximum of 5. Donate to get a maximum of 8!").queue();
                return;
            }
            if (instance.getStage() == CreationStage.PLAYERS) {
                int players;
                try {
                    players = Integer.parseInt(e.getMessage().getContentRaw());
                } catch (NumberFormatException e1) {
                    e.getChannel().sendMessage("Please ensure the value entered for **MAXIMUM_PLAYERS** is an integer!").queue();
                    return;
                }
                if (players < 3) {
                    e.getChannel().sendMessage("You can't have a CAD game with less than 3 people!").queue();
                    return;
                }
                if (players > 5) { // TODO: Donor check
                    e.getChannel().sendMessage("Sorry, but you have to donate to get more than 5 players!").queue();
                    return;
                }
                e.getChannel().sendMessage("Alright! The maximum amount of players that can join is **" + players + "**.").queue();
                instance.setStage(CreationStage.FINISHED);
                instance.completeSetup();
                return;
            }
            if(instance.getStage() == CreationStage.FINISHED){
                if(e.getMessage().getContentRaw().equalsIgnoreCase("start")){
                    if(instance.getPlayers().size() < 2){
                        e.getChannel().sendMessage("You need 3 or more players (including yourself) to start!").queue();
                        return;
                    }
                    instance.startGame();
                    return;
                }
                e.getChannel().sendMessage("You can invite players by telling them to run **cad!join " + instance.getJoinCode() + "**, or reply with **start** to begin.").queue();
                return;
            }
        }
        handle(e.getGuild(), e.getMessage(), e.getAuthor(), e.getMessage().getContentRaw());

    }
}

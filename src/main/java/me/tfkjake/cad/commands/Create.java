package me.tfkjake.cad.commands;

import me.tfkjake.cad.CardsAgainstDiscord;
import me.tfkjake.cad.command.AbstractCommand;
import me.tfkjake.cad.creator.CreatorInstance;
import me.tfkjake.cad.creator.CreatorManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

public class Create extends AbstractCommand {

    private CardsAgainstDiscord cardsAgainstDiscord;
    public Create(CardsAgainstDiscord cardsAgainstDiscord){
        super("create");
        this.cardsAgainstDiscord = cardsAgainstDiscord;
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        CreatorInstance testfor = CreatorManager.getCreationStage(user);
        if(testfor != null){
            message.getTextChannel().sendMessage("You've already started setup! Head over to " + testfor.getSetup().getAsMention() + " to complete setup.").queue();
            return;
        }

        Category category = (Category)server.getController().createCategory("[CADBOT] " + user.getName()).complete();
        TextChannel channel = (TextChannel)category.createTextChannel("setup").complete();
        channel.createPermissionOverride(server.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
        channel.createPermissionOverride(message.getMember()).setAllow(Permission.VIEW_CHANNEL).queue();

        channel.sendMessage(
                "**Heya, " + user.getAsMention() + "!** You're one step closer to beginning your Cards Against Discord game.\n\n" +
                        "First of all, would you like your game to be **public** or **private**?\n\nHaving a public game means your game and code will show up for anyone in the server who runs the gamelist command.\n\n*Reply to this message with your answer.*"
        ).queue();

        CreatorManager.addCreator(new CreatorInstance(user, category, channel));
    }
}

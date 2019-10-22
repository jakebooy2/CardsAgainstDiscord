package me.tfkjake.cad.command;

import me.tfkjake.cad.CardsAgainstDiscord;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {

    public boolean handle(Guild server, Message message, User user, String sMessage) {

        String prefix = "cad!";
        if (!sMessage.startsWith(prefix))
            return false;

        if (message.getAuthor().isBot())
            return false;

        sMessage = sMessage.substring(prefix.length());

        String[] args = sMessage.split(" ");

        String command = args[0];

        String baseCommand = args[0];

        AbstractCommand cmd = null;

        for (Command c : CardsAgainstDiscord.getCommandManager().getCommands()) {
            if (c.getExecutor().getName().equalsIgnoreCase(command))
                cmd = c.getExecutor();
            if (c.getAliases().contains(command.toLowerCase())) {
                baseCommand = c.getName();
                cmd = c.getExecutor();
            }
        }

        Command c = CardsAgainstDiscord.getCommandManager().getCommand(baseCommand);

        if(cmd == null && c == null)
            return false;

        String[] nArgs = new String[args.length - 1];
        for(int i = 1; i < args.length; i++){
            nArgs[i-1] = args[i];
        }

        try {
            cmd.execute(server, message, user, nArgs);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

}

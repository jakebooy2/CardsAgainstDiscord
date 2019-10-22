package me.tfkjake.cad.creator;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CreatorManager {

    private static List<CreatorInstance> creatorInstances = new ArrayList<>();

    public static void addCreator(CreatorInstance stage){
        if(!creatorInstances.contains(stage))
            creatorInstances.add(stage);
    }

    public static void removeCreator(CreatorInstance stage){
        if(creatorInstances.contains(stage))
            creatorInstances.remove(stage);
    }

    public static CreatorInstance getCreationStage(User user){
        for(CreatorInstance cs : creatorInstances){
            if(cs.getCreator() == user)
                return cs;
        }
        return null;
    }

    public static CreatorInstance getCreationStage(TextChannel channel){
        for(CreatorInstance cs : creatorInstances){
            if(cs.getSetup() == channel)
                return cs;
        }
        return null;
    }


    public static CreatorInstance getCreationStage(String channelId){
        for(CreatorInstance cs : creatorInstances){
            if(cs.getSetup().getId().equalsIgnoreCase(channelId))
                return cs;
        }
        return null;
    }




}

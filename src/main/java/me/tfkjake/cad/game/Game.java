package me.tfkjake.cad.game;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private User creator;

    private Category category;
    private TextChannel game;

    private HashMap<String, TextChannel> playerGames = new HashMap<>();
    private HashMap<String, Message> cardMessage = new HashMap<>();
    private List<User> players;
    private User cardCzar;
    private int czarIndex = -1;
    private User winner;

    private HashMap<String, List<Card>> playerCards = new HashMap<>();
    private Card blackCard;

    private int maxScore;
    private boolean inGame;

    public Game(Category category, User creator, int maxScore, List<User> players){

        this.category = category;
        this.creator = creator;
        this.maxScore = maxScore;
        this.players = players;

        for(Channel channel : category.getChannels())
            channel.delete().reason("CAD Start").queue();

        TextChannel gameChannel = (TextChannel)category.createTextChannel("game").complete();
        gameChannel.createPermissionOverride(category.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();

        blackCard = new Card(true);

        for(User p : players){
            // Generate Channel
            TextChannel channel = (TextChannel)category.createTextChannel(p.getName().toLowerCase().replace(" ", "-")).complete();
            channel.createPermissionOverride(category.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
            channel.createPermissionOverride(category.getGuild().getMember(p)).setAllow(Permission.VIEW_CHANNEL).queue();
            gameChannel.createPermissionOverride(category.getGuild().getMember(p)).setAllow(Permission.VIEW_CHANNEL).queue();
            gameChannel.createPermissionOverride(category.getGuild().getMember(p)).setDeny(Permission.MESSAGE_WRITE).queue();
            playerGames.put(p.getId(), channel);

            // Generate Cards
            List<Card> cards = new ArrayList<>();
            for(int i = 0; i < 8; i++){
                cards.add(new Card(false));
            }
            playerCards.put(p.getId(), cards);

            Message message = channel.sendFile(generateDeck(p.getId())).complete();
            cardMessage.put(p.getId(), message);
        }

        nextCardCzar();

        inGame = true;
    }

    public File generateDeck(String userId){

        BufferedImage image = new BufferedImage(2007, 305, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(blackCard.generate(), 0, 0, null);

        List<Card> playerCard = playerCards.get(userId);
        for(int i = 0; i < playerCard.size(); i++){
            BufferedImage card = playerCard.get(i).generate();
                g2d.drawImage(card, card.getWidth() + (i*card.getWidth()) + ((i+1)*15), 0, null);
        }
        File file;
        try {
            file = new File("card/output/" + playerGames.get(userId).getId() + ".png");
            ImageIO.write(image, "png", file);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return file;

    }

    public void nextCardCzar(){
        czarIndex++;
        cardCzar = players.get(czarIndex);
    }

    public TextChannel getChannel(){
        return this.channel;
    }




}

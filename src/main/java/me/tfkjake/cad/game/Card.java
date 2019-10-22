package me.tfkjake.cad.game;

import me.tfkjake.cad.CardsAgainstDiscord;
import me.tfkjake.cad.misc.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Card {

    private String cardText;
    private boolean isBlack;

    public Card(boolean isBlack){
        this.isBlack = isBlack;
        List<HashMap<String, Object>> result = CardsAgainstDiscord.getMySQL().find("SELECT * FROM cards WHERE type = ? ORDER BY RAND() LIMIT 1", (isBlack ? "black" : "white"));
        this.cardText = (String)result.get(0).get("value");
    }

    public BufferedImage generate(){
        try {
            BufferedImage card;
            if (isBlack)
                card = ImageIO.read(new File("card/black.png"));
            else
                card = ImageIO.read(new File("card/white.png"));

            Graphics2D g2d = card.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("card/BebasNeue Bold.ttf"));
            font = font.deriveFont(Font.BOLD, 27f);
            g2d.setFont(font);

            if(isBlack)
                g2d.setColor(new Color(238, 238, 238));
            else
                g2d.setColor(new Color(35, 39, 42));

            FontMetrics fm = g2d.getFontMetrics();
            List<String> wrap = StringUtils.wrap(cardText, fm, (card.getWidth() - 40));
            int y = 5;
            for(String s : wrap)
                g2d.drawString(s, 10, y+=fm.getHeight());
            return card;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

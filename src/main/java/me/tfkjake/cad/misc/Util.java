package me.tfkjake.cad.misc;

import me.tfkjake.cad.CardsAgainstDiscord;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Util {

    public static BufferedImage generateCard(boolean black) {
        try {
            BufferedImage card;
            if (black)
                card = ImageIO.read(new File("card/black.png"));
            else
                card = ImageIO.read(new File("card/white.png"));

            Graphics2D g2d = card.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("card/BebasNeue Bold.ttf"));
            font = font.deriveFont(Font.BOLD, 27f);
            g2d.setFont(font);

            if(black)
                g2d.setColor(new Color(238, 238, 238));
            else
                g2d.setColor(new Color(35, 39, 42));

            List<HashMap<String, Object>> result = CardsAgainstDiscord.getMySQL().find("SELECT * FROM cards WHERE type = ? ORDER BY RAND() LIMIT 1", (black ? "black" : "white"));

            FontMetrics fm = g2d.getFontMetrics();
            List<String> wrap = StringUtils.wrap((String)result.get(0).get("value"), fm, (card.getWidth() - 40));
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

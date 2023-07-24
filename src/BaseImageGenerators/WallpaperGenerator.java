package BaseImageGenerators;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class WallpaperGenerator extends JPanel
{
    public BufferedImage GenerateBaseImage(int width, int height, String filepath)
    {
        BufferedImage createdImage = new BufferedImage(width, height, TYPE_INT_ARGB);
        BufferedImage pattern;
        Graphics2D graphics2d = createdImage.createGraphics();

        pattern = null;
        try
        {
            pattern = ImageIO.read(new File(filepath));
        } catch (IOException e) {
        }


        for (int y = 0; y < height; y+=pattern.getHeight())
        {
            for (int x = 0; x < width; x+=pattern.getWidth())
            {
                graphics2d.drawImage(pattern, x, y, this);
            }
        }
        return createdImage;
    }
}
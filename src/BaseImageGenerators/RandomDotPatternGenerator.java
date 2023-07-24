package BaseImageGenerators;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomDotPatternGenerator extends JPanel
{
    public BufferedImage Generate(int width, int height, Color colorStart, Color colorEnd, int patternWidth)
    {

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage slice = GenerateSlice(patternWidth, height, colorStart, colorEnd);
        Graphics2D graphics2d = img.createGraphics();
        for (int x = 0; x < width; x+=patternWidth)
        {

            graphics2d.drawImage(slice, x, 0, this);
        }
        return img;
    }
    private BufferedImage GenerateSlice(int width, int height, Color colorStart, Color colorEnd) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int dotsize = 1;
        float interpolatateValue = 1;
        for (int row = 0; row < height; row+=dotsize)
        {
            float invertedInterpolateValue = 1 - interpolatateValue;

            int newColorR = (int) Math.floor((colorStart.getRed() * interpolatateValue) + (colorEnd.getRed() * invertedInterpolateValue));
            int newColorG = (int) Math.floor((colorStart.getGreen() * interpolatateValue) + (colorEnd.getGreen() * invertedInterpolateValue));
            int newColorB = (int) Math.floor((colorStart.getBlue() * interpolatateValue) + (colorEnd.getBlue() * invertedInterpolateValue));
            Color rowColor = new Color(newColorR, newColorG, newColorB);

            for (int column = 0; column < width; column+=dotsize)
            {
                int color = 0;
                if (new Random().nextBoolean())
                {
                    color = rowColor.getRGB();
                } else if(new Random().nextBoolean())
                {
                    //This is to make the repetitions  more distinctive as the distribution of black and white changes
                    if ((column+new Random().nextInt(width/2))% width >= width/4)
                        color = new Color(255,255,255).getRGB();
                }
                for(int i = 0; i < dotsize; i++)
                {
                    for (int j = 0; j < dotsize; j++)
                    {
                        img.setRGB(column + i, row + j, color);
                    }
                }
            }
            interpolatateValue = 1 - (float) row / height;
        }
        return img;
    }
}

package StereogramGeneration;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class GenerateAutostereogram extends JPanel {

    public Image createImage(BufferedImage shiftmap, BufferedImage baseImage, boolean reverseshift)
    {
        var width = shiftmap.getWidth();
        var height = shiftmap.getHeight();
        BufferedImage baseMap = baseImage;
        BufferedImage createdImage = new BufferedImage(shiftmap.getWidth(), shiftmap.getHeight(), TYPE_INT_ARGB);
        Graphics2D resultGraphics = createdImage.createGraphics();


        int shiftModifier = reverseshift ? 1 : -1;
        int[] shades = new int[8];
        var shadeVal = 0;
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                int currentPixShade = new Color(shiftmap.getRGB(col, row)).getBlue();
                if (!Arrays.stream(shades).anyMatch(x -> x == currentPixShade))
                {
                    shades[shadeVal] = currentPixShade;
                    shadeVal++;
                }
            }
        }

        Map<Integer, Integer> shiftAmountMap = new HashMap<Integer, Integer>();
        Arrays.sort(shades);
        for (int i = 0; i < shades.length - 1; i++)
        {
            shiftAmountMap.put(shades[i], 5 - i);
        }
        shiftAmountMap.put(shades[shades.length - 1], -8);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int randomPix = new Random().nextInt(width-1);
                createdImage.setRGB(x, y, baseMap.getRGB(randomPix, y));

            }
        }


        for (int i = 1; i < shades.length; i++)
        {
            BufferedImage plate = new BufferedImage(shiftmap.getWidth(), shiftmap.getHeight(), TYPE_INT_ARGB);
            Integer currentPlateShade = shades[i];
            int pixShift = shiftAmountMap.get(currentPlateShade) * shiftModifier;
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (new Color(shiftmap.getRGB(x, y)).getBlue() == currentPlateShade)
                    {
                        try
                        {
                            plate.setRGB(x, y, baseMap.getRGB(x, y));
                        } catch (ArrayIndexOutOfBoundsException e)
                        {
                            System.out.println(x + " " + y);
                        }
                    }
                }
            }
            resultGraphics.drawImage(plate, 0 + pixShift, 0, this);
        }
        return createdImage;
    }
}
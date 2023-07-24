package DepthmapProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DepthMapProcessor
{

    public BufferedImage getAndProcessDepthmap(String filename, boolean invertMap)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {}


        int width = img.getWidth();
        int height = img.getHeight();

        var highestvalue = 0;
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                var newShade = new Color(img.getRGB(col, row)).getBlue();
                if (newShade>highestvalue)
                {
                    highestvalue = newShade;
                }
            }
        }

        float mappingMultiplier = (float)255/highestvalue;

        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                var remappedVal =  (int) Math.floor(new Color(img.getRGB(col, row)).getBlue() * mappingMultiplier);
                var newShade = new Color(remappedVal, remappedVal, remappedVal);
                img.setRGB(col, row, newShade.getRGB());
            }
        }

        int bitwiseAndVal = Integer.parseInt("11100000", 2);

        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                //might be a way to clean up this assignment weirdness
                int mappedVal = new Color(img.getRGB(col, row)).getBlue();
                mappedVal &= bitwiseAndVal;
                var newShade = new Color(mappedVal, mappedVal, mappedVal);
                img.setRGB(col, row, newShade.getRGB());
            }
        }
        if (invertMap) img = invert(img);
        return img;
    }

    private BufferedImage invert(BufferedImage depthmap)
    {
        BufferedImage basemap = depthmap;
        var height = basemap.getHeight();
        var width = basemap.getWidth();
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                int shade = new Color(depthmap.getRGB(col, row)).getBlue();
                int inverted = new Color(255-shade,255-shade,255-shade).getRGB();
                basemap.setRGB(col, row, inverted);
            }
        }
        return basemap;
    }
}


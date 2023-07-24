package UI;

import DepthmapProcessing.DepthMapProcessor;
import StereogramGeneration.GenerateAutostereogram;
import BaseImageGenerators.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.tools.FileObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class StereogramMaker extends javax.swing.JFrame implements ActionListener, ChangeListener
{
    @Serial
    private static final long serialVersionUID = 1L;
    private JButton createButton;

    private JCheckBox invertDepthMapBox;

    private JCheckBox useUserRepeatingPattern;

    private JCheckBox createMotionParallax;

    private JButton uploadOwnPattern;
    private JLabel patternFileNameLabel;
    private File userPatternFile;

    private File depthmapFile;
    private  JButton uploadDepthMap;

    private JLabel depthMapFileNameLabel;

    private JButton openDepthmapCreator;


    JSlider[] color1Sliders =
            {
                    new JSlider(),
                    new JSlider(),
                    new JSlider()
            };

    JLabel[] color1ValueLabels =
            {
                    new JLabel(),
                    new JLabel(),
                    new JLabel()
            };

    JPanel color1Color;
    Color color1;

    JSlider[] color2Sliders =
            {
                    new JSlider(),
                    new JSlider(),
                    new JSlider()
            };

    JLabel[] color2ValueLabels =
            {
                    new JLabel(),
                    new JLabel(),
                    new JLabel()
            };

    JPanel color2Color;
    Color color2;

    String[] colorChannels =
            {
                    "red",
                    "green",
                    "blue"
            };

    public StereogramMaker()
    {
        super("Demo");
        int rows = 10;
        int columns = 5;
        GridLayout grid = new GridLayout(rows, columns);
        JPanel[][] panelHolder = new JPanel[rows][columns];
        setLayout(grid);
        setSize(1200, 800);

        for(int m = 0; m < rows; m++)
        {
            for(int n = 0; n < columns; n++)
            {
                panelHolder[m][n] = new JPanel();
                add(panelHolder[m][n]);
            }
        }


        createButton = new JButton();
        createButton.setText("Generate");
        createButton.setEnabled(canGenerate());

        invertDepthMapBox = new JCheckBox();
        invertDepthMapBox.setText("Invert depthmap");

        useUserRepeatingPattern = new JCheckBox();
        useUserRepeatingPattern.setText("use own pattern");

        createMotionParallax = new JCheckBox();
        createMotionParallax.setText("create motion parallax");

        uploadOwnPattern = new JButton();
        uploadOwnPattern.setText("Upload own pattern");
        uploadOwnPattern.setEnabled(invertDepthMapBox.isSelected());
        patternFileNameLabel = new JLabel();
        patternFileNameLabel.setText("");

        uploadDepthMap = new JButton();
        uploadDepthMap.setText("Upload depthmap");
        depthMapFileNameLabel = new JLabel();
        depthMapFileNameLabel.setText("");

        openDepthmapCreator = new JButton();
        openDepthmapCreator.setText("create a depthmap");

        panelHolder[0][0].add(new JLabel("Color 1"));
        for (int i = 0; i <3; i++)
        {
            color1Sliders[i].setMinimum(0);
            color1Sliders[i].setMaximum(255);
            color1Sliders[i].setValue(255);
            panelHolder[1+i][0].add(new JLabel(colorChannels[i]));
            panelHolder[1+i][1].add(color1Sliders[i]);

            color1ValueLabels[i].setText(String.valueOf(color1Sliders[i].getValue()));
            panelHolder[1+i][2].add(color1ValueLabels[i]);

            color1Sliders[i].addChangeListener((ChangeListener) this);
        }

        color1Color = new JPanel();
        color1 = new Color(color1Sliders[0].getValue(),color1Sliders[1].getValue(),color1Sliders[2].getValue());
        color1Color.setBackground(color1);
        panelHolder[0][1].add(color1Color);

        panelHolder[4][0].add(new JLabel("Color 2"));
        for (int i = 0; i <3; i++)
        {
            color2Sliders[i].setMinimum(0);
            color2Sliders[i].setMaximum(255);
            color2Sliders[i].setValue(255);
            panelHolder[5+i][0].add(new JLabel(colorChannels[i]));
            panelHolder[5+i][1].add(color2Sliders[i]);

            color2ValueLabels[i].setText(String.valueOf(color2Sliders[i].getValue()));
            panelHolder[5+i][2].add(color2ValueLabels[i]);

            color2Sliders[i].addChangeListener((ChangeListener) this);
        }


        color2Color = new JPanel();
        color2 = new Color(color2Sliders[0].getValue(),color2Sliders[1].getValue(),color2Sliders[2].getValue());
        color2Color.setBackground(color2);
        panelHolder[4][1].add(color2Color);

        panelHolder[1][3].add(openDepthmapCreator);
        panelHolder[2][3].add(useUserRepeatingPattern);
        panelHolder[2][4].add(uploadOwnPattern);
        panelHolder[3][4].add(patternFileNameLabel);
        panelHolder[4][4].add(invertDepthMapBox);
        panelHolder[4][3].add(uploadDepthMap);
        panelHolder[5][3].add(depthMapFileNameLabel);
        panelHolder[6][3].add(createButton);
        panelHolder[6][4].add(createMotionParallax);

        uploadDepthMap.addActionListener(this);
        createButton.addActionListener(this);
        createMotionParallax.addActionListener(this);
        useUserRepeatingPattern.addActionListener(this);
        uploadOwnPattern.addActionListener(this);
        openDepthmapCreator.addActionListener(this);
        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        String event = e.getActionCommand();
        if (event.equals("Generate"))
        {
            generate();
        }
        else if (event.equals("use own pattern"))
        {
            uploadOwnPattern.setEnabled(useUserRepeatingPattern.isSelected());
            for (int i = 0;i <3; i++)
            {
                color1Sliders[i].setEnabled(!useUserRepeatingPattern.isSelected());
                color2Sliders[i].setEnabled(!useUserRepeatingPattern.isSelected());
            }
        }
        else if (event.equals("Upload depthmap"))
        {
            openDepthmapFile();
        }
        else if (event.equals(("Upload own pattern")))
        {
            openPatternFile();
        }
        else if (event.equals("create a depthmap"))
        {
            openDepthmapProgram();
        }
        createButton.setEnabled(canGenerate());
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        for (int i = 0;i <3; i++)
        {
            color1ValueLabels[i].setText(String.valueOf(color1Sliders[i].getValue()));
            color1 = new Color(color1Sliders[0].getValue(),color1Sliders[1].getValue(),color1Sliders[2].getValue());
            color1Color.setBackground(color1);
            color2ValueLabels[i].setText(String.valueOf(color2Sliders[i].getValue()));
            Color color2 = new Color(color2Sliders[0].getValue(),color2Sliders[1].getValue(),color2Sliders[2].getValue());
            color2Color.setBackground(color2);
        }
    }


    private boolean canGenerate()
    {
        if (depthmapFile != null)
        {
            if (useUserRepeatingPattern.isSelected())
            {
                if (userPatternFile != null)
                {
                    return true;
                }
                return false;
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    private void openDepthmapFile()
    {

        final JFileChooser fileSelectionWindow = new JFileChooser();
        fileSelectionWindow.setFileFilter(new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes()));
        fileSelectionWindow.showOpenDialog(this);
        if (fileSelectionWindow.getSelectedFile() != null)
        {
            depthmapFile = fileSelectionWindow.getSelectedFile();
            depthMapFileNameLabel.setText(depthmapFile.getName());
        }
    }

    private void openPatternFile()
    {
        final JFileChooser fileSelectionWindow = new JFileChooser();
        fileSelectionWindow.setFileFilter(new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes()));
        fileSelectionWindow.showOpenDialog(this);
        if (fileSelectionWindow.getSelectedFile() != null)
        {
            userPatternFile = fileSelectionWindow.getSelectedFile();
            patternFileNameLabel.setText(userPatternFile.getName());
        }
    }
    private void openDepthmapProgram()
    {
        String path = "/depthmapGenerator/DepthmapMaker.exe";
        File file = new File(System.getProperty("user.dir") + path);
        if (! file.exists()) {
            throw new IllegalArgumentException("The file " + path + " does not exist");
        }
        try
        {
            Process p = Runtime.getRuntime().exec(file.getAbsolutePath());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void generate() {

        GenerateAutostereogram generator = new GenerateAutostereogram();
        BufferedImage shiftmap;
        var maploader = new DepthMapProcessor();
        shiftmap = maploader.getAndProcessDepthmap(depthmapFile.getAbsolutePath(), invertDepthMapBox.isSelected());

        int width = shiftmap.getWidth();
        int height = shiftmap.getHeight();

        BufferedImage baseImage;
        if (useUserRepeatingPattern.isSelected())
        {
            baseImage = new WallpaperGenerator().GenerateBaseImage(width, height, userPatternFile.getAbsolutePath());
        } else
        {
            var color1 = new Color(color1Sliders[0].getValue(), color1Sliders[1].getValue(), color1Sliders[2].getValue());
            var color2 = new Color(color2Sliders[0].getValue(), color2Sliders[1].getValue(), color2Sliders[2].getValue());

            //TODO: allow user to enter pattern width
            baseImage = new RandomDotPatternGenerator().Generate(width, height, color1, color2, 128);
        }

        Image result = generator.createImage(shiftmap, baseImage, false);
        Thread resultThread;
        if (createMotionParallax.isSelected())
        {
            Image result2 = generator.createImage(shiftmap, baseImage, true);
            resultThread = new Thread(() -> displayParallaxResult(result, result2, width, height));
        }
        else
        {
            resultThread = new Thread(() -> displayResult(result, width, height));
        }
        resultThread.start();
    }
    private void displayResult(Image result, int width, int height)
    {
        JFrame frame = new JFrame("generated");

        ImageIcon icon = new ImageIcon(result);
        frame.setLayout(new FlowLayout());
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setSize(width, height);
        frame.setVisible(true);
    }
    private void displayParallaxResult(Image resultOne, Image resultTwo, int width, int height) {
        JFrame frame = new JFrame("generated");
        ImageIcon icon = new ImageIcon(resultOne);
        frame.setLayout(new FlowLayout());
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setSize(width, height);
        frame.setVisible(true);
        while (true) {
            try {
                icon.setImage(resultOne);
                lbl.setIcon(icon);
                frame.add(lbl);
                frame.repaint();
                Thread.sleep(200);
                icon.setImage(resultTwo);
                lbl.setIcon(icon);
                frame.add(lbl);
                frame.repaint();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

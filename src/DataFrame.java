import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class DataFrame  extends JFrame
{
    private JPanel mainPanel, inputPanel, displayPanel, buttonPanel;
    private JLabel titleLabel;
    private JTextField inputTextField;
    private JTextArea originalTextArea, filteredTextArea;
    private JScrollPane originalScroll, filteredScroll;
    private JButton loadBtn, searchBtn, quitBtn;
    private JFileChooser chooser;
    private String displayFile;
    private Path originalFilePath;
    private ActionListener quit = new quitListener();
    private ActionListener load = new loadListener();
    private ActionListener search = new searchListener();

    DataFrame()
    {
        setTitle("Data Streams");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        inputPanel = new JPanel();
        displayPanel = new JPanel();
        buttonPanel = new JPanel();
        titleLabel = new JLabel("Data Streams");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
        inputTextField = new JTextField();
        inputTextField.setFont(new Font("Times New Roman", Font.BOLD, 20));

        TitledBorder inputBorder = BorderFactory.createTitledBorder("Input search terms");
        inputBorder.setTitleFont(new Font("Times New Roman", Font.BOLD, 15));
        inputTextField.setBorder(inputBorder);

        originalTextArea = new JTextArea(5,5);
        TitledBorder originalBorder = BorderFactory.createTitledBorder("Original Text");
        originalBorder.setTitleFont(new Font("Times New Roman", Font.BOLD, 20));
        originalTextArea.setBorder(originalBorder);
        originalTextArea.setFont(new Font("Times New Roman", Font.BOLD, 15));
        originalScroll = new JScrollPane(originalTextArea);
        originalTextArea.setEditable(false);

        filteredTextArea = new JTextArea(5,5);
        TitledBorder filteredBorder = BorderFactory.createTitledBorder("Filtered Text");
        filteredBorder.setTitleFont(new Font("Times New Roman", Font.BOLD, 20));
        filteredTextArea.setBorder(filteredBorder);
        filteredTextArea.setFont(new Font("Times New Roman", Font.BOLD, 15));
        filteredScroll = new JScrollPane(filteredTextArea);
        filteredTextArea.setEditable(false);

        loadBtn = new JButton("Load File");
        loadBtn.setFont(new Font("Bold", Font.BOLD, 25));
        loadBtn.addActionListener(load);

        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Bold", Font.BOLD, 25));
        searchBtn.addActionListener(search);

        quitBtn = new JButton("Quit");
        quitBtn.setFont(new Font("Bold", Font.BOLD, 25));
        quitBtn.addActionListener(quit);

        chooser = new JFileChooser();

        add(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new GridLayout(2,1));
        inputPanel.add(titleLabel);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(inputTextField);

        mainPanel.add(displayPanel, BorderLayout.CENTER);
        displayPanel.setLayout(new GridLayout(1,2));
        displayPanel.add(originalScroll);
        displayPanel.add(filteredScroll);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(loadBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(quitBtn);
    }
    private class loadListener implements ActionListener
    {
        public void actionPerformed(ActionEvent AE)
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();
                originalFilePath = selectedFile.toPath();
                try (Stream lines = Files.lines(Paths.get(originalFilePath.toString())))
                {
                    displayFile = (String) lines.collect(Collectors.joining("\n"));
                    originalTextArea.setText(displayFile);
                }
                catch (FileNotFoundException e)
                {
                    JOptionPane.showMessageDialog(mainPanel, "File Not Found!");
                    e.printStackTrace();
                }
                catch (NullPointerException e)
                {
                    JOptionPane.showMessageDialog(mainPanel, "File Not Found!");
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private class searchListener implements ActionListener
    {
        public void actionPerformed(ActionEvent AE)
        {
            String filter = inputTextField.getText();
            try (Stream lines = Files.lines(Paths.get(originalFilePath.toString()))) {
                displayFile = (String) lines
                        .filter(w -> w.toString().contains(filter))
                        .collect(Collectors.joining("\n"));
                filteredTextArea.setText(displayFile);
            }
            catch (FileNotFoundException e)
            {
                JOptionPane.showMessageDialog(mainPanel, "File Not Found!");
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                JOptionPane.showMessageDialog(mainPanel, "File Not Found!");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private class quitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent AE)
        {
            System.exit(0);
        }
    }
}
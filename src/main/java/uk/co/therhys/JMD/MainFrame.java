package uk.co.therhys.JMD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class MainFrame extends JFrame {
    private final JLabel formattedOutput;
    private final JTextArea mdEntry;

    private ActionListener saveListener;
    private ActionListener openListener;
    private ActionListener exportListener;

    private void initActions(){
        final MainFrame parent = this;

        saveListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                int option = fileChooser.showSaveDialog(parent);

                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();

                    FileUtils.writeFile(file, mdEntry.getText());
                }
            }
        };

        exportListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                int option = fileChooser.showSaveDialog(parent);

                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();

                    FileUtils.writeFile(file, new Markdown(mdEntry.getText()).toHTML());
                }
            }
        };

        openListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                int option = fileChooser.showSaveDialog(parent);

                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();

                    mdEntry.setText(FileUtils.readFile(file));
                }
            }
        };
    }

    private static JButton newToolButton(String title, String img, ActionListener listener){
        JButton btn = new JButton(title);

        btn.setIcon(new ImageIcon(img));

        btn.addActionListener(listener);

        return btn;
    }

    private void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");

        fileMenu.add("Save").addActionListener(saveListener);

        fileMenu.add("Open").addActionListener(openListener);
        fileMenu.add("Export").addActionListener(exportListener);

        menuBar.add(fileMenu);
    }

    private void initToolbar(JToolBar toolbar){
        getContentPane().add(toolbar, BorderLayout.NORTH);

        toolbar.add(newToolButton("Save", "src/main/resources/save.png", saveListener));
        toolbar.add(newToolButton("Open", "src/main/resources/open.png", openListener));
        toolbar.add(newToolButton("Export", "src/main/resources/open.png", exportListener));
    }

    private void initMarkdown(){
        JSplitPane mainContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        mainContent.setDividerLocation(500);

        mainContent.add(mdEntry);
        mainContent.add(formattedOutput);

        mdEntry.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {

            }

            public void keyPressed(KeyEvent keyEvent) {

            }

            public void keyReleased(KeyEvent keyEvent) {
                Markdown md = new Markdown(mdEntry.getText());

                formattedOutput.setText(md.toHTML());
            }
        });

        getContentPane().add(mainContent, BorderLayout.CENTER);
    }

    MainFrame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout(0, 1);
        getContentPane().setLayout(layout);

        initActions();

        formattedOutput = new JLabel();
        mdEntry = new JTextArea();

        JToolBar toolbar = new JToolBar();
        initToolbar(toolbar);
        initMenuBar();

        initMarkdown();

        setSize(800, 600);
    }
}

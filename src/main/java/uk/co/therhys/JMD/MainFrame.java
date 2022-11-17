package uk.co.therhys.JMD;

import cz.adamh.utils.NativeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class MainFrame extends JFrame {
    private final JLabel formattedOutput;
    private final JTextArea mdEntry;

    private ActionListener saveListener;
    private ActionListener openListener;
    private ActionListener exportListener;

    private File currentFile;

    private void saveToFile(File file){
        FileUtils.writeFile(file, mdEntry.getText());
    }

    private void initActions() {
        final MainFrame parent = this;

        saveListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(currentFile != null) {
                    saveToFile(currentFile);
                } else {
                    JFileChooser fileChooser = new JFileChooser();

                    int option = fileChooser.showSaveDialog(parent);

                    if (option == JFileChooser.APPROVE_OPTION) {
                        currentFile = fileChooser.getSelectedFile();
                        saveToFile(currentFile);
                    }
                }
            }
        };

        exportListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                int option = fileChooser.showSaveDialog(parent);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    FileUtils.writeFile(file, new Markdown(mdEntry.getText()).toHTML());
                }
            }
        };

        openListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                int option = fileChooser.showSaveDialog(parent);

                if (option == JFileChooser.APPROVE_OPTION) {
                    currentFile = fileChooser.getSelectedFile();

                    mdEntry.setText(FileUtils.readFile(currentFile));
                }
            }
        };

        getRootPane().registerKeyboardAction(saveListener, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK, false), JComponent.WHEN_FOCUSED);
    }

    private static ImageIcon getIcon(String img) {
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource(img);
            if(url != null) {
                return new ImageIcon(url);
            }

            return null;
        } catch (Exception e) {
            return new ImageIcon("src/main/resources/" + img);
        }
    }

    private static JButton newToolButton(String title, String img, ActionListener listener) {
        JButton btn = new JButton(title);

        btn.setIcon(getIcon(img));

        btn.addActionListener(listener);

        return btn;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenu = fileMenu.add("Save");
        saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveMenu.addActionListener(saveListener);

        JMenuItem openMenu = fileMenu.add("Open");
        openMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openMenu.addActionListener(openListener);

        JMenuItem exportMenu = fileMenu.add("Export");
        exportMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        exportMenu.addActionListener(exportListener);

        menuBar.add(fileMenu);
    }

    private void initToolbar(JToolBar toolbar) {
        getContentPane().add(toolbar, BorderLayout.NORTH);

        toolbar.add(newToolButton("Save", "save.png", saveListener));
        toolbar.add(newToolButton("Open", "open.png", openListener));
        toolbar.add(newToolButton("Export", "export.png", exportListener));
    }

    private void initMarkdown() {
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

    MainFrame() {
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

package uk.co.therhys.JMD;

import apple.dts.samplecode.osxadapter.OSXAdapter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.undo.UndoManager;

public class MainFrame extends JFrame {
    private final JLabel formattedOutput;
    private final JTextArea mdEntry;
    private final UndoManager undoManager;

    private ActionListener saveListener;
    private ActionListener openListener;
    private ActionListener exportListener;
    private ActionListener undoListener;
    private ActionListener redoListener;

    private ActionListener quitListener;

    private File currentFile;

    private final boolean isOsx = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));

    private final int commandKey;

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

        undoListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                undoManager.undo();
            }
        };

        redoListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                undoManager.redo();
            }
        };

        quitListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                parent.dispose();
            }
        };
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
        JButton btn = OsxUiFactory.getInstance().getUnifiedToolBtn(title);

        btn.setIcon(getIcon(img));

        btn.addActionListener(listener);

        return btn;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenu = fileMenu.add("Save");
        saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, commandKey));
        saveMenu.addActionListener(saveListener);

        JMenuItem openMenu = fileMenu.add("Open");
        openMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, commandKey));
        openMenu.addActionListener(openListener);

        JMenuItem exportMenu = fileMenu.add("Export");
        exportMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, commandKey));
        exportMenu.addActionListener(exportListener);

        JMenuItem undoMenu = fileMenu.add("Undo");
        undoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, commandKey));
        undoMenu.addActionListener(undoListener);

        JMenuItem redoMenu = fileMenu.add("Redo");
        redoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, commandKey));
        redoMenu.addActionListener(redoListener);

        if(isOsx) {
            OSXAdapter.setQuitHandler(this, quitListener);
        } else {
            JMenuItem exitMenu = fileMenu.add("Exit");
            exitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, commandKey));
            exitMenu.addActionListener(quitListener);
        }

        menuBar.add(fileMenu);
    }

    private void initToolbar(JToolBar toolbar) {
        getContentPane().add(toolbar, BorderLayout.NORTH);

        toolbar.setFloatable(false);

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
        setContentPane(OsxUiFactory.getInstance().getUnifiedToolbarPanel(new BorderLayout(0, 1)));

        if(isOsx){
            commandKey = InputEvent.META_MASK;
        }else{
            commandKey = InputEvent.CTRL_MASK;
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initActions();

        formattedOutput = new JLabel();
        mdEntry = new JTextArea();

        undoManager = new UndoManager();

        mdEntry.getDocument().addUndoableEditListener(undoManager);

        JToolBar toolbar = new JToolBar();
        initToolbar(toolbar);
        initMenuBar();

        initMarkdown();

        setSize(800, 600);
    }
}

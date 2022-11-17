package uk.co.therhys.JMD;

import apple.dts.samplecode.osxadapter.OSXAdapter;
import uk.co.therhys.MD.Converter;

import javax.swing.*;

public class JMD {
    private static boolean tryLookAndFeel(String className){
        try {
            UIManager.setLookAndFeel(className);

            return false;
        }catch(Exception e){
            e.printStackTrace();

            return true;
        }
    }

    public static void main(String[] args){
        if(! tryLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
            System.out.println("Using gtk theme");
        }else if(! tryLookAndFeel(UIManager.getSystemLookAndFeelClassName())){
            System.out.println("Using system theme");
        }else if(! tryLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())){
            System.out.println("Using cross platform theme");
        }else{
            System.out.println("Cannot get theme!");
        }
        
        try{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JMD");

            OSXAdapter.init();
		} catch(Exception e){
			e.printStackTrace();
		}

        new Converter();

        MainFrame frame = new MainFrame();

        frame.setVisible(true);
    }
}

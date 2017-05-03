package com.CmmCompiler.UI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JWindow;
/**
 *
 * @author Administrator
 */
public class Login extends JWindow {
    
	private int duration;
    Image ii;
    int iconWidth = 200;
    int iconHeight = 200;
    int liveTime, currTime;
    
    public Login(int duration) {
        liveTime = duration / 1000;
        ii = this.getToolkit().getImage("Image/login.gif");
        Dimension welcomeScreen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (welcomeScreen.width - iconWidth) / 2;
        int y = (welcomeScreen.height - iconHeight) / 2;
        currTime = liveTime;
        this.setBounds(x, y, iconHeight, iconHeight);
        this.setVisible(true);
        this.toFront();
    }
    public void showWelcomeWindow() {
        try {
            repaint();
            Thread.sleep(1000 * liveTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showWeclomeWindowAndExit(){
        this.showWelcomeWindow();
        this.dispose();
    }
    @Override
    public void update(Graphics g){
        paint(g);
    }
    @Override
    public void paint(Graphics g){
        g.drawImage(ii, 0, 0, this);
    }
}
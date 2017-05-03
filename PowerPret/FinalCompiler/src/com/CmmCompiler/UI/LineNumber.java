package com.CmmCompiler.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
/**
* 
* @author Administrator 给容器添加行号
*/
public class LineNumber extends JComponent {

/**
* 
*/
private static final long serialVersionUID = 1L;
private final static Color DEFAULT_BACKGROUND = new Color(230, 230, 230);
private final static Color DEFAULT_FOREGROUND = Color.black;
private final static Font DEFAULT_FONT = new Font("monospaced", Font.PLAIN,
12);
private final static int HEIGHT = Integer.MAX_VALUE - 1000000;
private final static int MARGIN = 1;
private int lineHeight;
private int fontLineHeight;
private int currentRowWidth;
private FontMetrics fontMetrics;

// 构造方法，设置容器的背景以及字体
public LineNumber(JComponent component) {
if (component == null) {
setBackground(DEFAULT_BACKGROUND);
setForeground(DEFAULT_FOREGROUND);
setFont(DEFAULT_FONT);
} else {
setBackground(DEFAULT_BACKGROUND);
setForeground(component.getForeground());
setFont(component.getFont());
}
setPreferredSize(99999);
}

public void setPreferredSize(int row) {
int width = fontMetrics.stringWidth(String.valueOf(row));
if (currentRowWidth < width) {
currentRowWidth = width;
setPreferredSize(new Dimension( 2*MARGIN + width, HEIGHT));
}
}

public void setFont(Font font) {
super.setFont(font);
fontMetrics = getFontMetrics(getFont());
fontLineHeight = fontMetrics.getHeight();
}

public int getLineHeight() {
if (lineHeight == 0)
return fontLineHeight;
else
return lineHeight;
}

public void setLineHeight(int lineHeight) {
if (lineHeight > 0)
this.lineHeight = lineHeight;
}

public int getStartOffset() {
return 1;
}

// 重绘容器
public void paintComponent(Graphics g) {
int lineHeight = getLineHeight();
int startOffset = getStartOffset();
Rectangle drawHere = g.getClipBounds();
g.setColor(getBackground());
g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
g.setColor(getForeground());
int startLineNumber = (drawHere.y / lineHeight) + 1;
int endLineNumber = startLineNumber + (drawHere.height / lineHeight);
int start = (drawHere.y / lineHeight) * lineHeight + lineHeight
- startOffset;
for (int i = startLineNumber; i <= endLineNumber; i++) {
String lineNumber = String.valueOf(i);
int width = fontMetrics.stringWidth(lineNumber);
g.drawString(lineNumber, MARGIN + currentRowWidth - width, start);
start += lineHeight;
}
setPreferredSize(endLineNumber);
}
}




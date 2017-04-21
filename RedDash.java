package ex4;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class RedDash implements Icon{
	
	private int width;
	private int height;
	
	public RedDash (int w, int h){
		if((w | h) < 0) {
            throw new IllegalArgumentException("Illegal dimensions: "
                    + "(" + w + ", " + h + ")");
        }
        this.width  = w;
        this.height = h;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color temp = g.getColor();
        g.setColor(Color.RED);
        g.fillRect(x+5, y-1, getIconWidth(),getIconHeight());
        g.setColor(temp);
	}

}

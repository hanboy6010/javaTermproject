package grimpan;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class BackgroundPanel extends JPanel {
	
	private final static Color StartColor = new Color( 220, 230, 240 );
	private final static Color EndColor = new Color( 200, 210, 230 );
	
	public BackgroundPanel()
	{
		
	}
	
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		GradientPaint gp = new GradientPaint( 0, 0, StartColor, getWidth(), getHeight(), 
				EndColor );
		Graphics2D g2d = ( Graphics2D ) g;
		g2d.setPaint( gp );
		g2d.fillRect( 0, 0, getWidth(), getHeight() );
	}

}

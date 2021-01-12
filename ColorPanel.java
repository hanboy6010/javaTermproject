package grimpan;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.*;

public class ColorPanel extends JPanel{

	public static Color Selected_Color = Color.black;
	private Color Color_Table[] = { Color.black, Color.white, Color.blue, Color.cyan,
			Color.green, Color.magenta, Color.orange, Color.pink, Color.red, Color.yellow};
	private JButton[] colorButton = new JButton[10];
	private JButton moreColorButton;
	private ButtonHandler handler = new ButtonHandler();
	
	public ColorPanel()
	{	
		for( int i = 0; i < 10; i++ )
		{
			colorButton[i] = new JButton();
			colorButton[i].setBackground( Color_Table[i] );
			colorButton[i].setLocation( 10 + 20*(i/2), 5 + 20*(i%2) );
			colorButton[i].setSize( 15, 15 );
			colorButton[i].addActionListener( handler );
			add( colorButton[i] );
		}
		
		moreColorButton = new JButton( "More Colors" );
		moreColorButton.setLocation( 120, 5 );
		moreColorButton.setSize( 120, 40 );
		moreColorButton.addActionListener( handler );
		add( moreColorButton );
			
	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			JButton selected_button = ( JButton )event.getSource();
			for( int i = 0; i < 10; i++ )
			{
				if( selected_button == colorButton[i] )
				{
					Selected_Color = Color_Table[i];
				}
			}
			
			if( selected_button == moreColorButton )
			{
				JFrame ColorDialog = new JFrame();
				Selected_Color = JColorChooser.showDialog( ColorDialog, "Select Color"
						, Color.black );
				if( Selected_Color == null )
					Selected_Color = Color.black;
			}
		}
	}
	
}

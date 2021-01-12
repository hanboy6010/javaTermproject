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


public class ToolPanel extends JPanel{
	
	public static int button_on = 0;
	public static int LineWidth;
	private String icon_name[] = { "pencil.png", "eraser.png", "paint.png", "select.png", 
			"spray.png", "brush.png", "straight.png", "text.png", "rect.png", "oval.png",
			"magnifier.png", "cliparea.png"};
	private String LineWidth_Name[] = { "1.0", "2.0", "3.0", "4.0" };
	private Icon[] icon = new ImageIcon[12];
	private JButton[] toolButton = new JButton[12];
	ButtonHandler handler = new ButtonHandler();
	private JList SelectLineWidth;
	
	public ToolPanel()
	{
		for( int i = 0; i < 12; i++ )
		{
			icon[i] = new ImageIcon( getClass().getResource( icon_name[i] ) );
			toolButton[i] = new JButton( icon[i] );
			toolButton[i].setLayout( null ); 
			toolButton[i].setBounds( 5 + 25*(i%2), 10 + 25*(i/2), 20, 20 );
			toolButton[i].addActionListener( handler );
			add( toolButton[i] );
		}
		
		SelectLineWidth = new JList( LineWidth_Name );
		SelectLineWidth.setLayout( null );
		SelectLineWidth.setBounds( 10, 200, 40, 80 );
		SelectLineWidth.setVisibleRowCount( 4 );
		SelectLineWidth.addListSelectionListener(
				new ListSelectionListener()
				{
					public void valueChanged( ListSelectionEvent event )
					{
						LineWidth = SelectLineWidth.getSelectedIndex() + 1;
					}
				});
		
		add( SelectLineWidth );
		
	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			MainFrame.r = 0;
			JButton selected_button = ( JButton )event.getSource();
			for( int i = 0; i < 12; i++ )
			{
				if( selected_button == toolButton[i])
				{
					button_on = i;
				}
			}
		}
	}
	
}
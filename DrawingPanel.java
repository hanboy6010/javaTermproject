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

import javax.swing.JPanel;

public class DrawingPanel extends JPanel
{
	private int[] Figure_Check = new int[10];
	public static int Figure_Number, k;
	public static BufferedImage bi = new BufferedImage( 1000, 500, BufferedImage.TYPE_INT_ARGB);
	public static Graphics2D gg = bi.createGraphics();
	private boolean fill_check = false;
	private int clip_check;
	public static Point clip_coordinate1, clip_coordinate2, old_clip_coordinate1, old_clip_coordinate2;
	public static BufferedImage clip_image;
	private AffineTransform tx = new AffineTransform();
	
	public DrawingPanel()
	{
		setBackground( Color.white );
		Figure_Number = 0;
		Figure_Check[0] = 0;
	}
	
	public void DrawRect( Graphics2D g, int fx, int fy, int lx, int ly, boolean check_fill )
	{
		int width = Math.abs( fx - lx );
		int height = Math.abs( fy - ly );
		int x = 0, y = 0;
		if( (fx <= lx) && (fy <= ly) ){ x = fx; y = fy;}
		else if( (fx >= lx) && (fy <= ly) ){ x = lx; y = fy;}
		else if( (fx <= lx) && (fy >= ly) ){ x = fx; y = ly;}
		else if( (fx >= lx) && (fy >= ly) ){ x = lx; y = ly;}
		if( check_fill == false ){g.drawRect( x, y, width, height );}
		else if( check_fill == true ){g.fillRect( x, y, width, height );}
	}
	
	public void DrawOval( Graphics2D g, int fx, int fy, int lx, int ly, boolean fill_check )
	{
		int width = Math.abs( fx - lx );
		int height = Math.abs( fy - ly );
		int x = 0, y = 0;
		if( (fx <= lx) && (fy <= ly) ){x = fx; y = fy;}
		else if( (fx >= lx) && (fy <= ly) ){x = lx; y = fy;}
		else if( (fx <= lx) && (fy >= ly) ){x = fx; y = ly;}
		else if( (fx >= lx) && (fy >= ly) ){x = lx; y =  ly;}
		if( fill_check == false ){ g.drawOval(x, y, width, height);}
		else if( fill_check == false ){ g.fillOval(x, y, width, height);}
	}
	
	public void RePaint( Graphics g )
	{
		for( int j = 0; j < Figure_Check[Figure_Number]; ++j )
		{
			PaintInformation pi = (PaintInformation) MainFrame.vc.elementAt(j);

			gg.setStroke(new BasicStroke( pi.getLineWidth() ) );
			gg.setColor( pi.getSelectColor() );
			
			if( pi.getSelectButton() == 0 )
			{
				gg.drawLine( pi.getOldpoint().x, pi.getOldpoint().y, pi.getNewpoint().x, pi.getNewpoint().y );
			}

			else if( pi.getSelectButton() == 1 )
			{
				gg.setColor( Color.white );
				for( double i = 0; i < 6.3; i+=0.2 )
				{
					int ox = ( int )( pi.getOldpoint().x + 2 * Math.cos(i) );
					int oy = ( int )( pi.getOldpoint().y + 2 * Math.sin(i) );
					int nx = ( int )( pi.getNewpoint().x + 2 * Math.cos(i) );
					int ny = ( int )( pi.getNewpoint().y + 2 * Math.sin(i) );
					gg.drawLine( ox, oy, nx, ny );
				}
			}

			else if( pi.getSelectButton() == 2 )
			{
				Graphics2D g2d = ( Graphics2D ) g;
				//g2d.setStroke(new BasicStroke( 2.0f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER, 5f, dashpattern, 10f ) );
				g2d.setColor( Color.blue );
				DrawRect( (Graphics2D)g, MainFrame.firstpoint.x,
						MainFrame.firstpoint.y, MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );

			}

			else if( pi.getSelectButton() == 4 )
			{
				for( int i = 0; i < 100; i++ )
				{
					int Rand = ( int )( Math.random() * 20 );
					double Theta = Math.random() * 2 * Math.PI;
					int x = ( int )( pi.getNewpoint().x + Rand * Math.cos(Theta) );
					int y = ( int )( pi.getNewpoint().y + Rand * Math.sin(Theta) );
					gg.fillOval( x, y, 1, 2 );
				}
			}

			else if( pi.getSelectButton() == 5 )
			{
				for( double i = -2 ; i < 2; i += 0.1 )
				{
					int ox = ( int )( pi.getOldpoint().x + (-2) * i );
					int oy = ( int )( pi.getOldpoint().y + 4 * i );
					int nx = ( int )( pi.getNewpoint().x + (-2) * i );
					int ny = ( int )( pi.getNewpoint().y + 4 * i );
					gg.drawLine( ox, oy, nx, ny );
				}
			}

			else if( pi.getSelectButton() == 6 )
			{
				gg.drawLine( pi.getFirstpoint().x, pi.getFirstpoint().y, pi.getNewpoint().x, pi.getNewpoint().y );
			}

			else if( pi.getSelectButton() == 7 )
			{

			}

			else if( pi.getSelectButton() == 8 )
			{
				if( pi.getMouseStatus() == 2 )
				{
					gg.setColor( pi.getSelectColor() );
					DrawRect( gg, pi.getFirstpoint().x, pi.getFirstpoint().y,
							pi.getNewpoint().x, pi.getNewpoint().y, fill_check );
				}
			}

			else if( pi.getSelectButton() == 9 )
			{
				if( MainFrame.Mouse_Status == 2 )
					DrawOval( gg, MainFrame.firstpoint.x, MainFrame.firstpoint.y,
							MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );
			}

			else if( pi.getSelectButton() == 10 )
			{

			}

			else if( pi.getSelectButton() == 11 )
			{
				if( clip_check == 1 )
				{
					//gg.drawImage( clip_image, 100, 100, this );
					if( (clip_coordinate1.x < MainFrame.firstpoint.x) && 
							(clip_coordinate2.x > MainFrame.firstpoint.x) &&
							(clip_coordinate1.y < MainFrame.firstpoint.y) &&
							(clip_coordinate2.y > MainFrame.firstpoint.y) )
					{
						if( MainFrame.Mouse_Status == 2 )
						{
							gg.drawImage( clip_image, clip_coordinate1.x + MainFrame.newpoint.x - MainFrame.firstpoint.x, 
									clip_coordinate1.y + MainFrame.newpoint.y - MainFrame.firstpoint.y, this );
						}

					}
					else
					{
						clip_check = 0;
					}
				}

				if( clip_check == 0 )
				{
					Graphics2D g2d = ( Graphics2D ) g;
					//g2d.setStroke(new BasicStroke( 2.0f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER, 5f, dashpattern, 10f ) );
					g2d.setColor( Color.blue );
					DrawRect( (Graphics2D)g, MainFrame.firstpoint.x,
							MainFrame.firstpoint.y, MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );
					if( MainFrame.Mouse_Status == 2 )
					{
						gg.setColor( Color.blue );
						//gg.setStroke(new BasicStroke( 2.0f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER, 5f, dashpattern, 10f ) );
						DrawRect( gg, MainFrame.firstpoint.x, MainFrame.firstpoint.y,
								MainFrame.newpoint.x, MainFrame.newpoint.y, false );
						if( MainFrame.firstpoint.x < MainFrame.newpoint.x )
						{
							clip_coordinate1 = MainFrame.firstpoint; 
							clip_coordinate2 = MainFrame.newpoint;
						}
						else
						{
							clip_coordinate1 = MainFrame.newpoint; 
							clip_coordinate2 = MainFrame.firstpoint;
						}
						clip_coordinate2.x += 1;
						clip_coordinate2.y += 1;
						clip_image = bi.getSubimage( clip_coordinate1.x, clip_coordinate1.y,
								clip_coordinate2.x - clip_coordinate1.x, clip_coordinate2.y - clip_coordinate1.y );
						clip_check = 1;
					}
				}
			}
		}
	}
	
	public void Paint( Graphics g )
	{
		gg.setStroke(new BasicStroke( ToolPanel.LineWidth ) );
		gg.setColor( ColorPanel.Selected_Color );
		
		if( ToolPanel.button_on == 0 )
		{
			gg.drawLine( MainFrame.oldpoint.x, MainFrame.oldpoint.y, MainFrame.newpoint.x, MainFrame.newpoint.y );
		}
		
		else if( ToolPanel.button_on == 1 )
		{
			gg.setColor( Color.white );
			for( double i = 0; i < 6.3; i+=0.2 )
			{
				int ox = ( int )( MainFrame.oldpoint.x + 2 * Math.cos(i) );
				int oy = ( int )( MainFrame.oldpoint.y + 2 * Math.sin(i) );
				int nx = ( int )( MainFrame.newpoint.x + 2 * Math.cos(i) );
				int ny = ( int )( MainFrame.newpoint.y + 2 * Math.sin(i) );
				gg.drawLine( ox, oy, nx, ny );
			}
		}
		
		else if( ToolPanel.button_on == 2 )
		{
			Graphics2D g2d = ( Graphics2D ) g;
			//g2d.setStroke(new BasicStroke( 2.0f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER, 5f, dashpattern, 10f ) );
			g2d.setColor( Color.blue );
			DrawRect( (Graphics2D)g, MainFrame.firstpoint.x,
					MainFrame.firstpoint.y, MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );
			
		}
		
		else if( ToolPanel.button_on == 3 )
		{
			int RGB = bi.getRGB( MainFrame.newpoint.x, MainFrame.newpoint.y );
			int R = ( RGB >> 16 ) & 0xFF;
			int G = ( RGB >> 8 ) & 0xFF;
			int B = ( RGB >> 0 ) & 0xFF;
			ColorPanel.Selected_Color = new Color( R, G, B );
		}
		
		else if( ToolPanel.button_on == 4 )
		{
			for( int i = 0; i < 100; i++ )
			{
				int Rand = ( int )( Math.random() * 20 );
				double Theta = Math.random() * 2 * Math.PI;
				int x = ( int )( MainFrame.newpoint.x + Rand * Math.cos(Theta) );
				int y = ( int )( MainFrame.newpoint.y + Rand * Math.sin(Theta) );
            	gg.fillOval( x, y, 1, 2 );
			}
		}
		
		else if( ToolPanel.button_on == 5 )
		{
			for( double i = -2; i < 2; i += 0.1 )
			{
				int ox = (int)( MainFrame.oldpoint.x + (-2) * i );
				int oy = (int)( MainFrame.oldpoint.y + 4 * i );
				int nx = (int)( MainFrame.newpoint.x + (-2) * i );
				int ny = (int)( MainFrame.newpoint.y + 4 * i );
				gg.drawLine( ox, oy, nx, ny );
			}
		}
		
		else if( ToolPanel.button_on == 6 )
		{
			g.setColor( ColorPanel.Selected_Color );
		    g.drawLine( MainFrame.firstpoint.x, MainFrame.firstpoint.y, MainFrame.newpoint.x, MainFrame.newpoint.y);
		    if( MainFrame.Mouse_Status == 2 )
		    {
		    	gg.setColor( ColorPanel.Selected_Color );
		    	gg.drawLine( MainFrame.firstpoint.x, MainFrame.firstpoint.y, MainFrame.newpoint.x, MainFrame.newpoint.y );
		    }
		}
		
		else if( ToolPanel.button_on == 7 )
		{
			
		}
		
		else if( ToolPanel.button_on == 8 )
		{
			g.setColor( ColorPanel.Selected_Color );
		    DrawRect( (Graphics2D)g, MainFrame.firstpoint.x,
					MainFrame.firstpoint.y, MainFrame.newpoint.x, MainFrame.newpoint.y, fill_check );
		    if( MainFrame.Mouse_Status == 2 )
		    {
		    	gg.setColor( ColorPanel.Selected_Color );
		    	DrawRect( gg, MainFrame.firstpoint.x, MainFrame.firstpoint.y,
		    			MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );	
		    }
		}
		
		else if( ToolPanel.button_on == 9 )
		{
			g.setColor( ColorPanel.Selected_Color );
		    DrawOval( (Graphics2D)g, MainFrame.firstpoint.x,
					MainFrame.firstpoint.y, MainFrame.newpoint.x, MainFrame.newpoint.y, fill_check );
		    if( MainFrame.Mouse_Status == 2 )
		    {
		    	gg.setColor( ColorPanel.Selected_Color );
		    	DrawOval( gg, MainFrame.firstpoint.x, MainFrame.firstpoint.y,
		    			MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );
		    }
		}
		
		else if( ToolPanel.button_on == 10 )
		{
			
		}
		
		else if( ToolPanel.button_on == 11 )
		{
			if( clip_check == 1 )
			{
				//gg.drawImage( clip_image, 100, 100, this );
				if( (clip_coordinate1.x < MainFrame.firstpoint.x) && 
						(clip_coordinate2.x > MainFrame.firstpoint.x) &&
						 (clip_coordinate1.y < MainFrame.firstpoint.y) &&
						 (clip_coordinate2.y > MainFrame.firstpoint.y) )
				{
					if( MainFrame.Mouse_Status == 2 )
					{
						clip_coordinate1.x += MainFrame.newpoint.x - MainFrame.firstpoint.x;
						clip_coordinate1.y += MainFrame.newpoint.y - MainFrame.firstpoint.y;
						gg.drawImage( clip_image, clip_coordinate1.x, clip_coordinate1.y, this );
						gg.setColor( Color.white );
						gg.fillRect( old_clip_coordinate1.x, old_clip_coordinate1.y, 
								old_clip_coordinate2.x - old_clip_coordinate1.x, old_clip_coordinate2.y - old_clip_coordinate1.y);
					}
					
				}
				else
				{
					clip_check = 0;
					gg.setColor( Color.white );
					gg.drawRect( clip_coordinate1.x, clip_coordinate1.y,
							clip_coordinate2.x - clip_coordinate1.x, clip_coordinate2.y - clip_coordinate1.y);
					gg.drawRect( old_clip_coordinate1.x, old_clip_coordinate1.y,
							old_clip_coordinate2.x - old_clip_coordinate1.x, old_clip_coordinate2.y - old_clip_coordinate1.y);
				}
			}
			
			if( clip_check == 0 )
			{
				Graphics2D g2d = ( Graphics2D ) g;
				//g2d.setStroke(new BasicStroke( 2.0f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER, 5f, dashpattern, 10f ) );
				g2d.setColor( Color.blue );
				DrawRect( (Graphics2D)g, MainFrame.firstpoint.x,
						MainFrame.firstpoint.y, MainFrame.oldpoint.x, MainFrame.oldpoint.y, fill_check );
				if( MainFrame.Mouse_Status == 2 )
				{
					gg.setColor( Color.blue );
					//gg.setStroke(new BasicStroke( 2.0f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER, 5f, dashpattern, 10f ) );
					DrawRect( gg, MainFrame.firstpoint.x, MainFrame.firstpoint.y,
							MainFrame.newpoint.x, MainFrame.newpoint.y, false );
					if( MainFrame.firstpoint.x < MainFrame.newpoint.x )
					{
						clip_coordinate1 = MainFrame.firstpoint; 
						clip_coordinate2 = MainFrame.newpoint;
					}
					else
					{
						clip_coordinate1 = MainFrame.newpoint; 
						clip_coordinate2 = MainFrame.firstpoint;
					}
					clip_coordinate2.x += 1;
					clip_coordinate2.y += 1;
					clip_image = bi.getSubimage( clip_coordinate1.x, clip_coordinate1.y,
							clip_coordinate2.x - clip_coordinate1.x, clip_coordinate2.y - clip_coordinate1.y );
					old_clip_coordinate1 = clip_coordinate1;
					old_clip_coordinate2 = clip_coordinate2;
					clip_check = 1;
				}
			}
		}
	}

	protected void paintComponent( Graphics g )
	{
		super.paintComponent(g);

		if( MainFrame.r == 0 )
		{
			Paint( g );
			k++;
			PaintInformation PI = new PaintInformation( MainFrame.oldpoint, MainFrame.newpoint,
					MainFrame.firstpoint, ToolPanel.LineWidth, ToolPanel.button_on, 
					ColorPanel.Selected_Color, MainFrame.Mouse_Status );
			MainFrame.vc.add( PI );
			MainFrame.oldpoint = MainFrame.newpoint;
			if( MainFrame.Mouse_Status == 2 )
			{
				Figure_Number++;
				Figure_Check[Figure_Number] = k;
			}
			g.drawImage( bi, 0, 0, this );
		}
		else if( MainFrame.r == 1 )
		{
			k -= Figure_Check[Figure_Number] - Figure_Check[Figure_Number-1];
			Figure_Number--;
			RePaint( g );
			MainFrame.r = 0;
			g.drawImage( bi, 0, 0, this );
		}
		else if( MainFrame.r == 2 )
			g.drawImage( bi, 0, 0, this );
		
	}
	
}

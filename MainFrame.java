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

public class MainFrame extends JFrame implements MouseListener, MouseMotionListener{
	
	public static JLabel statusBar;
	public static Point firstpoint, oldpoint, newpoint;
	public static int Mouse_Status = 0;
	public static Vector vc = new Vector();
	public static int r;
	private JMenuBar Menu;
	private JMenu FileMenu, ToolMenu, EditMenu, ViewMenu, HelpMenu;
	private JMenuItem NewFileItem, OpenFileItem, SaveFileItem, ExitItem,
							UndoItem, CutItem, RotateItem;
	private JFileChooser FileIO = new JFileChooser();
	private Image img;
	
	public MainFrame()
	{	
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		statusBar = new JLabel();
		
		BackgroundPanel backgroundpanel = new BackgroundPanel();
	
		DrawingPanel drawingpanel = new DrawingPanel();
		drawingpanel.setLocation( 70, 70 );
		drawingpanel.setSize( 1000, 500 );
		
		ToolPanel toolpanel = new ToolPanel();
		toolpanel.setLayout( null );
		toolpanel.setBounds( 0, 60, 60, 600 );
		
		ColorPanel colorpanel = new ColorPanel();
		colorpanel.setLayout( null );
		colorpanel.setBounds( 0, 0, 1500, 60 );
		
		this.add( statusBar, BorderLayout.SOUTH );
		this.add( toolpanel );
		this.add( colorpanel );
		this.add( drawingpanel );
		this.add( backgroundpanel );
		
		ItemHandler handler = new ItemHandler();
		Menu = new JMenuBar();
		
		FileMenu = new JMenu( "File" );
		NewFileItem = new JMenuItem( "New File" );
		OpenFileItem = new JMenuItem( "Open File" );
		SaveFileItem = new JMenuItem( "Save File" );
		ExitItem = new JMenuItem( "Exit" );
		NewFileItem.addActionListener( handler );
		OpenFileItem.addActionListener( handler );
		SaveFileItem.addActionListener( handler );
		ExitItem.addActionListener( handler );
		FileMenu.add( NewFileItem );
		FileMenu.add( OpenFileItem );
		FileMenu.add( SaveFileItem );
		FileMenu.addSeparator();
		FileMenu.add( ExitItem );
		
		ToolMenu = new JMenu( "Tool" );
		
		EditMenu = new JMenu( "Edit" );
		UndoItem = new JMenuItem( "Undo" );
		CutItem = new JMenuItem( "Cut" );
		RotateItem = new JMenuItem( "Rotate" );
		UndoItem.addActionListener( handler );
		CutItem.addActionListener( handler );
		RotateItem.addActionListener( handler );
		EditMenu.add( UndoItem );
		EditMenu.add( CutItem );
		EditMenu.add( RotateItem );
		
		ViewMenu = new JMenu( "View" );
		HelpMenu = new JMenu( "Help" );

		Menu.add( FileMenu );
		Menu.add( ToolMenu );
		Menu.add( EditMenu );
		Menu.add( ViewMenu );
		Menu.add( HelpMenu );
		
		setJMenuBar( Menu );
		
	}
	
	private class ItemHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			JFrame frame = new JFrame();
			JMenuItem selected_item = ( JMenuItem )event.getSource();
			if( selected_item == NewFileItem )
			{
				DrawingPanel.bi = new BufferedImage( 1000, 500, BufferedImage.TYPE_INT_ARGB);
				DrawingPanel.gg = DrawingPanel.bi.createGraphics();
				vc.clear();
				r = 2;
				repaint();
			}
			
			else if( selected_item == OpenFileItem )
			{
				AffineTransform tx = new AffineTransform();
				tx.translate( 0, 0 );
				AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BILINEAR);
				int ret_s = FileIO.showOpenDialog( frame );
				if (ret_s == JFileChooser.APPROVE_OPTION )
				{ 
					File file = FileIO.getSelectedFile(); 
					try{ 
						DrawingPanel.bi = ImageIO.read(file);
						DrawingPanel.bi = op.filter( DrawingPanel.bi, null );
						repaint();
					}catch ( Exception ee ){ 
						ee.printStackTrace();
					}
				}  
			}

			else if( selected_item == SaveFileItem )
			{
				int ret_s = FileIO.showSaveDialog( frame );
	            File file = FileIO.getSelectedFile(); 
				if (ret_s == JFileChooser.APPROVE_OPTION)
				{
					try {
						ImageIO.write( DrawingPanel.bi,"gif",file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			else if( selected_item == ExitItem )
			{
				System.exit( 0 );
			}
			
			else if( selected_item == UndoItem )
			{
				DrawingPanel.bi = new BufferedImage( 1000, 500, BufferedImage.TYPE_INT_ARGB);
				DrawingPanel.gg = DrawingPanel.bi.createGraphics();
				repaint();
				r = 1;
				repaint();
			}
			
			else if( selected_item == CutItem )
			{
				DrawingPanel.gg.setColor( Color.white );
				DrawingPanel.gg.fillRect( DrawingPanel.clip_coordinate1.x, DrawingPanel.clip_coordinate1.y, 
						DrawingPanel.clip_coordinate2.x - DrawingPanel.clip_coordinate1.x,
						DrawingPanel.clip_coordinate2.y - DrawingPanel.clip_coordinate1.y );
				r = 2;
				repaint();
			}
			
			else if( selected_item == RotateItem )
			{
				AffineTransform tx = new AffineTransform();
				tx.rotate( Math.PI/2, DrawingPanel.clip_image.getWidth()/2, DrawingPanel.clip_image.getHeight()/2);
				AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BILINEAR);
				DrawingPanel.clip_image = op.filter(DrawingPanel.clip_image, null );
				r = 2;
			}
			
		}
	}

	public void mouseDragged(MouseEvent event) {
		Mouse_Status = 1;
		MainFrame.statusBar.setText( String.format( "dragged at [%d, %d]", event.getX(), event.getY() ) );
		newpoint = event.getPoint();
		newpoint.x -= 78;
		newpoint.y -= 125;
		repaint();
	}

	public void mouseMoved(MouseEvent event) {
		MainFrame.statusBar.setText( String.format( "moved at [%d, %d]", event.getX(), event.getY() ) );
	}

	public void mouseClicked(MouseEvent event) {
		MainFrame.statusBar.setText( String.format( "clicked at [%d, %d]", event.getX(), event.getY() ) );
	}

	public void mousePressed(MouseEvent event) {
		Mouse_Status = 0;
		MainFrame.statusBar.setText( String.format( "pressed at [%d, %d]", event.getX(), event.getY() ) );
		firstpoint = event.getPoint();
		oldpoint = event.getPoint();
		newpoint = event.getPoint();
		firstpoint.x -= 78;
		firstpoint.y -= 125;
		oldpoint.x -= 78;
		oldpoint.y -= 125;
		newpoint.x -= 78;
		newpoint.y -= 125;
		repaint();
	}

	public void mouseReleased(MouseEvent event) {
		Mouse_Status = 2;
		MainFrame.statusBar.setText( String.format( "released at [%d, %d]", event.getX(), event.getY() ) );
		newpoint = event.getPoint();
		newpoint.x -= 78;
		newpoint.y -= 125;
		repaint();
	}

	public void mouseEntered(MouseEvent event) {
		MainFrame.statusBar.setText( String.format( "entered at [%d, %d]", event.getX(), event.getY() ) );
	}
	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
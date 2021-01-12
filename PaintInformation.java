package grimpan;
import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.Vector;

public class PaintInformation implements Serializable {
	
	private Point oldpoint, newpoint, firstpoint;
	private int  selectButton, MouseStatus, LineWidth;
	private Color selectColor;
	
	public PaintInformation( Point op, Point np, Point fp, int lw, int sb, Color c, int ms )
	{
		oldpoint = op;
		newpoint = np;
		firstpoint = fp;
		LineWidth = lw;
		selectButton = sb;
		selectColor = c;
		MouseStatus = ms;
	}
	
	public Point getOldpoint(){ return oldpoint; }
	public Point getNewpoint(){ return newpoint; }
	public Point getFirstpoint(){ return firstpoint; }
	public int getLineWidth(){ return LineWidth; }
	public int getSelectButton(){ return selectButton; }
	public Color getSelectColor(){ return selectColor; }
	public int getMouseStatus(){ return MouseStatus;}

}
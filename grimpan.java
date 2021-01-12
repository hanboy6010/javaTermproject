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

public class grimpan extends JFrame implements ActionListener, MouseListener,MouseMotionListener,ChangeListener,ItemListener,WindowListener {
	private JMenuBar jmb;
	private JMenu fileJMenu, windowJMenu;
	private JMenuItem newFileJMenuItem, openFileJMenuItem, saveFileJMenuItem, exitJMenuItem;		                      
	private JCheckBoxMenuItem toolJMenuItem, infoJMenuItem, colorButtonJMenuItem, 
											colorScrollJMenuItem, gradationJMenuItem;
	private JButton pencilJButton, straightJButton, rectJButton, circleJButton, circleRectJButton,
						  magnifierJButton, eraserJButton;
	private JPanel toolButtonJPanel, tooldownButtonJPanel, toolWestJPanel, lineWidthJPanel, checkBoxPanel, scaleJPanel, 
						mainJPanel, toolEastJPanel, arcHeightJPanel, eraserJPanel, toolNorthPanel,toolCenterPanel,toolSouthPanel;
	private JLabel pointJLabel, rgbJLabel, lineWidthJLabel, scaleJLabel, gradStartLabel,gradEndLabel,arcHeightJLabel,eraserJLabel;
	private JLabel toolUpLabel, toolDownLabel;
	private JComboBox lineWidthJComboBox, scaleJComboBox, arcHeightJComboBox, eraserJComboBox;
	private String pointStr, rgbStr; 
	private String lineWidthStr[] = {"1.0","2.0","3.0","4.0","5.0"};
	private String scaleStr[] = {"1.0","2.0","3.0","4.0","0.5"};
	private String arcHeightStr[] = {"5","10","15","20","25"};
	private String eraserStr[] = {"5.0","10","15","20","25"};
	private Graphics2D g;
	private Paint color;
	private Color startColor, endColor;
	private JCheckBox jumLineJCheckBox, fillJCheckBox, gradCheckBox;
	private int mousePointX, mousePointY, mousePointXX, mousePointYY, pressMousePointX,pressMousePointY, redColor, greenColor, blueColor;
	//100 : 연필, 101 : 직선, 102: 사각형, 103 : 원, 104 : 둥근사각, 105 : 돋보기, 106 : 지우개
	private int selectButton=100, lineWidth=1, arcHeight=5, arcWidth=5, eraser=5;
	private int oldWidth = 0, oldHeight=0, width = 0, height=0;
	private double scale = 1.0;
	private boolean isMousePress = false, isDot = false, isFill=false, isOn =false;
	private Vector  dataVector  =  new  Vector(10,5);	
	private JDialog toolJDialog,infoJDialog,colorInfoJDialog, scrollJDialog, gradientJDialog;
	private float[] dash={5.0f,5.0f,5.0f,5.0f};  	
	private JButton colorButton, whiteButton, redButton, blueButton, yellowButton, pinkButton, blackButton, gradStartButton, gradEndButton;
	private JPanel colorButtonPanel, colorButtonPanel2,redSliderPanel,greenSliderPanel,blueSliderPanel, gradPanel , gradNorthPanel, gradCenterPanel;
	private JColorChooser jcc;
	private JSlider redScroll, greenScroll, blueScroll;
	private JTextField redTextField,greenTextField,blueTextField;
	private Image buffer;
	private BufferedImage img;
	private GeneralPath gp;

	public grimpan(){
		
		super("그림판");
		setBounds(300,200,700,700);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		jmb = setMenu();
		setJMenuBar(jmb);
		//setMenuBar(jmb);
		setBackground(Color.white);
		mainJPanel = new JPanel();
		gp = new GeneralPath();
		img = (BufferedImage)createImage(getWidth(),getHeight());
		buffer = createImage(getWidth(),getHeight());
		getContentPane().add(mainJPanel);
		
		mainJPanel.setBackground(Color.white);
		color = new Color(0,0,0);
		startColor = new Color(255,255,255);
		endColor = new Color(0,0,0);
		jcc = new JColorChooser();


		//선 굵기 패널 생성
		toolNorthPanel = new JPanel();
		toolUpLabel = new JLabel("선굵기");
		lineWidthJComboBox = new JComboBox(lineWidthStr);
		lineWidthJComboBox.setSelectedIndex(lineWidth-1);
		toolNorthPanel.add(toolUpLabel);
		toolNorthPanel.add(lineWidthJComboBox);

		//돋보기 패널 생성
		scaleJComboBox = new JComboBox(scaleStr);
		scaleJComboBox.setSelectedIndex(lineWidth-1);		

		//라운드 패널 생성
		toolSouthPanel = new JPanel();
		toolDownLabel = new JLabel("라운드");
		arcHeightJComboBox = new JComboBox(arcHeightStr);
		arcHeightJComboBox.setSelectedIndex(0);
		arcHeightJComboBox.setEnabled(false);
		toolSouthPanel.add(toolDownLabel);
		toolSouthPanel.add(arcHeightJComboBox);

		//지우기 콤보박스 생성
		eraserJComboBox = new JComboBox(eraserStr);
		eraserJComboBox.setSelectedIndex(0);		

		//체크박스 및 아이템 설정
		toolCenterPanel = new JPanel();
		jumLineJCheckBox = new JCheckBox("점선");
		fillJCheckBox = new JCheckBox("fill");
		fillJCheckBox.setEnabled(false);
		toolCenterPanel.add(jumLineJCheckBox);
		toolCenterPanel.add(fillJCheckBox);


		//getContentPane().add(mainJPanel,"Center");		

		toolDialog();
		infoDialog();
		colorButtonDialog();
		colorScrollDialog();
		gradDialog();
		toolJDialog.setVisible(true);
		infoJDialog.setVisible(true);
		colorInfoJDialog.setVisible(true);
		scrollJDialog.setVisible(true);
		gradientJDialog.setVisible(true);
		
		lineWidthJComboBox.addActionListener(this);
		arcHeightJComboBox.addActionListener(this);
		jumLineJCheckBox.addActionListener(this);
		fillJCheckBox.addActionListener(this);
		scaleJComboBox.addActionListener(this);
		eraserJComboBox.addActionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);		
		toolJDialog.addWindowListener(this);
		infoJDialog.addWindowListener(this);
		colorInfoJDialog.addWindowListener(this);
		scrollJDialog.addWindowListener(this);
		gradientJDialog.addWindowListener(this);
	}
	
	public void setPointStr(){
		pointStr = "x : " + mousePointX + ", y : " + mousePointY;
	}

	public void setRgbStr(){
		rgbStr = "r : " + redColor + " g : " + greenColor + " b : " + blueColor;
	}

	public void toolDialog(){
		toolJDialog = new JDialog(this,"도구");
		toolJDialog.setResizable(false);
		toolJDialog.setBounds(150,200,150,400);
		toolJDialog.setLayout(new BorderLayout());

		toolButtonJPanel = new JPanel(new GridLayout(7,1));
		pencilJButton = new JButton("연필");
		setEnableFalseButton(pencilJButton);
		straightJButton = new JButton("직선");
		rectJButton = new JButton("네모");
		circleJButton = new JButton("원");
		circleRectJButton = new JButton("둥근네모");
		magnifierJButton = new JButton("돋보기");
		eraserJButton = new JButton("지우개");
		
		//버튼 패널에 add
		toolButtonJPanel.add(pencilJButton);
		toolButtonJPanel.add(straightJButton);
		toolButtonJPanel.add(rectJButton);
		toolButtonJPanel.add(circleJButton);
		toolButtonJPanel.add(circleRectJButton);
		toolButtonJPanel.add(magnifierJButton);
		toolButtonJPanel.add(eraserJButton);

		//버튼에 이벤트 add
		pencilJButton.addActionListener(this);
		straightJButton.addActionListener(this);
		rectJButton.addActionListener(this);
		circleJButton.addActionListener(this);
		circleRectJButton.addActionListener(this);
		magnifierJButton.addActionListener(this);
		eraserJButton.addActionListener(this);

		tooldownButtonJPanel = new JPanel(new GridLayout(3,1));
		tooldownButtonJPanel.add(toolNorthPanel,"North");
		tooldownButtonJPanel.add(toolCenterPanel,"Center");
		tooldownButtonJPanel.add(toolSouthPanel,"South");

		toolJDialog.add(toolButtonJPanel,"North");
		toolJDialog.add(tooldownButtonJPanel,"Center");
	}

	
	public void infoDialog(){
		infoJDialog = new JDialog(this,"정보");
		infoJDialog.setResizable(false);
		infoJDialog.setBounds(1000,200,100,100);
		infoJDialog.setLayout(new GridLayout(2,1,2,15));

		setPointStr();
		setRgbStr();
		pointJLabel = new JLabel(pointStr);
		rgbJLabel = new JLabel(rgbStr);
		infoJDialog.add(pointJLabel);
		infoJDialog.add(rgbJLabel);
	}

	public void colorButtonDialog(){
		colorInfoJDialog = new JDialog(this,"색상버튼");		
		colorInfoJDialog.setResizable(false);
		colorInfoJDialog.setBounds(1000,300,100,80);
				
		colorButton = new JButton();
		colorButton.setBackground(Color.black);

		colorButtonPanel = new JPanel(new GridLayout(1,6,5,5));
		whiteButton = new JButton();
		whiteButton.setBackground(Color.white);
		redButton = new JButton();
		redButton.setBackground(Color.red);
		blueButton = new JButton();
		blueButton.setBackground(Color.blue);
		yellowButton = new JButton();
		yellowButton.setBackground(Color.yellow);
		pinkButton = new JButton();
		pinkButton.setBackground(Color.pink);
		blackButton = new JButton();
		blackButton.setBackground(Color.black);
		colorButtonPanel.add(whiteButton);
		colorButtonPanel.add(redButton);
		colorButtonPanel.add(blueButton);
		colorButtonPanel.add(yellowButton);
		colorButtonPanel.add(pinkButton);
		colorButtonPanel.add(blackButton);

		whiteButton.addActionListener(this);
		redButton.addActionListener(this);
		blueButton.addActionListener(this);
		yellowButton.addActionListener(this);
		pinkButton.addActionListener(this);
		blackButton.addActionListener(this);
		colorButton.addActionListener(this);

		colorButtonPanel2 = new JPanel(new GridLayout(2,1,5,5));
		colorButtonPanel2.add(colorButton);
		colorButtonPanel2.add(colorButtonPanel);
		colorInfoJDialog.add(colorButtonPanel2);
	}

	public void colorScrollDialog(){
		scrollJDialog = new JDialog(this,"색상스크롤");	
		scrollJDialog.setResizable(false);
		scrollJDialog.setBounds(1000,400,200,130);
		scrollJDialog.setLayout(new GridLayout(3,1,5,5));

		redSliderPanel = new JPanel();
		redTextField = new JTextField("0",3);
		redTextField.setEnabled(false);
		redScroll = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		redScroll.setBackground(Color.red);
		redScroll.setPreferredSize(new Dimension(100,20));
		redSliderPanel.add(redScroll);
		redSliderPanel.add(redTextField);


		greenSliderPanel = new JPanel();
		greenTextField = new JTextField("0",3);
		greenTextField.setEnabled(false);
		greenScroll = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		greenScroll.setBackground(Color.green);
		greenScroll.setPreferredSize(new Dimension(100,20));
		greenSliderPanel.add(greenScroll);
		greenSliderPanel.add(greenTextField);


		blueSliderPanel = new JPanel();
		blueTextField = new JTextField("0",3);
		blueTextField.setEnabled(false);
		blueScroll = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		blueScroll.setBackground(Color.blue);
		blueScroll.setPreferredSize(new Dimension(100,20));
		blueSliderPanel.add(blueScroll);
		blueSliderPanel.add(blueTextField);


		scrollJDialog.add(redSliderPanel);	
		scrollJDialog.add(greenSliderPanel);
		scrollJDialog.add(blueSliderPanel);
		redScroll.addChangeListener(this);
		greenScroll.addChangeListener(this);
		blueScroll.addChangeListener(this);
	}

	public void gradDialog(){
		gradientJDialog = new JDialog(this,"그라데이션");	
		gradientJDialog.setResizable(false);
		gradientJDialog.setBounds(1000,600,100,100);

		gradPanel = new JPanel(new GridLayout(3,1,5,5));
		gradNorthPanel = new JPanel();
		gradCenterPanel = new JPanel();

		gradStartLabel = new JLabel("START");
		gradEndLabel = new JLabel("END");
		gradStartButton = new JButton();
		gradStartButton.setBackground((Color)startColor);
		gradEndButton = new JButton();
		gradEndButton.setBackground((Color)endColor);
		gradCheckBox = new JCheckBox("적용");

		gradNorthPanel.add(gradStartLabel);
		gradNorthPanel.add(gradStartButton);
		gradCenterPanel.add(gradEndLabel);
		gradCenterPanel.add(gradEndButton);
		gradPanel.add(gradNorthPanel);
		gradPanel.add(gradCenterPanel);
		gradPanel.add(gradCheckBox);

		gradientJDialog.add(gradPanel);

		gradStartButton.addActionListener(this);
		gradEndButton.addActionListener(this);
		gradCheckBox.addActionListener(this);
	}	

	public void setEnableFalseButton(JButton jb){
		jb.setEnabled(false);
	}

	public void setEnableTrueButton(JButton jb){
		jb.setEnabled(true);
	}

	//ActionEvent
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == pencilJButton){
			selectButton = 100;	
			toolUpLabel.setText("선굵기");
			jumLineJCheckBox.setEnabled(true);
			arcHeightJComboBox.setEnabled(false);
			fillJCheckBox.setEnabled(false);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(lineWidthJComboBox);

			setEnableFalseButton(pencilJButton);
			setEnableTrueButton(straightJButton);
			setEnableTrueButton(rectJButton);
			setEnableTrueButton(circleJButton);
			setEnableTrueButton(circleRectJButton);
			setEnableTrueButton(magnifierJButton);
			setEnableTrueButton(eraserJButton);
	
		}else if(e.getSource() == straightJButton){
			selectButton = 101;
			toolUpLabel.setText("선굵기");
			jumLineJCheckBox.setEnabled(true);
			arcHeightJComboBox.setEnabled(false);
			fillJCheckBox.setEnabled(false);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(lineWidthJComboBox);

			setEnableFalseButton(straightJButton);
			setEnableTrueButton(pencilJButton);
			setEnableTrueButton(rectJButton);
			setEnableTrueButton(circleJButton);
			setEnableTrueButton(circleRectJButton);
			setEnableTrueButton(magnifierJButton);
			setEnableTrueButton(eraserJButton);

		}else if(e.getSource() == rectJButton){
			selectButton = 102;
			toolUpLabel.setText("선굵기");
			jumLineJCheckBox.setEnabled(true);
			arcHeightJComboBox.setEnabled(false);
			fillJCheckBox.setEnabled(true);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(lineWidthJComboBox);

			setEnableFalseButton(rectJButton);
			setEnableTrueButton(straightJButton);
			setEnableTrueButton(pencilJButton);
			setEnableTrueButton(circleJButton);
			setEnableTrueButton(circleRectJButton);
			setEnableTrueButton(magnifierJButton);
			setEnableTrueButton(eraserJButton);
		}else if(e.getSource() == circleJButton){			
			selectButton = 103;
			toolUpLabel.setText("선굵기");
			jumLineJCheckBox.setEnabled(true);
			arcHeightJComboBox.setEnabled(false);
			fillJCheckBox.setEnabled(true);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(lineWidthJComboBox);

			setEnableFalseButton(circleJButton);
			setEnableTrueButton(straightJButton);
			setEnableTrueButton(rectJButton);
			setEnableTrueButton(pencilJButton);
			setEnableTrueButton(circleRectJButton);
			setEnableTrueButton(magnifierJButton);
			setEnableTrueButton(eraserJButton);
		}else if(e.getSource() == circleRectJButton){			
			selectButton = 104;
			jumLineJCheckBox.setEnabled(true);
			toolUpLabel.setText("선굵기");
			arcHeightJComboBox.setEnabled(true);
			fillJCheckBox.setEnabled(true);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(lineWidthJComboBox);

			setEnableFalseButton(circleRectJButton);
			setEnableTrueButton(straightJButton);
			setEnableTrueButton(rectJButton);
			setEnableTrueButton(circleJButton);
			setEnableTrueButton(pencilJButton);
			setEnableTrueButton(magnifierJButton);
			setEnableTrueButton(eraserJButton);
		}else if(e.getSource() == magnifierJButton){
			selectButton = 105;
			toolUpLabel.setText("돋보기");
			jumLineJCheckBox.setEnabled(true);
			arcHeightJComboBox.setEnabled(false);
			fillJCheckBox.setEnabled(false);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(scaleJComboBox);


			setEnableFalseButton(magnifierJButton);
			setEnableTrueButton(straightJButton);
			setEnableTrueButton(rectJButton);
			setEnableTrueButton(circleJButton);
			setEnableTrueButton(circleRectJButton);
			setEnableTrueButton(pencilJButton);
			setEnableTrueButton(eraserJButton);		
			repaint();
		}else if(e.getSource() == eraserJButton){
			selectButton = 106;
			toolUpLabel.setText("지우개");
			arcHeightJComboBox.setEnabled(false);
			fillJCheckBox.setEnabled(false);
			jumLineJCheckBox.setEnabled(false);
			toolNorthPanel.removeAll();
			toolNorthPanel.add(toolUpLabel);
			toolNorthPanel.add(eraserJComboBox);

			setEnableFalseButton(eraserJButton);
			setEnableTrueButton(straightJButton);
			setEnableTrueButton(rectJButton);
			setEnableTrueButton(circleJButton);
			setEnableTrueButton(circleRectJButton);
			setEnableTrueButton(magnifierJButton);
			setEnableTrueButton(pencilJButton);
		}else if(e.getSource() ==lineWidthJComboBox){
			if(((String)lineWidthJComboBox.getSelectedItem()).equals("1.0")){
				lineWidth = 1;
			}else if(((String)lineWidthJComboBox.getSelectedItem()).equals("2.0")){
				lineWidth = 2;
			}else if(((String)lineWidthJComboBox.getSelectedItem()).equals("3.0")){
				lineWidth = 3;
			}else if(((String)lineWidthJComboBox.getSelectedItem()).equals("4.0")){
				lineWidth = 4;
			}else if(((String)lineWidthJComboBox.getSelectedItem()).equals("5.0")){
				lineWidth = 5;
			}		
		}else if(e.getSource() == jumLineJCheckBox){
			if(jumLineJCheckBox.isSelected())
				isDot = true;
			else
				isDot = false;
		}else if(e.getSource() == fillJCheckBox){
			if(fillJCheckBox.isSelected())
				isFill = true;
			else
				isFill = false;
		}else if(e.getSource() == scaleJComboBox){
			if(((String)scaleJComboBox.getSelectedItem()).equals("1.0")){
				scale = 1.0;
			}else if(((String)scaleJComboBox.getSelectedItem()).equals("2.0")){
				scale = 2.0;
			}else if(((String)scaleJComboBox.getSelectedItem()).equals("3.0")){
				scale = 3.0;
			}else if(((String)scaleJComboBox.getSelectedItem()).equals("4.0")){
				scale = 4.0;
			}else if(((String)scaleJComboBox.getSelectedItem()).equals("0.5")){
				scale = 0.5;
			}

			repaint();
		}else if(e.getSource() ==whiteButton ){
			gradCheckBox. setSelected(false);
			color = Color.white;			
			colorButton.setBackground((Color)color);	
			setColor();
			g.setPaint(color);
		}else if(e.getSource() ==redButton ){
			gradCheckBox. setSelected(false);
			color = Color.red;
			colorButton.setBackground((Color)color);
			setColor();
			g.setPaint(color);
		}else if(e.getSource() ==blueButton ){
			gradCheckBox. setSelected(false);
			color = Color.blue;
			colorButton.setBackground((Color)color);
			setColor();
			g.setPaint(color);
		}else if(e.getSource() ==yellowButton ){
			gradCheckBox. setSelected(false);
			color = Color.yellow;
			colorButton.setBackground((Color)color);
			setColor();
			g.setPaint(color);
		}else if(e.getSource() ==pinkButton ){
			gradCheckBox. setSelected(false);
			color = Color.pink;
			colorButton.setBackground((Color)color);
			setColor();
			g.setPaint(color);
		}else if(e.getSource() ==blackButton ){
			gradCheckBox. setSelected(false);
			color = Color.black;
			colorButton.setBackground((Color)color);
			setColor();
			g.setPaint(color);
		}else if(e.getSource() ==colorButton ){
			color =jcc.showDialog(this, "색상선택", colorButton.getBackground());
			if(color == null){
				if(gradCheckBox.isSelected()){
					color = new GradientPaint(0.0f,0.0f,startColor,50.0f,50.0f,endColor,true);
					g.setPaint(color);
				}else{
					color = new Color(redColor,greenColor,blueColor);
					g.setPaint(color);
				}
			}
			else{
				gradCheckBox. setSelected(false);
				setColor();
				colorButton.setBackground((Color)color);
				g.setPaint(color);
			}
		}else if(e.getSource() == gradStartButton ){
			startColor =jcc.showDialog(this, "색상선택", colorButton.getBackground());
			gradStartButton.setBackground(startColor);
			if(gradCheckBox.isSelected()){
				color = new GradientPaint(0.0f,0.0f,startColor,50.0f,50.0f,endColor,true);
				g.setPaint(color);
			}
		}else if(e.getSource() == gradEndButton ){
			endColor =jcc.showDialog(this, "색상선택", colorButton.getBackground());
			gradEndButton.setBackground(endColor);
			if(gradCheckBox.isSelected()){
				color = new GradientPaint(0.0f,0.0f,startColor,50.0f,50.0f,endColor,true);
				g.setPaint(color);
			}
		}else if(e.getSource() == gradCheckBox ){
			if(gradCheckBox.isSelected()){
				color = new GradientPaint(0.0f,0.0f,startColor,50.0f,50.0f,endColor,true);
				g.setPaint(color);
			}else{
				color = new Color(redColor,greenColor,blueColor);
				g.setPaint(color);
			}
		}else if(e.getSource() == arcHeightJComboBox){
			if(((String)arcHeightJComboBox.getSelectedItem()).equals("5")){
				arcHeight = 5;
			}else if(((String)arcHeightJComboBox.getSelectedItem()).equals("10")){
				arcHeight = 10;
			}else if(((String)arcHeightJComboBox.getSelectedItem()).equals("15")){
				arcHeight = 15;
			}else if(((String)arcHeightJComboBox.getSelectedItem()).equals("20")){
				arcHeight = 20;
			}else if(((String)arcHeightJComboBox.getSelectedItem()).equals("25")){
				arcHeight = 25;
			}
			arcWidth = arcHeight;
		}else if(e.getSource() == eraserJComboBox){
			if(((String)eraserJComboBox.getSelectedItem()).equals("5.0")){
				eraser = 5;
			}else if(((String)eraserJComboBox.getSelectedItem()).equals("10")){
				eraser = 10;
			}else if(((String)eraserJComboBox.getSelectedItem()).equals("15")){
				eraser = 15;
			}else if(((String)eraserJComboBox.getSelectedItem()).equals("20")){
				eraser = 20;
			}else if(((String)eraserJComboBox.getSelectedItem()).equals("25")){
				eraser = 25;
			}
		}else if(e.getSource() == newFileJMenuItem){
			dataVector.clear();
			g = (Graphics2D)img.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0,55,getWidth(),getHeight());
			g.drawImage(img,null,null);
			repaint();
		}else if(e.getSource() == openFileJMenuItem){
			JFileChooser chooser_open = new JFileChooser(); 
           int ret_s = chooser_open.showOpenDialog(this); 

           if (ret_s == JFileChooser.APPROVE_OPTION ){ 
               File file = chooser_open.getSelectedFile();  
               try{ 
				   drawData dd = new drawData();
				   dataVector.clear();
					//g.setColor(Color.white);
					//g.fillRect(0,0,getWidth(),getHeight());
				   dd.img = ImageIO.read(file);
				   dd.selectButton = 107;
				   dataVector.addElement(dd);
				   repaint();
               }catch ( Exception ee ){ 
                    ee.printStackTrace();
               }
           }  

		}else if(e.getSource() == saveFileJMenuItem){
			JFileChooser chooser_save= new JFileChooser(); 
			 int ret_s = chooser_save.showSaveDialog(this);
            File file = chooser_save.getSelectedFile(); 
			if (ret_s == JFileChooser.APPROVE_OPTION){
               try{				   
				   //g = (Graphics2D)img.getGraphics();
				   setRepaint(g);				  
				   repaint();
				   ImageIO.write(img,"jpg",file);
               }catch (Exception ee ){
					ee.printStackTrace();
			   }
			}
           }else if(e.getSource() == exitJMenuItem){
			System.exit(0);
		}
	}

	public void setColor(){
		Color c = (Color)color;
		redColor =c.getRed();
		greenColor = c.getGreen();
		blueColor = c.getBlue();
		setRgbStr();
		rgbJLabel.setText(rgbStr);
		redScroll.setValue(redColor);
		greenScroll.setValue(greenColor);
		blueScroll.setValue(blueColor);
		redTextField.setText(String.valueOf(redColor));
		greenTextField.setText(String.valueOf(greenColor));
		blueTextField.setText(String.valueOf(blueColor));
	}

	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == redScroll){
			redColor = redScroll.getValue();
			redTextField.setText(String.valueOf(redColor));
		}else if(e.getSource() == greenScroll){
			greenColor = greenScroll.getValue();
			greenTextField.setText(String.valueOf(greenColor));
		}else if(e.getSource() == blueScroll){
			blueColor = blueScroll.getValue();
			blueTextField.setText(String.valueOf(blueColor));
		}
		color = new Color(redColor,greenColor,blueColor);
		gradCheckBox. setSelected(false);
		g.setPaint(color);
		colorButton.setBackground((Color)color);
		setRgbStr();
		rgbJLabel.setText(rgbStr);
	}

	class  drawData{
		int  mousePointX,  mousePointY;
		int  mousePointXX,  mousePointYY;
		int  selectButton;  
		int lineWidth;
		int arcWidth;
		int arcHeight;
		int eraser;
		Paint color;
		boolean isDot=false;
		boolean isFill=false;
		Image img;
	}

	
	public void drawPencil(int x1, int y1, int x2, int y2){
		if(isMousePress == true){
			drawData data = new drawData();
			data.mousePointX=x1;
			data.mousePointY=y1;
			data.mousePointXX=x2;
			data.mousePointYY=y2;
			data.lineWidth=lineWidth;
			data.color=color;
			data.selectButton=100;
			if(isDot == false){
				g.setStroke(new BasicStroke(lineWidth));
			}else{				
				g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
				data.isDot = isDot;
			}
			g.drawLine(x1,y1-55,x2,y2-55);
			getGraphics().drawImage(img,0,55,this);
			dataVector.addElement(data);
		}
	}

	public void drawStraight(int x1, int y1, int x2, int y2, int x3, int y3){
		Line2D.Double line = new Line2D.Double();
		if(isMousePress == true){
			g.setXORMode(Color.white);
			if(isDot == false){
				g.setStroke(new BasicStroke(lineWidth));
			}else{
				g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
			}		

			line.setLine(x1,y1-55,x3,y3-55);
			g.draw(line);			
			getGraphics().drawImage(img,0,55,this);
			

			line.setLine(x1,y1-55,x2,y2-55);
			g.draw(line);			
			getGraphics().drawImage(img,0,55,this);
			
		}
	}
	public void drawRect(int x1, int y1, int x2, int y2, int x3, int y3){
		if(isMousePress == true){			
			gp.reset();	
			g.setXORMode(Color.white);
			if(isDot == false){
				g.setStroke(new BasicStroke(lineWidth));
			}else{				
				g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
			}
			if(isFill){
				gp.moveTo(x1,y1-55); 
				gp.lineTo(x3,y1-55);   
				gp.lineTo(x3,y3-55);   
				gp.lineTo(x1,y3-55);   
				gp.closePath();      
				g.fill(gp);
				getGraphics().drawImage(img,0,55,this);

				gp.reset();
				//g.setPaintMode();
				gp.moveTo(x1,y1-55);
				gp.lineTo(x2,y1-55);  
				gp.lineTo(x2,y2-55);  
				gp.lineTo(x1,y2-55);  
				gp.closePath();     
				g.fill(gp);	
				getGraphics().drawImage(img,0,55,this);

			}else{
				gp.moveTo(x1,y1-55); 
				gp.lineTo(x3,y1-55);   
				gp.lineTo(x3,y3-55);   
				gp.lineTo(x1,y3-55);   
				gp.closePath();      
				g.draw(gp);
				getGraphics().drawImage(img,0,55,this);


				gp.reset();

				gp.moveTo(x1,y1-55);
				gp.lineTo(x2,y1-55);  
				gp.lineTo(x2,y2-55);  
				gp.lineTo(x1,y2-55);  
				gp.closePath();     
				g.draw(gp);	
				getGraphics().drawImage(img,0,55,this);

			}			
		}
	}

	public void drawCircle(int x1, int y1, int x2, int y2, int x3, int y3){
		if(isMousePress == true){
			g.setXORMode(Color.white);
			if(isDot == false){
				g.setStroke(new BasicStroke(lineWidth));
			}else{				
				g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
			}

			oldWidth  =  x3  -  x1;
			oldHeight  =  y3  -  y1;

			width  =  x2  -  x1;
			height  =  y2  -  y1;

			if(isFill){
				g.fillOval (x1,  y1-55,  oldWidth,  oldHeight);
				getGraphics().drawImage(img,0,55,this);
			    g.fillOval(x1,  y1-55,  width,  height);	
				getGraphics().drawImage(img,0,55,this);
			}else{				
				g.drawOval (x1,  y1-55,  oldWidth,  oldHeight);
				getGraphics().drawImage(img,0,55,this);
			    g.drawOval(x1,  y1-55,  width,  height);	
				getGraphics().drawImage(img,0,55,this);
			}			
		}
	}

	public void drawCircleRect(int x1, int y1, int x2, int y2, int x3, int y3){		
		if(isMousePress == true){	
			g.setXORMode(Color.white);
			if(isDot == false){
				g.setStroke(new BasicStroke(lineWidth));
			}else{				
				g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
			}

			oldWidth  =  x3  -  x1;
			oldHeight  =  y3  -  y1;

			width  =  x2  -  x1;
			height  =  y2  -  y1;

			if(isFill){
				g.fillRoundRect (x1,  y1-55,  oldWidth,  oldHeight,arcWidth,arcHeight);
				getGraphics().drawImage(img,0,55,this);
			    g.fillRoundRect(x1,  y1-55,  width,  height,arcWidth,arcHeight);	
				getGraphics().drawImage(img,0,55,this);
			}else{
				g.drawRoundRect(x1,  y1-55,  oldWidth,  oldHeight,arcWidth,arcHeight);
				getGraphics().drawImage(img,0,55,this);
			    g.drawRoundRect(x1,  y1-55,  width,  height,arcWidth,arcHeight);	
				getGraphics().drawImage(img,0,55,this);
			}

		}
	}

	public void drawEraser(int x1, int y1){
		drawData  data=new  drawData();
		g.setPaint(Color.white);
		data.mousePointX=x1;
		data.mousePointY=y1;
		data.eraser=eraser;
		data.color = Color.white;
		data.selectButton=106;
		if(isMousePress == true){
			g.fillRect(x1,y1-55,eraser,eraser);
			getGraphics().drawImage(img,0,55,this);
		}
		dataVector.addElement(data);
	}

  public  void  paint(Graphics  gg){
		  if(!isOn){
			  try{			  
				Thread.sleep(1000);
				isOn=true;
			  }catch(Exception e){ }
		  }		  		  
		  //g = (Graphics2D)img.getGraphics();
		 //g = (Graphics2D)getGraphics();		 
		 g = (Graphics2D)img.getGraphics();
		 setRepaint(g);  		 
		 getGraphics().drawImage(img,0,55,this);
		jmb.updateUI();
      }

	public void setRepaint(Graphics2D g){
		if(magnifierJButton.isEnabled() == false){
			g.setColor(Color.white);
			g.fillRect(0,0,getWidth(),getHeight());
			g.scale(scale,scale);						
		}else{
			g.setColor(Color.white);
			g.fillRect(0,0,getWidth(),getHeight());
			g.scale(1.0,1.0);
		}	

         drawData  data;
         int  w,  h;
            for(int i=0  ;  i<dataVector.size();  i++){
                data  =  (drawData)dataVector.elementAt(i);
				if(data.selectButton==101 || data.selectButton == 100){
					g.setPaint(data.color);
					if(data.isDot)
						g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
					else
						g.setStroke(new BasicStroke(data.lineWidth));

					g.drawLine(data.mousePointX,  data.mousePointY-55,data.mousePointXX,data.mousePointYY-55);
				}else if(data.selectButton==102){
					gp.reset(); 
                    w=  data.mousePointXX  -  data.mousePointX;
                    h=  data.mousePointYY  -  data.mousePointY;
					g.setPaint(data.color);
					if(data.isDot)
						g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
					else
						g.setStroke(new BasicStroke(data.lineWidth));
					
					gp.moveTo(data.mousePointX,data.mousePointY-55);
					gp.lineTo(data.mousePointXX,data.mousePointY-55);  
					gp.lineTo(data.mousePointXX,data.mousePointYY-55);  
					gp.lineTo(data.mousePointX,data.mousePointYY-55);  
					gp.closePath();     

					if(data.isFill)
						g.fill(gp);
					else						
						g.draw(gp);

					gp.reset(); 
                }else if(data.selectButton==103){
                    w=  data.mousePointXX  -  data.mousePointX;
                    h=  data.mousePointYY  -  data.mousePointY;
					g.setPaint(data.color);
					if(data.isDot)
						g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
					else
						g.setStroke(new BasicStroke(data.lineWidth));

					if(data.isFill)
						g.fillOval(data.mousePointX,  data.mousePointY-55,w,h);
					else
						g.drawOval(data.mousePointX,  data.mousePointY-55,w,h);
                }else if(data.selectButton==104){
                    w=  data.mousePointXX  -  data.mousePointX;
                    h=  data.mousePointYY  -  data.mousePointY;
					g.setPaint(data.color);
					if(data.isDot)
						g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1.0f,dash, 0));
					else
						g.setStroke(new BasicStroke(data.lineWidth));

					if(data.isFill)
						g.fillRoundRect(data.mousePointX,  data.mousePointY-55,w,h,data.arcWidth,data.arcHeight);
					else
						g.drawRoundRect(data.mousePointX,  data.mousePointY-55,w,h,data.arcWidth,data.arcHeight);
                }else if(data.selectButton == 106){
					g.setPaint(data.color);
					g.fillRect(data.mousePointX, data.mousePointY-55,data.eraser,data.eraser);
				}else if(data.selectButton==107){
					g.drawImage(data.img,null,null);
				}
            }
			g.setPaint(color);
		}

	//MouseListener
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
	}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){	
		mousePointX = e.getX();
		mousePointY = e.getY();
		pressMousePointX = e.getX();
		pressMousePointY = e.getY();
		isMousePress = true;
		repaint();
	}
	public void mouseReleased(MouseEvent e){
		 drawData  data=new  drawData();
		data.mousePointX=pressMousePointX;
		data.mousePointY=pressMousePointY;
		data.mousePointXX=e.getX();
		data.mousePointYY=e.getY();
		data.lineWidth=lineWidth;
		data.color=color;
		data.isDot = isDot;
		data.isFill = isFill;
		data.arcWidth = arcWidth;
		data.arcHeight = arcHeight;
		if(selectButton == 101){			
			data.selectButton=101;
		}else if(selectButton == 102){
			data.selectButton=102;
		}else if(selectButton == 103){
			data.selectButton=103;
		}else if(selectButton == 104){
			data.selectButton=104;
		}else if(selectButton == 106){
			data.selectButton=106;
		}
		dataVector.addElement(data);
		isMousePress = false;
		repaint();		
	}
	

	//MouseMotionListener
	public void mouseDragged(MouseEvent e){
		mousePointXX = mousePointX;
		mousePointYY = mousePointY;
		mousePointX = e.getX();
		mousePointY = e.getY();
		if(selectButton == 100){
			drawPencil(mousePointXX, mousePointYY,mousePointX,mousePointY);
		}else if(selectButton == 101){	
			drawStraight(pressMousePointX,pressMousePointY,mousePointX,mousePointY,mousePointXX,mousePointYY);
		}else if(selectButton == 102){	
			drawRect(pressMousePointX,pressMousePointY,mousePointX,mousePointY,mousePointXX,mousePointYY);
		}else if(selectButton == 103){	
			drawCircle(pressMousePointX,pressMousePointY,mousePointX,mousePointY,mousePointXX,mousePointYY);
		}else if(selectButton == 104){	
			drawCircleRect(pressMousePointX,pressMousePointY,mousePointX,mousePointY,mousePointXX,mousePointYY);
		}else if(selectButton == 106){
			drawEraser(mousePointX,mousePointY);
		}
		//repaint();
		setPointStr();
		setRgbStr();
		pointJLabel.setText(pointStr);
		rgbJLabel.setText(rgbStr);	
	}    
	public void mouseMoved(MouseEvent e){
		mousePointX = e.getX();
		mousePointY = e.getY();
		setPointStr();
		setRgbStr();
		pointJLabel.setText(pointStr);
		rgbJLabel.setText(rgbStr);	
	}

	public JMenuBar setMenu(){
		JMenuBar jmb = new JMenuBar();

		fileJMenu = new JMenu("파일");
		newFileJMenuItem = new JMenuItem("새파일");
		openFileJMenuItem = new JMenuItem("열기");
		saveFileJMenuItem = new JMenuItem("저장");
		exitJMenuItem = new JMenuItem("끝내기");
		fileJMenu.add(newFileJMenuItem);
		fileJMenu.add(openFileJMenuItem);
		fileJMenu.add(saveFileJMenuItem);
		fileJMenu.addSeparator();
		fileJMenu.add(exitJMenuItem);


		windowJMenu = new JMenu("윈도우");
		toolJMenuItem = new JCheckBoxMenuItem("도구");
		infoJMenuItem = new JCheckBoxMenuItem("정보");
		colorButtonJMenuItem = new JCheckBoxMenuItem("색상버튼");
		colorScrollJMenuItem = new JCheckBoxMenuItem("색상스크롤");
		gradationJMenuItem = new JCheckBoxMenuItem("그라데이션");
		toolJMenuItem.setState(true);
		infoJMenuItem.setState(true);
		colorButtonJMenuItem.setState(true);
		colorScrollJMenuItem.setState(true);
		gradationJMenuItem.setState(true);
		windowJMenu.add(toolJMenuItem);
		windowJMenu.add(infoJMenuItem);
		windowJMenu.add(colorButtonJMenuItem);
		windowJMenu.add(colorScrollJMenuItem);
		windowJMenu.add(gradationJMenuItem);

		newFileJMenuItem.addActionListener(this);
		openFileJMenuItem.addActionListener(this);
		saveFileJMenuItem.addActionListener(this);
		exitJMenuItem.addActionListener(this);

		toolJMenuItem.addItemListener(this);
		infoJMenuItem.addItemListener(this);
		colorButtonJMenuItem.addItemListener(this);
		colorScrollJMenuItem.addItemListener(this);
		gradationJMenuItem.addItemListener(this);

		jmb.add(fileJMenu);
		jmb.add(windowJMenu);

		return jmb;
	}

	public void itemStateChanged(ItemEvent e){
		if(e.getItem() == toolJMenuItem){
			if(toolJMenuItem.getState() == true){
				toolJDialog.setVisible(true);				
			}else
				toolJDialog.setVisible(false);
			}else if(e.getItem() == infoJMenuItem){
			if(infoJMenuItem.getState() == true){
				infoJDialog.setVisible(true);
			}else
				infoJDialog.setVisible(false);

		}else if(e.getItem() == colorButtonJMenuItem){
			if(colorButtonJMenuItem.getState() == true){
				colorInfoJDialog.setVisible(true);
			}else
				colorInfoJDialog.setVisible(false);

		}else if(e.getItem() == colorScrollJMenuItem){
			if(colorScrollJMenuItem.getState() == true){
				scrollJDialog.setVisible(true);
			}else
				scrollJDialog.setVisible(false);

		}else if(e.getItem() == gradationJMenuItem){
			if(gradationJMenuItem.getState() == true){
				gradientJDialog.setVisible(true);
			}else
				gradientJDialog.setVisible(false);
		}
	}

	public  void windowActivated(WindowEvent e) {}          
	public  void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		if(e.getSource() == toolJDialog){
			toolJMenuItem.setState(false);
		}else if(e.getSource() == infoJDialog){
			infoJMenuItem.setState(false);
		}else if(e.getSource() == colorInfoJDialog){
			colorButtonJMenuItem.setState(false);
		}else if(e.getSource() == scrollJDialog){
			colorScrollJMenuItem.setState(false);
		}else if(e.getSource() == gradientJDialog){
			gradationJMenuItem.setState(false);
		}

	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e)  {}


	public static void main(String args[]){
		new grimpan();
	}
}
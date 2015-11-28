package go;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;


@SuppressWarnings("serial")
public class BoardImage extends JComponent {

	//holds positions of stones on board
	private char[][] imageArray;
	
	//holds position of special group for life/death problems
	private int[] specialGroup;
	

	public BoardImage(){
		this.setSize(800, 800);
	//	this.setOpaque(true);
		
		
		imageArray = new char[19][19] ;
		//Initialise with empty board - set all chars to '-'
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				imageArray[i][j]='-';
			}
		}
		
		specialGroup = new int[2];
		specialGroup[0]=0;
		specialGroup[1]=0;
		
	}
	
	
	public void updateImage(GoBoard board){
		imageArray = board.getArray();
		specialGroup=board.getLifeLocation();
		repaint();
	}
	

	
	public void paintComponent(Graphics g){
		//Color wood = new Color(240,160,70);
		//Color wood = new Color(195,148,89);
		Color wood = new Color(220,148,79);
		
		g.setColor(wood);
//		g.fillRect(20, 20, 760, 760);
		g.fillRect(0, 0, 800, 800);
		g.setColor(Color.BLACK);
		//draw vertical lines
		for(int x=40;x<800;x+=40){
			g.drawLine(x,40,x, 760);
		}
		//draw horizontal lines
		for(int y=40;y<800;y+=40){
			g.drawLine(40,y,760,y);
		}
		
		
		//draw key intersection marks
		for(int x=156;x<650;x+=240){
			for(int y=156;y<650;y+=240){
				g.fillOval(x, y, 8, 8);
			}
		}
		
		//draw column labels from A to T (note: no I - convention is to avoid confusion with J)
		//draw row labels from 1 to 18
		g.setFont(new Font("monospaced",Font.BOLD,16));
		//g.setColor(Color.WHITE);
		String columnLabels = "ABCDEFGHJKLMNOPQRST";
		int xPosition=38;
		int yPosition=765;
		for(int i=0;i<19;i++){
			g.drawString(columnLabels.charAt(i)+"",xPosition,790);
			g.drawString(columnLabels.charAt(i)+"",xPosition,18);
			
			xPosition+=40;
			g.drawString(i+1+"", 3, yPosition);
			g.drawString(i+1+"", 780, yPosition);
			
			yPosition-=40;
		}
		
		for (int row=0;row<19;row++){
			for (int column=0;column<19;column++){
				int x = 40+40*column;  //map column number to an x coordinate
				int y = 40+ 40*(18-row);  //map row number to a y coordinate
				//draw black stones
				if (imageArray[row][column]=='x'){
					g.setColor(Color.BLACK);
					g.fillOval(x-19,y-19, 38, 38);
				}
				//draw white stones
				if (imageArray[row][column]=='o'){
					g.setColor(Color.WHITE);
					g.fillOval(x-19, y-19, 38, 38);
				}
				//draw out of bounds markers
				if (imageArray[row][column]=='*'){
					g.setColor(Color.DARK_GRAY);
					g.drawOval(x-14, y-14, 28, 28);
					g.drawLine(x+10, y-10, x-10, y+10); // +/- rcos45
					
				}
			}
		}
		//DRAW RED DOT TO MARK LIFE/DEATH GROUP
		
		g.setColor(Color.RED);
		//obtain position to paint on image from mapping array
		int row = specialGroup[0];
		int column = specialGroup[1];
		//map game positions to image positions
		int x = 40+40*column;  //map column number to an x coordinate
		int y = 40+ 40*(18-row);  //map row number to a y coordinate
		
		
		
		
		g.fillOval(x-6, y-6, 12, 12);
	
		
		
		
	}



}

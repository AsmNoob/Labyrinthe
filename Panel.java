import java.awt.Color;
import java.awt.Font;	
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.*;
 
public class Panel extends JPanel {
  	private Maze maze_;
  	// private int posX1;
  	// private int posY1;
  	private int posX;
  	private int posY;
  	private int block_size;
  	ArrayList<ArrayList<Integer>> blackList = new ArrayList<ArrayList<Integer>>();
  	private static final long serialVersionUID = 2; // get rid of wanings

	public Panel(Maze maze,int block){
		maze_ = maze;
		//posX; // = maze.getCoordPukkaman()[1]*block_size*2+45;
		//posY; //= maze.getCoordPukkaman()[0]*block_size*2+45;
		block_size = block;
	}


 	public void paintComponent(Graphics graph){
	    graph.setColor(Color.WHITE);
	    graph.fillRect(0, 0, this.getWidth(), this.getHeight());
	    // graph.fillOval(posX1, posY1, 10,10);
	    try{
	    	Image pakkuman = ImageIO.read(new File("pakkuman.png"));
	    	Image ghost = ImageIO.read(new File("ghost.png"));
	    	Image candy = ImageIO.read(new File("candy3.png"));
	    	graph.setColor(Color.BLACK);
	    	// Rond noir qui rprésente pakkuman
	    	if(posX != 0 || posY != 0){
	    		graph.fillOval(posX, posY, block_size/**3/5*/,block_size/**3/5*/);
	    	}
	    	//graph.drawImage(pakkuman, posX, posY,block_size , block_size, this);
		    for(int i = 0; i < maze_.getMaze().length;i++){
		    	for(int j = 0; j < maze_.getMaze()[0].length;j++){
		    		String value = maze_.Output_Analyse(maze_.getMaze()[i][j],i,j);
		    		ArrayList<Integer> coord = new ArrayList<Integer>(2);
		    		coord.add((i-1)/2);coord.add((j-1)/2);
					if(value == "---"){
						graph.drawLine((j-1)*block_size+25,i*block_size+25, (j+1)*block_size+25,i*block_size+25);
		    		}else if (value == "|") {
		    			graph.drawLine(j*block_size+25,(i-1)*block_size+25, j*block_size+25,(i+1)*block_size+25);
		    		}else if (value == " B " && !blackList.contains(coord)){
		    			graph.drawImage(candy, (j-1)*block_size+(25+block_size/2), (i-1)*block_size+(25+block_size/2), block_size , block_size, this);//+37 à la base
		    		}else if (value == " M " && !blackList.contains(coord)) {
		    			graph.drawImage(ghost, (j-1)*block_size+(25+block_size/2), (i-1)*block_size+(25+block_size/2),block_size , block_size, this);
		    		}else if (value == " P ") {
		    			graph.drawImage(pakkuman, (j-1)*block_size+(25+block_size/2), (i-1)*block_size+(25+block_size/2),block_size , block_size, this);
		    		}
		    	}
		    }
		}catch(IOException e){
	    	System.err.println("Caught IOException in Panel: " + e.getMessage());
	    }
	}

	/* public void setPosX1(int pos){
		this.posX1 = pos;
	}

	public void setPosY1(int pos){
	 	this.posY1 = pos;
	}*/


	public ArrayList<ArrayList<Integer>> get_BlackList(){
		return blackList;
	}


	public int getPosX() {
	    return posX;
	}

	public void setPosX(int posX) {
	    this.posX = posX;
	}

	public int getPosY() {
	    return posY;
	}

	public void setPosY(int posY) {
	    this.posY = posY;
	}           
}
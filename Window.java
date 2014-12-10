import javax.swing.JFrame;
import java.awt.Dimension; 
import java.util.*;
 
public class Window extends JFrame{
	private Panel panel;
	private int block_size;

  	public Window(Maze maze){
  	// Fenêtre de base
  		try{
  			block_size = 27 - ((maze.getMaze().length > maze.getMaze()[0].length) ? ((maze.getMaze().length-1)/2)/3*2 : ((maze.getMaze()[0].length-1)/2)/3*2);
	  		if(block_size < 5){
	  			block_size = 5;
	  		}
	  		panel = new Panel(maze,block_size);
	    	this.setTitle("MazeSolver");
	    	this.setSize((maze.getMaze()[0].length-1)*block_size+50,(maze.getMaze().length-1)*block_size+75);
		    this.setLocationRelativeTo(null); //centered
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    this.setResizable(false);
		    this.setContentPane(panel);
		    this.setVisible(true);
		    way(maze);
		   	
		    try {
			  	Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.err.println("Caught InterruptedException in Window(): " + e.getMessage());
			}


		    this.dispose();

		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Window(): " + e.getMessage());
			System.err.println("The maze given is empty!");
		}
  	}

	private void way(Maze maze){
		int x,y;
	    for(int i = 0;i < maze.get_completeWay().size(); i++){

	    	// chaque i => coord
	    	// si je check si coord in MonsterList ou Bonbon List 
	    	// Créer une liste notDraw => sur laquelle on dessinne un bloc en blanc à la place dans panel
	    	ArrayList<Integer> coord = new ArrayList<Integer>(2); //ArrayList<ArrayList<Integer>>
	    	coord.add(maze.get_completeWay().get(i).get(0));coord.add(maze.get_completeWay().get(i).get(1));
	    	//System.out.println("coord[0]: "+coord.get(0)+" coord[1]: "+coord.get(1));
	    	if(maze.getCoordMonsters().contains(coord) || maze.getCoordSweets().contains(coord)){ 
	    		panel.get_BlackList().add(coord);
	    	}

	    	if(i > 0 ){
	    		// TODO Rajouter une condition pour s'occuper de i == 0
	    		int diff_x = (maze.get_completeWay().get(i).get(1)*2*block_size - maze.get_completeWay().get(i-1).get(1)*2*block_size);
	    		int diff_y = (maze.get_completeWay().get(i).get(0)*2*block_size - maze.get_completeWay().get(i-1).get(0)*2*block_size);
	    		//System.out.println("diff_x: "+diff_x+" diff_y: "+diff_y);
	      		for(int j = 0; j < 2*block_size;j++){
	      			x= maze.get_completeWay().get(i-1).get(1)*2*block_size+(25+block_size/2)+(diff_x*j/(2*block_size));// de base 45
	      			y= maze.get_completeWay().get(i-1).get(0)*2*block_size+(25+block_size/2)+(diff_y*j/(2*block_size));
	      			//System.out.println("1: "+diff_x*j/(2*block_size)+" 2: "+diff_y*j/(2*block_size));
	      			/*
	      			if(j >0){
	      				panel.setPosX1(maze.get_completeWay().get(i-1).get(1)*2*block_size+45+(diff_x*(j-1)/(2*block_size)));
	      				panel.setPosY1(maze.get_completeWay().get(i-1).get(0)*2*block_size+45+(diff_y*(j-1)/(2*block_size)));
	      			}else{
	      				panel.setPosX1(maze.get_completeWay().get(i-1).get(1)*2*block_size+45+(diff_x*(0)/(2*block_size)));
	      				panel.setPosY1(maze.get_completeWay().get(i-1).get(0)*2*block_size+45+(diff_y*(0)/(2*block_size)));
	      			}
					*/
	      			panel.setPosX(x);
	      			panel.setPosY(y);
	      			panel.repaint(); 
	      			try {
			      	  	Thread.sleep(15);
			      	} catch (InterruptedException e) {
			     		System.err.println("Caught InterruptedException in Window(): " + e.getMessage());
			     	}
	      		}
	      	}
	    }
	}

}
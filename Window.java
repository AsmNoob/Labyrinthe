import javax.swing.JFrame;
import java.awt.Dimension; 
 
public class Window extends JFrame{
	private Panel panel;
	private int block_size = 25;

  	public Window(Maze maze){
  	// Fenêtre de base
  		panel = new Panel(maze);
    	this.setTitle("MazeSolver");
    	// 10*10 => 150*150 ce qui est enfait un 100*100 avec 25 px autours du laby
    	//System.out.println("i: "+(maze.getMaze().length)+" j: "+(maze.getMaze()[0].length));
    	this.setSize((maze.getMaze()[0].length-1)*block_size+50,(maze.getMaze().length-1)*block_size+75);
	    this.setLocationRelativeTo(null); //centered
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);

	    // Panneau interne à la Fenêtre

	    this.setContentPane(panel);


	    this.setVisible(true);
	    way(maze);
  	}

	private void way(Maze maze){
		int x,y;
	    for(int i = 0;i < maze.get_completeWay().size(); i++){
	    	if(i > 0 ){
	    		int diff_x = (maze.get_completeWay().get(i).get(1)*2*block_size - maze.get_completeWay().get(i-1).get(1)*2*block_size);
	    		int diff_y = (maze.get_completeWay().get(i).get(0)*2*block_size - maze.get_completeWay().get(i-1).get(0)*2*block_size);
	    		//System.out.println("diff_x: "+diff_x+" diff_y: "+diff_y);
	      		for(int j = 0; j < 2*block_size;j++){
	      			x= maze.get_completeWay().get(i-1).get(1)*2*block_size+45+(diff_x*j/(2*block_size));
	      			y= maze.get_completeWay().get(i-1).get(0)*2*block_size+45+(diff_y*j/(2*block_size));
	      			//System.out.println("1: "+diff_x*j/(2*block_size)+" 2: "+diff_y*j/(2*block_size));
	      			if(j >0){
	      				panel.setPosX1(maze.get_completeWay().get(i-1).get(1)*2*block_size+45+(diff_x*(j-1)/(2*block_size)));
	      				panel.setPosY1(maze.get_completeWay().get(i-1).get(0)*2*block_size+45+(diff_y*(j-1)/(2*block_size)));
	      			}else{
	      				panel.setPosX1(maze.get_completeWay().get(i-1).get(1)*2*block_size+45+(diff_x*(0)/(2*block_size)));
	      				panel.setPosY1(maze.get_completeWay().get(i-1).get(0)*2*block_size+45+(diff_y*(0)/(2*block_size)));
	      			}
	      			panel.setPosX(x);
	      			panel.setPosY(y);
	      			panel.repaint(); 
	      			try {
			      	  	Thread.sleep(5);
			      	} catch (InterruptedException e) {
			     		System.err.println("Caught InterruptedException in Window(): " + e.getMessage());
			     	}
	      		}
	      	}
	      	//x = maze.get_completeWay().get(i).get(1)*2*block_size+45;
	      	//y = maze.get_completeWay().get(i).get(0)*2*block_size+45;
	      	//panel.setPosX(x);
	      	//panel.setPosY(y);
	      	//panel.repaint();  
	      	/*try {
	      	  	Thread.sleep();
	      	} catch (InterruptedException e) {
	     		System.err.println("Caught InterruptedException in Window(): " + e.getMessage());
	     	}*/
	    }
	}

}
public class Main{
	public static void main(String[] args) {
		//Main
		try{
			// System.out.println("Controle_MainStart");
			// System.out.println();
			// System.out.println("Controle_MazeStart");
			// System.out.println();
			long begin = System.currentTimeMillis();
			Maze maze = new Maze(args[0]);

	    	long step1 = System.currentTimeMillis();
	  		// System.out.println();
			// System.out.println("Controle_MazeEnd");
			// System.out.println();
			// System.out.println("Controle_GraphStart");
			// System.out.println();
			Graph graph = new Graph(maze.getMaze(),maze.getCoordPukkaman());
	    	long step2 = System.currentTimeMillis();
			// System.out.println();
			// System.out.println("Controle_GraphEnd");
			// System.out.println();
			// System.out.println("Controle_DijkstraStart");
			// System.out.println();
			Dijkstra dijkstra = new Dijkstra(graph.get_listNode());
	    	long step3 = System.currentTimeMillis();
			// System.out.println();
			// System.out.println("Controle_DijkstraEnd");
			// System.out.println();
			// System.out.println("Controle_SituationFinaleStart");
			// System.out.println();
			maze.Final_Situation(dijkstra.get_finalWay(),graph,dijkstra.get_totalWeight());
			long step4 = System.currentTimeMillis();
			// System.out.println();
			// System.out.println("Controle_SituationFinaleEnd");
			// System.out.println();
			// System.out.println("Controle_WindowStart");
			// System.out.println();
			Window window = new Window(maze);
			// System.out.println();			
			// System.out.println("Controle_WindowEnd");
			// System.out.println();
			// System.out.println("Controle_EndMain");
			// System.out.println();
			float time1 = ((float) (step1-begin)) / 1000f;
			float time2 = ((float) (step2-step1)) / 1000f;
			float time3 = ((float) (step3-step2)) / 1000f;
			float time4 = ((float) (step4-step3)) / 1000f;
			System.out.print("Time exe || Maze : ");
			System.out.print(time1);
			System.out.print(" || Graph : ");
			System.out.print(time2);
			System.out.print(" || Dijkstra : ");
			System.out.print(time3);
			System.out.print(" || Final_Situation : ");
			System.out.print(time4);
			System.out.print("Total Time execution: ");	
			System.out.println(time1+time2+time3+time4);
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Caught ArrayIndexOutOfBoundsException in Main: " + e.getMessage());
			System.out.println("InputError: No maze given in input('java Main labyXX.txt').");
		}
	}
}
public class Main{
	public static void main(String[] args) {
		//Main
		System.out.println("Controle_Start");
		System.out.println(args[0]);

		Maze maze = new Maze(args[0]);
		System.out.println("Controle_EndMaze");

		int i =0; int j=0;
		Graph graph = new Graph(maze.getMaze(),i,j); 
	}
}
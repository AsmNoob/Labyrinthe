public class Main{
	public static void main(String[] args) {
		//Main
		System.out.println("Controle_Start");
		System.out.println(args[0]);

		Maze maze = new Maze(args[0]);
		System.out.println("Controle_EndMaze");

		Graph graph = new Graph(maze.getMaze(),maze.getCoordPacman()); 
	}
}
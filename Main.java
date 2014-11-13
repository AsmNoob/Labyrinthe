public class Main{
	public static void main(String[] args) {
		//Main
		System.out.println("Controle_MainStart");
		System.out.println();
		System.out.println(args[0]);
		System.out.println();
		System.out.println("Controle_MazeStart");
		System.out.println();
		Maze maze = new Maze(args[0]);
		System.out.println()
		System.out.println("Controle_GraphStart");
		System.out.println();
		Graph graph = new Graph(maze.getMaze(),maze.getCoordPukkaman());
		System.out.println();
		System.out.println("Controle_GraphEnd");
		System.out.println();
		System.out.println("Controle_EndMain);
	}
}
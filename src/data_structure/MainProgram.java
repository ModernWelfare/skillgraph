package data_structure;

public class MainProgram {
	public MainProgram() {
	}

	public static void main(String args[]) {
		if (args.length < 4) {
			System.out.println("Enter the following input files");
			System.out.println("A skill structure file");
			System.out.println("An item to skill mapping file");
			System.out.println("A CPT range file");
			System.out.println("A Guess And Slip Range file");
		} else {
			SkillGraph skillGraph = new SkillGraph(args[0], args[1], args[2],
					args[3]);
			System.out.println(skillGraph);
		}
	}
}

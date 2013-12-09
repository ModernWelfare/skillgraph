package data_structure;

import java.io.IOException;

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
			skillGraph.mergeSkills(2, 3);
			System.out.println(skillGraph);
		}

		// code to execute the matlab command

		try {
			// run the matlab command on a new process
			Process p = Runtime
					.getRuntime()
					.exec("matlab -nodisplay -wait -nosplash -nodesktop -r run('script')");
			System.out.println("matlab command entered");
			p.waitFor();
			// do something when the command has been executed
			System.out.println("matlab command completed");
		} catch (InterruptedException | IOException ex) {
			// print stack trace and exit
			ex.printStackTrace();
			System.exit(-1);
		}
	}
}

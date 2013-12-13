package data_structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.FileReader;
import util.MatlabFileWriter;
import util.RandomGraphGenerator;

public class MainProgram {
	// list of strings to hold the results from matlab evaluations
	static List<String> matlabResults;

	public static void main(String args[]) {
		int iterationNumber = 1;
		// initialize the evaluation result list
		matlabResults = new ArrayList<String>();

		// generates the random graph
		int[] a = { 2, 4, 1, 2, 1, 3 };
		RandomGraphGenerator.generateRandomGraph(a, "test_graph");

		SkillGraph graph = new SkillGraph("test_graph/SkillGraph.csv",
				"test_graph/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
				"test_graph/GuessAndSlipRanges.csv");

		MatlabFileWriter.outPutSkillGraphMatlabFile(graph, iterationNumber,
				"sampleDag.m");

		// if (args.length < 4) {
		// System.out.println("Enter the following input files");
		// System.out.println("A skill structure file");
		// System.out.println("An item to skill mapping file");
		// System.out.println("A CPT range file");
		// System.out.println("A Guess And Slip Range file");
		// } else {
		// SkillGraph skillGraph = new SkillGraph(args[0], args[1], args[2],
		// args[3]);
		// System.out.println(skillGraph);
		// skillGraph.mergeSkills(2, 3);
		// System.out.println(skillGraph);
		// }

		// code to execute the matlab command

		try {
			// run the matlab command on a new process
			Process p = Runtime.getRuntime().exec(
					"matlab -nodisplay -wait -nosplash -nodesktop -r \"cd bnt; "
							+ "addpath(genpathKPM(pwd)); "
							+ "cd ../matlab_scripts; "
							+ "run CreateDLMObject; " + "run Evaluation1; "
							+ "exit;\"");
			System.out.println("matlab command entered");
			p.waitFor();
			// do something when the command has been execute d
			System.out.println("matlab command completed");

			matlabResults.add(FileReader.readCSVFile("results/results.txt")
					.get(0));

			for (String s : matlabResults) {
				System.out.println(s);
			}

		} catch (InterruptedException | IOException ex) {
			// print stack trace and exit
			ex.printStackTrace();
			System.exit(-1);
		}
	}
}

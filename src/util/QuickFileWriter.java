
package util;

import java.io.File;
import java.io.FileWriter;

public class QuickFileWriter
{
	private QuickFileWriter()
	{
	}

	public static void createFolder(String folderName)
	{
		File dir = new File(folderName);

		if(!dir.exists())
		{
			dir.mkdir();
		}
	}

	public static void createFolders(String folderName)
	{
		File dir = new File(folderName);

		if(!dir.exists())
		{
			dir.mkdirs();
		}
	}

	/**
	 * Writes the output to a file
	 * 
	 * @param filePath
	 *            the path of the file to write to
	 * @param outputString
	 *            the output string to be written to the file
	 */
	public static void writeFile(String filePath, String outputString)
	{
		try
		{
			FileWriter file = new FileWriter(filePath, false);

			file.write(outputString);
			file.flush();
			file.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
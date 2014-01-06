
package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
 

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
	public static void writeFile(String filePath, String outputString, boolean append)
	{
		try
		{
			FileWriter file = new FileWriter(filePath, append);

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

	public static void copyFile(String source, String destination)
	{
		try
		{
			Path copy = Paths.get(source);
			Path paste = Paths.get(destination);
			
			Files.copy(copy, paste, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}	
	}
}


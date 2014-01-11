package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
/*
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
*/
import java.util.ArrayList;
import java.util.List;

/**
 * Class for implementing all operations involving file i/o
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class QuickFileReader
{

	//private final static Charset ENCODING = StandardCharsets.UTF_8;

	/**
	 * Read a file to a list of strings with each string holding a complete
	 * line
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	/*
	public static List<String> readFile(String filePath)
	{
		List<String> returnList = new ArrayList<String>();

		try
		{
			Path path = Paths.get(filePath);
			returnList = Files.readAllLines(path, ENCODING);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		return returnList;
	}
	*/

	public static List<String> readFile(String filePath)
	{
		List<String> returnList = new ArrayList<String>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = "";

			while((line = br.readLine()) != null)
			{
				returnList.add(line);
			}

			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		return returnList;
	}
}

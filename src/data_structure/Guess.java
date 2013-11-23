package data_structure;

/**
 * Class to implement the node for the guess parameter inside the skill graph
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class Guess extends Node implements Comparable<Guess>
{
	private Item item;

	public Guess(int index, String name)
	{
		super(index, name);
	}

	public Guess(Guess otherGuess)
	{
		super(otherGuess.getIndex(), otherGuess.getName());
		//item = new Item(otherGuess.getItem());
		cptTable = new CPT(otherGuess.getCPTTable());
	}

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item im)
	{
		item = im;
	}

	public void setItemAndItemGuess(Item im)
	{
		item = im;
		item.setGuess(this);
	}

	//output the cpt table in an easily readable form
	//assumes no commas or other annoying characters in the name
	public String getStringCPTTable()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(getIndex() + ": " + getName() + " = did not guess");
		sb.append(",");
		sb.append(getIndex() + ": " + getName() + " = guessed");
		
		sb.append("\n");

		int cptTableSize = cptTable.getSize();

		for(int i=0; i<cptTableSize; i++)
		{
			String binaryString = Integer.toBinaryString(i);

			for(int j=0; j<2; j++)
			{
				sb.append(cptTable.getProbability(binaryString, j));
				sb.append(",");				
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}

		sb.deleteCharAt(sb.length()-1);

		return sb.toString();
	}

	@Override
	public boolean equals(Object otherGuess)
	{
		boolean returnValue = false;

		if(otherGuess == null)
		{
			returnValue = false;
		}
		else if(this == otherGuess)
		{
			returnValue = true;
		}
		else if(!(otherGuess instanceof Guess))
		{
			// probably an error

			returnValue = false;
		}
		else
		{
			Guess otherGuess1 = (Guess) otherGuess;

			if(getName().equals(otherGuess1.getName()) && getIndex() == otherGuess1.getIndex())
			{
				if(cptTable.equals(otherGuess1.getCPTTable()))
				{
					returnValue = true;
				}
			}
		}

		return returnValue;
	}

	@Override
	public int compareTo(Guess otherGuess)
	{
		// die if null
		if(otherGuess == null)
		{
			System.err.println("Trying to compare to a null guess node");
			System.exit(-1);
		}

		return (getIndex() - otherGuess.getIndex());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Guess index = " + getIndex());
		sb.append("\n");
		sb.append("Guess name = " + getName());
		sb.append("\n");
		sb.append("Item name = " + item.getName());
		sb.append("\n");
		sb.append("Guess CPT Table = ");
		sb.append("\n");
		sb.append(getCPTTable());
		sb.append("\n");

		return sb.toString();
	}
}

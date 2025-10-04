import data.Data;
import exceptions.ClusteringRadiusException;
import exceptions.EmptyDatasetException;
import keyboardinput.Keyboard;
import mining.QTMiner;

public class MainTest {

	/**
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) 
	{
		Data data;
				
		boolean running = true;
		char answer;
		
		try
		{
			data = new Data();
			System.out.println(data);

			while(running)
			{
				System.out.println("Insert radius (>0): ");
				QTMiner qt=new QTMiner(Keyboard.readDouble());
				
				try 
				{
					int numIter = qt.compute(data);
					if(numIter > 1)
					System.out.println("Number of clusters: " + numIter);
					
					System.out.println(qt.getC().toString(data));
				}
				catch(ClusteringRadiusException e)
				{
					System.err.println(e.getMessage());
					continue;
				}
				
				do
				{
					System.out.println("New execution?(y/n): ");
					answer = Keyboard.readChar();
				} while(answer != 'y' && answer != 'n');
				
				if(answer == 'n') running = false;
			}
		} 
		catch (EmptyDatasetException e)
		{
			System.err.println(e.getMessage());
		}
	}
}
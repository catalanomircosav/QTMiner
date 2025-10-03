import data.Data;
import keyboardinput.Keyboard;
import mining.QTMiner;

public class MainTest {

	/**
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		Data data = new Data();
		
		boolean running = true;
		char answer;

		System.out.println(data);
		while(running)
		{
			System.out.println("Insert radius (>0): ");
			QTMiner qt=new QTMiner(Keyboard.readDouble());

			int numIter = qt.compute(data);
			if(numIter > 1)
				System.out.println("Number of clusters: " + numIter);
				
			System.out.println(qt.getC().toString(data));

			do
			{
				System.out.println("New execution?(y/n): ");
				answer = Keyboard.readChar();
			} while(answer != 'y' && answer != 'n');

			if(answer == 'n') running = false;
		}
	}
}
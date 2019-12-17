import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class risingCity {

	private static final String OUTPUT = "output_file.txt";
	private RedBlackTree rootRBT = null;
	private minHeap minHeapBld = null;
	Set<Integer> insertedBldNums = new HashSet<Integer>();
	Building currBld = null;
	
	// constructor to initialize redblack tree with empty node and minHeap of size 2000
	public risingCity() {
		this.rootRBT = new RedBlackTree();
		this.minHeapBld = new minHeap(2000);
	}
	
	public static void main(String... args) {
		// create rising city object
		risingCity rC = new risingCity();
		// start the construction of building
		rC.simulate(args[0]);
	}
	// start the construction of building
	public void simulate(String input) {
		File output_file = new File(OUTPUT);
		File input_file = new File(input);
		//validate the input output file 
		checkInputOutput(input_file, output_file);
		//start construction on input file
		simulateConstruction(input_file, output_file);
	}
	
	//simulate the construction 
	private void simulateConstruction(File input_file, File output_file) {

		try {
			
			FileWriter fw = new FileWriter(output_file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader fileReader = new BufferedReader(new FileReader(input_file));
			int globalDayCount = 0;
			String readLine = fileReader.readLine();
			
			// infinite loop until file is read completely
			while (true) {
				// read line from file is end of file is reached break
				if (readLine == null) {
					readLine = fileReader.readLine();
					if (readLine == null)
						break;
				}
				// execute building from day 1 on the current min building 
				if (globalDayCount != 0)
					executeBuilding(bw, globalDayCount);

				String[] parts = readLine.split(":");
				
				if (parts.length >= 2) {
					int currentDayCount = 0;
					currentDayCount = Integer.parseInt(parts[0]);
					// current day is equal to global day count action is performed
					if (currentDayCount == globalDayCount) {
						String errorMsg = null;
						String[] subparts = parts[1].trim().split("\\(");
						switch (subparts[0].trim()) {
						// printbuilding action 
						case "PrintBuilding":

							errorMsg = printBuilding(subparts[1], bw);
							break;
						// insert action 
						case "Insert":
							errorMsg = insertBuilding(subparts[1]);
							break;

						default:
							errorMsg = "Invalid Command = " + subparts[0].trim();
						}
						// error handling for each action operation 
						if (errorMsg != null) {
							bw.write("Invalid Line = " + readLine + " " + errorMsg + "\n");
							bw.close();
							fileReader.close();
							return;
						}
						readLine = null;
					}

				} else {
					bw.write("Invalid Command = " + readLine + "\n");
					bw.close();
					fileReader.close();
					return;
				}
				// increment globalday
				globalDayCount++;
			}
			// execute remaining buidings in minheap after reading entire file
			executeRemainingBuildings(bw, globalDayCount);
			fileReader.close();
			bw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	// function to validate the input and the output files
	private void checkInputOutput(File input_file, File output_file) {
		try {
			if (output_file.exists())
				output_file.delete();

			output_file.createNewFile();
			FileWriter fw = new FileWriter(output_file);
			BufferedWriter bw = new BufferedWriter(fw);

			if (input_file == null) {
				bw.write("Invalid File" + "\n");
				bw.close();
				return;
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			System.out.println("Failed to create OutputFile" + "\n");
			e.printStackTrace();
		}
	}
	
	// execute function to construct building by taking the first building from minHeap
	private void executeBuilding(BufferedWriter bw, int globalDayCount) throws IOException {

		if (currBld == null) {
			currBld = minHeapBld.removeMin();
			if (currBld == null) {
				return;
			}
		}
		// increase executed time for each day
		currBld.setExecuted_time(currBld.getExecuted_time() + 1);
		currBld.workingDays++;
		// when executed time is equal total time do the printing in file 
		// and delete the node of that building from redblack tree
		// and print to the output file
		if (currBld.getExecuted_time() == currBld.getTotal_time()) {

			bw.write("(" + currBld.getBuildingNum() + "," + (globalDayCount) + ")" + "\n");
			// remove current building from redblack tree and make current building null
			rootRBT.removeBuilding(currBld);
			currBld = null;
			return;
		}
		//  current building consecutive working till 5 days and 
		//  reinsert into min heap operation
		if (currBld.workingDays == 5) {
			currBld.workingDays = 0;
			minHeapBld.insert(currBld);
			currBld = null;
		}

		return;
	}
	// execute remaining buildings after doing all actions from file
	private String executeRemainingBuildings(BufferedWriter bw, int globalDayCount) throws IOException {
		// do the construction till the minheap is empty
		while (minHeapBld.getSize() != -1 || currBld != null) {
			executeBuilding(bw, globalDayCount++);
		}
		return null;
	}
	// function to insert building into minheap and redblack tree
	private String insertBuilding(String str) {

		String parts[] = str.split(",");
		if (parts.length != 2) {
			return "Building Number or Total Time is missing";
		}
		parts[1] = parts[1].substring(0, parts[1].length() - 1);
		
		int buildingNum = Integer.valueOf(parts[0].trim());
		// checking if the buidling number is duplicate 
		if (insertedBldNums.contains(buildingNum)) {
			return "Building Number is Duplicate";
		}
		insertedBldNums.add(buildingNum);
		int totalTime = Integer.parseInt(parts[1].trim());
		Building building = new Building(buildingNum, 0, totalTime);
		// insert building into minHeap and redblack tree
		minHeapBld.insert(building);
		rootRBT.insert(building);
		return null;
	}

	// function to print the building actions from redblack tree
	private String printBuilding(String str, BufferedWriter bw) throws IOException {

		String[] parts = str.split(",");
		bw.flush();
		// print the building from redblack tree based on building num
		if (parts.length == 1) {
			parts[0] = parts[0].trim();
			int buildingToPrint = Integer.parseInt(parts[0].substring(0, parts[0].length() - 1));

			String output = rootRBT.printBuilding(buildingToPrint);

			bw.write(output + "\n");
		} // print the buildings within the given range 
		else if (parts.length == 2) {

			parts[1] = parts[1].trim();
			int range1 = Integer.parseInt(parts[0].trim());
			int range2 = Integer.parseInt(parts[1].substring(0, parts[1].length() - 1));

			String output = rootRBT.printBuilding(range1, range2);

			bw.write(output + "\n");
		}
		return null;

	}

}

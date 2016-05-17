import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This is to parse the txt file to retrieve the values of duct0,1,2,3, and fill
 * the values to a txt file according to the x, y position.
 * 
 * @author guanliu
 * 
 */

public class DutXYValues{
   
	static class Dut implements Comparable<Dut>{
		public int id = -1;
		public int x = -1;
		public int y = -1;
		public ArrayList<String> values = new ArrayList<String>();
		
		@Override
		public int compareTo(Dut dut) {
			if(dut == null){
				return 1;
			}else if(this == dut){
				return 0;
			}else{
				return this.y == dut.y ? (this.x - dut.x) : (this.y - dut.y);		
			}
		}		
		
		public boolean validate(){
			return id >= 0 && x >= 0 && y>=0;
		}
		
		public String toString() {
	        return x + ":" + y + ":" + id;
	    }
	}
	
	//private static List<Dut> dutList = new LinkedList<Dut>();
	
	public static void main(String[] args) {
		
		String pathName = null; ////"C:\\YQY\\workspace\\YQYTools\\samples\\A664453.19_#19,20_CP2.dlg";
		if(args.length ==1){
			pathName = args[0];
		}else{
			System.out.println("Usage: YQYTools.jar fileTobeProcessed");
		}
		if(pathName != null){
			System.out.println("processing file: " + pathName);
			process(new File(pathName));
		}
	}

	
	//if path is a director, process all the files in it
    //otherwise, process the file
    public static void process(File file){        
        if(file.exists()){
            if(file.isDirectory()){
                //list the file and process
                for(File f : file.listFiles()){
                    processFile(f);
                }
            }else{
                processFile(file);
            }                
        }else{
            System.out.println("ERROR - file does not exist, path: " + file.getAbsolutePath());
        }            
    }

    //this is just processing a file; will skip directories
    private static void processFile(File file) {
    	
    	if(file.isDirectory()){
    		return;
    	}
    	
        String path = file.getAbsolutePath();
        
        if(!path.endsWith(".dlg")){
        	return;
        }
        
        System.out.println("INFO - processing file: " + path);
       
        BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			StringBuilder sb = new StringBuilder();
			String line = "";
			
			List<Dut> dutList = new LinkedList<Dut>();

			while (true) {
				
				line = br.readLine();
				
				if(line == null){
					parseUnit(dutList, sb.toString());
					break;
				}else{
					if(line.contains("Program:")){ //new unit
						parseUnit(dutList, sb.toString());
						sb.setLength(0); //clean the string builder
					}
					sb.append(line);
					sb.append("\n");
				}												
			}
			
			//testing...
			//validateDuts(dutList, path);
			
			processResult1(dutList, path);
			processResult2(dutList, path);

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    /**
     * sort the duts and output the duts x/y/value to the file
     */
    private static void processResult2(List<Dut> dutList, String originFilename){
    	
    	Collections.sort(dutList);
    	
    	StringBuilder sb = new StringBuilder();
    	
    	//print x positions
    	int max_x = -1;    	
    	for(Dut dut : dutList){
    		if(max_x < dut.x){
    			max_x = dut.x;    			
    		}
    	}    	
    	
    	sb.append("\t");
    	for(int i = 0; i < max_x; i++){
    		sb.append(i).append("\t");
    	}
    	sb.append("\n");
    	//----------------------------------
    	
    	int y = dutList.get(0).y - 1; int x = 0;
    	
    	for(Dut dut : dutList){
    		
    		//fill the rows as placeholder if needed
    		while(y < dut.y){
    			sb.append("\n").append(++y);
    			x = 0;
    		}
    		
    		if(y == dut.y){
    			//print the row from 0 to max of x
    			while(x < dut.x){
    				sb.append("\t");
    				x++;
    			}   
    			if(x == dut.x){
    				String v_2nd = dut.values.get(1);
        			sb.append("\t");
        			sb.append(v_2nd.substring(v_2nd.length()-2, v_2nd.length()));
        			x++;
    			}else{
    				System.out.println("Error - this should not happen: x > dut.x | " + x + "-" + y + "-" + dut);
    				System.exit(1);
    			}
    			
     		}else{
     			System.out.println("Error - this should not happen: y > dut.y");
     			System.exit(1);
     		}    		
    	}
    	
    	writeToFile(sb.toString(), originFilename + ".result2");
    }

    
    /**
     * sort the duts and output the duts x/y/value to the file
     */
    private static void processResult1(List<Dut> dutList, String originFilename){
    	
    	Collections.sort(dutList);
    	
    	StringBuilder sb = new StringBuilder();
    	
    	//print x positions
    	int max_x = -1;    	
    	for(Dut dut : dutList){
    		if(max_x < dut.x){
    			max_x = dut.x;    			
    		}
    	}    	
    	
    	sb.append("\t");
    	for(int i = 0; i < max_x; i++){
    		sb.append(i).append("\t");
    	}
    	sb.append("\n");
    	//----------------------------------
    	
    	int y = dutList.get(0).y - 1; int x = 0;
    	
    	for(Dut dut : dutList){
    		
    		//fill the rows as placeholder if needed
    		while(y < dut.y){
    			sb.append("\n").append(++y);
    			x = 0;
    		}
    		
    		if(y == dut.y){
    			//print the row from 0 to max of x
    			while(x < dut.x){
    				sb.append("\t");
    				x++;
    			}   
    			if(x == dut.x){
    				String v_1st = dut.values.get(0);
        			sb.append("\t");
        			sb.append(v_1st.substring(0, 2));
        			x++;
    			}else{
    				System.out.println("Error - this should not happen: x > dut.x | " + x + "-" + y + "-" + dut);
    				System.exit(1);
    			}
    			
     		}else{
     			System.out.println("Error - this should not happen: y > dut.y");
     			System.exit(1);
     		}    		
    	}
    	
    	writeToFile(sb.toString(), originFilename + ".result1");
    }

	
	private static void parseUnit(List<Dut> dutList, String str){
		
		Map<Integer, Dut> unitDuts = new HashMap<Integer, Dut>();
		
        String dutVPattern = "DUT\\[(\\d)\\] Die Tracking Info Table.*?Rd_info_nom (\\w+).*?Rd_info_nom (\\w+)";
        String dutXYPattern = "DUT (\\d) Result=PASS.*?Die: Row\\(Y\\)= (\\d+) Column\\(X\\)= (\\d+)"; //.*? means least matching
        
        Pattern p1 = Pattern.compile(dutVPattern,  Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m1 = p1.matcher(str);
        
        while(m1.find()){
        	Dut dut = new Dut();
        	dut.id = Integer.parseInt(m1.group(1));
        	dut.values.add(m1.group(2));
        	dut.values.add(m1.group(3));
        	unitDuts.put(dut.id, dut);
        }
        
        if(unitDuts.isEmpty()){
        	System.out.println("INFO - No unit for the current string, this could be normal");
        	return;
        }
        
        Pattern p2 = Pattern.compile(dutXYPattern,  Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m2 = p2.matcher(str);
        
        while(m2.find()){
        	int id = Integer.parseInt(m2.group(1));
        	if(!unitDuts.containsKey(id)){
        		System.out.println("Error. An expected dut is not there - " + m2.group());
        		System.exit(1);
        	}
        	
        	Dut dut = unitDuts.get(id);
        	dut.y = Integer.parseInt(m2.group(2));
        	dut.x = Integer.parseInt(m2.group(3));
        }
        
        for(Dut dut : unitDuts.values()){
        	if(dut.validate()){
        		dutList.add(dut);
        	}else{
        		System.out.println("WARNING - invalid dut: " + dut);
        	}
        }
	}

	private static void validateDuts(List<Dut> dutList, String validateResult){
   	
    	Collections.sort(dutList);

    	StringBuilder sb = new StringBuilder();
    	
    	for(Dut dut : dutList){
    		sb.append(dut).append("\n");
    	}    	
    	
    	
    	//store the result to the output file
    	BufferedWriter output = null;
		try {
			String result = validateResult + ".duts";
			
			File newFile = new File(result);
			newFile.createNewFile();
			output = new BufferedWriter(new FileWriter(newFile));
			output.write(sb.toString());

			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(output != null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void writeToFile(String content, String filename){

    	//store the result to the output file
    	BufferedWriter output = null;
		try {		
			File newFile = new File(filename);
			newFile.createNewFile();
			output = new BufferedWriter(new FileWriter(newFile));
			output.write(content);

			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(output != null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}


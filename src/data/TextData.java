package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Data pre-processing
 * @author LittleYUYU
 *
 */
public class TextData {

	String dataPath;
	ArrayList<TextSen> sens;
	Map<String, Integer> countClass;
	Map<String, Map<String, Integer>> countWordClass;
	
	public TextData(){
		this.sens = new ArrayList<TextSen>();
		this.countClass = new HashMap<String, Integer>();
		this.countWordClass = new HashMap<String, Map<String, Integer>>();
	}
	
	public TextData(String dataPath){
		this.dataPath = dataPath;
		this.sens = new ArrayList<TextSen>();
		this.countClass = new HashMap<String, Integer>();
		this.countWordClass = new HashMap<String, Map<String, Integer>>();
		dataProcs();
	}
	
	protected void dataProcs(){
		readSens();
		countClassFreq();
		countWordClass();
	}
	
	protected void countWordClass() {
		// TODO Auto-generated method stub
		for(TextSen sen : sens){
			String label = sen.label;
			if(!countWordClass.containsKey(label)){
				Map<String, Integer> map = new HashMap<String, Integer>();
				countWordClass.put(label, map);
			}
			for(Entry<String, Integer> en : sen.wordTable.entrySet()){
				if(countWordClass.get(label).containsKey(en.getKey())){
					countWordClass.get(label).put(en.getKey(), 
							countWordClass.get(label).get(en.getKey()) + en.getValue());
				}
				else{
					countWordClass.get(label).put(en.getKey(), en.getValue());
				}
			}
		}
	}

	protected void countClassFreq() {
		// TODO Auto-generated method stub
		for(TextSen sen : sens){
			String label = sen.label;
			if(countClass.containsKey(label))
				countClass.put(label, countClass.get(sen.label) + 1);
			else
				countClass.put(label, 1);
		}
	}

	protected void readSens() {
		// TODO Auto-generated method stub
		try{
			BufferedReader br = new BufferedReader(new FileReader(dataPath));
			String sen = null;
			while((sen = br.readLine()) != null){
				TextSen txtSen = new TextSen(sen.split(",")[0], sen.split(",")[1]);
				sens.add(txtSen);
			}
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public Map<String, Integer> getClassCount(){
		return this.countClass;
	}
	
	public Map<String, Map<String, Integer>> getWordClassCount(){
		return this.countWordClass;
	}
	
	public void printWordClassCount(String path){
		try{
			FileWriter fw = new FileWriter(path);
			for(Entry<String, Map<String, Integer>> en : countWordClass.entrySet()){
				fw.write(en.getKey() + "\r\n");
				for(Entry<String, Integer> en2 : en.getValue().entrySet()){
					fw.write(en2.getKey() + "\t" + en2.getValue() + "\r\n");
				}
			}
			fw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public int getSenSize(){
		return sens.size();
	}
	
	public ArrayList<TextSen> getSens(){
		return sens;
	}
	
	public void printSens(String path){
		try{
			FileWriter fw = new FileWriter(path);
			for(TextSen sen : sens){
				fw.write(sen.label + ",");
				for(Entry<String, Integer> en : sen.wordTable.entrySet()){
					fw.write(en.getKey() + " " + en.getValue() + " ");
				}
				fw.write("\r\n");
			}
			fw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}



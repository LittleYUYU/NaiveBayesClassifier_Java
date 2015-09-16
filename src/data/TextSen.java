package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Modeling sentences in text.
 * Attributes for each sentence: class label, sentence content, and word table.
 * @author LittleYUYU
 *
 */
public class TextSen {

	String label;
	String sen;
	Map<String, Integer> wordTable;
	
	public TextSen(String label, Map<String, Integer> wordTable){
		this.label = label;
		this.wordTable = wordTable;
	}
	
	public TextSen(String label, String sen){
		this.label = label;
		this.sen = sen;
		wordTable = new HashMap<String, Integer>();
		computeWordTbl();
	}

	private void computeWordTbl() {
		// TODO Auto-generated method stub
		String[] words = sen.split(" ");
		for(String word : words){
			if(wordTable.containsKey(word))
				wordTable.put(word, wordTable.get(word) + 1);
			else
				wordTable.put(word, 1);
		}
	}
	
	public String getLabel(){
		return label;
	}
	
//	public String getSen(){
//		return sen;
//	}
	
	public Map<String, Integer> getWordTable(){
		return wordTable;
	}
}

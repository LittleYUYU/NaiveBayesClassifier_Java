package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Process 20Newsgroup data.
 * Data source: http://qwone.com/~jason/20Newsgroups/
 * Class NgData is used to process 20news-bydate.tar.gz.
 * @author LittleYUYU
 *
 */
public class NgData extends TextData{

	String dataWdTblePath; //e.g. train.data in 20news-bydate
	String dataLabelPath;  //e.g. train.label in 20news-bydate
	
	public NgData(String dataWdTblPath, String dataLabelPath){
		super();
		this.dataWdTblePath = dataWdTblPath;
		this.dataLabelPath = dataLabelPath;
		dataProcs();
	}
	
	@Override
	protected void readSens(){
		try{
			ArrayList<String> label = readLabel();
			BufferedReader br = new BufferedReader(new FileReader(dataWdTblePath));
			int idxSen = 0;
			String sen = null;
			Map<String, Integer> mapOfWord = null;
			while((sen = br.readLine()) != null){
				String[] senVal = sen.split(" "); 
				if(Integer.parseInt(senVal[0]) == idxSen + 1){
					if(mapOfWord != null){
						TextSen txtsen = new TextSen(label.get(idxSen), mapOfWord);
						sens.add(txtsen);
					}
					mapOfWord = new HashMap<String, Integer>();
					mapOfWord.put(senVal[1], Integer.parseInt(senVal[2]));
					idxSen ++;
				}else if(Integer.parseInt(senVal[0]) == idxSen){
					mapOfWord.put(senVal[1], Integer.parseInt(senVal[2]));
				}
			}
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	private ArrayList<String> readLabel() {
		// TODO Auto-generated method stub
		ArrayList<String> label = new ArrayList<String>();
		try{
			String sen = null;
			BufferedReader br = new BufferedReader(new FileReader(dataLabelPath));
			while((sen = br.readLine()) != null){
				label.add(sen);
			}
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return label;
	}
}

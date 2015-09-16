package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import data.TextData;
import data.TextSen;

/**
 * Naive Bayes Classifier
 * @author LittleYUYU
 *
 */
public class MNB {

	TextData dataTrain; //training data
	TextData dataTest; //testing data
	Map<String, Double> paraClass; //P(class)
	Map<String, Map<String, Double>> paraWordClass; //P(word|class)
	
	double[][] posterior; //result for testing data
	
	public Map<Integer, String> classIndxInverse; //helper structure
	public Map<String, Integer> classIndx;
	public int classNum;
	
	public MNB(TextData dataTrain, TextData dataTest){
		this.dataTrain = dataTrain;
		this.dataTest = dataTest;
		paraClass = new HashMap<String, Double>();
		paraWordClass = new HashMap<String, Map<String, Double>>();
		classIndxInverse = new HashMap<Integer, String>();
		classIndx = new HashMap<String, Integer>();
		classNum = 0;
	}
	
	/**
	 * Get parameter: P(class)
	 */
	private void getParaClass(){
		int sum = 0;
		for(Entry<String, Integer> en : dataTrain.getClassCount().entrySet()){
			sum += en.getValue();
		}
		int idx = 0;
		for(Entry<String, Integer> en : dataTrain.getClassCount().entrySet()){
			paraClass.put(en.getKey(), en.getValue()*1.0 / sum);
			classIndxInverse.put(idx, en.getKey());
			classIndx.put(en.getKey(), idx);
			idx++;
		}
		classNum = classIndx.size();
	}
	
	/**
	 * Get parameter: P(word|class).
	 * We perform Laplace Smooth here.
	 */
	private void getParaWordClass(){
		Set<String> vocab = new HashSet<String>();
		for(Entry<String, Map<String, Integer>> en : dataTrain.getWordClassCount().entrySet()){
			vocab.addAll(en.getValue().keySet());
			Map<String, Double> mapOfWord = new HashMap<String, Double>();
			paraWordClass.put(en.getKey(), mapOfWord);
		}
		System.out.println("Vocabulary Size: " + vocab.size());
		//we consider all the words occurring in either class
		int[] sumValue = new int[paraClass.size()];
		int i = 0;
		for(String word : vocab){
			i = 0;
			for(Entry<String, Map<String, Integer>> en : dataTrain.getWordClassCount().entrySet()){
				if(en.getValue().containsKey(word)){
					paraWordClass.get(en.getKey()).put(word, en.getValue().get(word)*1.0 + 1.0);
					sumValue[i] += en.getValue().get(word);
				}else{
					paraWordClass.get(en.getKey()).put(word, 1.0); //Laplace smooth
					sumValue[i] += 1.0;
				}
				i++;
			}
		}
		i = 0;
		for(String word : vocab){
			i = 0;
			for(Entry<String, Map<String, Double>> en : paraWordClass.entrySet()){
				en.getValue().put(word, en.getValue().get(word) / sumValue[i]);
				i++;
			}
		}
	}
	
	private double[] computeSenPost(TextSen sen){
		double[] post = new double[paraClass.size()];
		double[] postInLog = new double[post.length];
		int i = 0;
		for(Entry<String, Double> en : paraClass.entrySet()){ //P(class)
			postInLog[i] += Math.log(en.getValue());
			i++;
		}
		for(Entry<String, Integer> en : sen.getWordTable().entrySet()){ //P(word|class)
			String word = en.getKey();
			i = 0;
			for(Entry<String, Map<String, Double>> en2 : paraWordClass.entrySet()){
				if(en2.getValue().containsKey(word)){
					postInLog[i] += Math.log(en2.getValue().get(word));
				}
				i++;
			}
		}
		//log-sum-exp trick
		double maxPost = postInLog[0];
		for(int k = 0; k < postInLog.length; k++){
			if(postInLog[k] > maxPost)
				maxPost = postInLog[k];
		}
		double sumPost = 0.0;
		for(int k = 0; k < postInLog.length; k++){
			sumPost += Math.exp(postInLog[k] - maxPost);
		}
		sumPost = maxPost + Math.log(sumPost);
		//final posterior
		for(int k = 0; k < post.length; k++){
			post[k] = Math.exp(postInLog[k] - sumPost);
		}
		return post;
	}
	
	private void computePosterior(){
		posterior = new double[dataTest.getSenSize()][paraClass.size()];
		int index = 0;
		for(TextSen sen : dataTest.getSens()){
			posterior[index] = computeSenPost(sen);
			index ++;
		}
	}
	
	public void trainMNB(){
		getParaClass();
		getParaWordClass();
	}
	
	public void testMNB(){
		computePosterior();
	}
	
	public double[][] getTetsResult(){
		return posterior;
	}
	
}

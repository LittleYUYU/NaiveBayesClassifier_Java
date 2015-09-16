package test;


import model.MNB;
import data.NgData;
import data.TextData;
import data.TextSen;

/**
 * Test MNB model.
 * @author LittleYUYU
 *
 */
public class MNBTest {

	TextData dataTrain;
	TextData dataTest;
	MNB model;
	
	//evaluation
	int[][] confMatr; //confusion matrix
	double[] precision;
	double[] recall;
	double macroPrec;
	double macroRec;
	
	public MNBTest(String pathTrainWdTbl, String pathTrainLabel, String pathTestWdTbl, String pathTestLabel ){
		dataTrain = new NgData(pathTrainWdTbl, pathTrainLabel);
		dataTest = new NgData(pathTestWdTbl, pathTestLabel);
		model = new MNB(dataTrain, dataTest);
	}
	
	public MNBTest(String pathTrain, String pathTest){
		dataTrain = new TextData(pathTrain);
		dataTest = new TextData(pathTest);
		model = new MNB(dataTrain, dataTest);
	}
	
	/**
	 * train a MNB model
	 */
	public void trainModel(){
		model.trainMNB();
		System.out.println("MNB model training");
	}
	
	/**
	 * test data
	 */
	public void testModel(){
		model.testMNB();
		
		precision = new double[model.classNum]; 
		recall = new double[model.classNum];
		confMatr = new int[model.classNum][model.classNum];
		macroPrec = 0.0;
		macroRec = 0.0;
		
		evaluation(); //confusion matrix
		computePrec(); //precision
		computeRec(); //recall
		computeMacroPrec(); //Macro Precision
		computeMacroRec(); //Macro Recall
		System.out.println("precision = ");
		for(int i = 0 ; i < precision.length; i ++)
			System.out.print(precision[i] + "\t");
		System.out.println();
		System.out.println("recall = ");
		for(int i = 0 ; i < recall.length; i ++)
			System.out.print(recall[i] + "\t");
		System.out.println();
		System.out.println("macro precision = " + macroPrec);
		System.out.println("macro recall = " + macroRec);
	}

	private void computeMacroRec() {
		// TODO Auto-generated method stub
		for(int i = 0; i < recall.length; i ++){
			macroRec += recall[i];
		}
		macroRec /= recall.length;
	}

	private void computeMacroPrec() {
		// TODO Auto-generated method stub
		for(int i = 0; i < precision.length; i ++){
			macroPrec += precision[i];
		}
		macroPrec /= precision.length;
	}

	private void computeRec() {
		// TODO Auto-generated method stub
		double[] pred = new double[confMatr.length];
		for(int i = 0; i < confMatr.length; i ++){
			for(int j = 0; j < confMatr[i].length; j ++){
				pred[i] += confMatr[i][j];
			}
		}
		for(int label = 0; label < confMatr.length; label ++){
			recall[label] = confMatr[label][label] * 1.0 / pred[label];
		}
	}

	private void computePrec() {
		// TODO Auto-generated method stub
		double[] pred = new double[confMatr.length];
		for(int i = 0; i < confMatr.length; i ++){
			for(int j = 0; j < confMatr[i].length; j ++){
				pred[j] += confMatr[i][j];
			}
		}
		for(int label = 0; label < confMatr.length; label ++){
			precision[label] = confMatr[label][label] * 1.0 / pred[label];
		}
	}

	/**
	 * computing confsion matrix
	 */
	private void evaluation() {
		// TODO Auto-generated method stub
		int idxSen = 0;
		for(TextSen sen : dataTest.getSens()){
			int predict = max(model.getTetsResult()[idxSen]); //predict class (index)
			int trueLabel = model.classIndx.get(sen.getLabel()); //true label (index)
			confMatr[trueLabel][predict] += 1;
			idxSen ++;
		}
	}

	private int max(double[] score) {
		// TODO Auto-generated method stub
		int maxIndx = 0;
		double maxScr = Double.MIN_EXPONENT;
		for(int i = 0; i < score.length; i++){
			if(score[i] > maxScr){
				maxScr = score[i];
				maxIndx = i;
			}
		}
		return maxIndx;
	}
	
	public static void main(String args[]){
//		String pathTrain[] = {"E:/Data/textClass/20NewsGroup/20news-bydate/matlab/train.data",
//				"E:/Data/textClass/20NewsGroup/20news-bydate/matlab/train.label"};
//		String pathTest[] = {"E:/Data/textClass/20NewsGroup/20news-bydate/matlab/test.data",
//				"E:/Data/textClass/20NewsGroup/20news-bydate/matlab/test.label"};
//		MNBTest test = new MNBTest(pathTrain[0], pathTrain[1], pathTest[0], pathTest[1]);
		
//		String pathTrain = "E:/Data/textClass/20NewsGroup/20news-bydate/matlab/train.txt";
//		String pathTest = "E:/Data/textClass/20NewsGroup/20news-bydate/matlab/test.txt";
//		MNBTest test = new MNBTest(pathTrain, pathTest);
		
		MNBTest test = null;
		if(args.length == 2){
			test = new MNBTest(args[0], args[1]);
		}else if(args.length == 4){
			test = new MNBTest(args[0], args[1], args[2], args[3]);
		}else{
			System.out.println("Wrong input arguments!");
			return;
		}
		
		test.trainModel();
		test.testModel();
		
	}
	
}

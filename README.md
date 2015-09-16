# NaiveBayesClassifier_Java
Code for Naive Bayes Classifier in Java

# How to use it in command line (windows)
First, you need to compile it:
		javac test\MNBTest.java
Then, run it:
		java test.MNBTest arg1 arg2
where arg1 refers to the training data, and arg2 refers to the test data. For example:
		java test.MNBTest train.txt test.txt
Tips: For testing my code with 20newsgroup data (Ken Lang, 1995), I write NgData.java for pre-process the 20newsgroup data, which does not follow the input format. So, when you need to pre-process other specific data with different format, you can extend the class TextData to a suitable class like NgData.

# Input Format
The input train or test file should consist of lines of instances. Each line should be consistent with the following form:
		label,feature1 feature2 feature3 ...		
The "label" refers to the true label of this instance, and the following features are separated by blank space.


#Output Format
We output "precision" and "recall" for each class, and compute the final Macro precision and recall value.

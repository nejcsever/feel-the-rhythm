package com.sever.ftr.handlers;

import java.util.ArrayList;

public class ScoringHandler {
	/**
	 * Calculates score percentage from users solution and solution of current level.
	 */
	public static int getScorePercentage(ArrayList<Note> usersSolution, ArrayList<Note> solution) {
		double result = 100.0;
		int usersSolSize = usersSolution.size();
		int solSize = solution.size();
		
		int minSize = usersSolSize;
		
		if (usersSolSize > solSize) {
			minSize = solSize;
			int sub = usersSolSize - solSize;
			double temp = (double) sub / (double) solSize;
			if(temp > 1.0)
				temp = 1.0;
			result -= result * temp; // subtract if users solution is too long
		} else if (usersSolSize < solSize) {
			int sub = solSize - usersSolSize;
			double temp = (double) sub / (double) solSize;
			result -= result * temp; // subtract if users solution is too long
		}
		
		double errorPerc = 1 / (double) solSize; //error per note missed
		for (int i = 0; i < minSize; i++) {
			if (usersSolution.get(i).compareTo(solution.get(i)) != 0) {
				result -= 100.0*errorPerc;
			}
		}
		
		int intResult = (int) Math.floor(result);
		if (intResult < 0) {
			intResult = 0;
		}
		return intResult;
	}
}

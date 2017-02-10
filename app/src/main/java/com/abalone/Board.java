package com.abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
	public Board(int x0, int y0, int l) {

		for (int i = 0; i < initSt.length; i++) {
			state[i] = initSt[i];
			prevState[i] = initSt[i];
		}
		// actbghfjmea uvgfhna fadionf aidngkj fnadsjg naojngi eawniojg narwng
		// owipgn wapng rwjng oparwng onrwaogn roenhg roajng oaerwngo jrwnaig
		// nraeijongjio
		r = (float) (0.45 * l / 10);
		float step = l / 10;
		int k = 0;
		for (int i = 0; i < 26; i++) {
			int n = 5;
			if (i < 5)
				n = 5;
			else if (i < 11)
				n = 6;
			else if (i < 18)
				n = 7;
			else if (i < 26)
				n = 8;
			// else if(i<35)
			// continue;
			float o = (float) (x0 + (9 - n) * l * 0.05);
			float o1 = (float) (x0 + l - (9 - n) * l * 0.05);
			coords[i][0] = o + (k + 3 / 2) * step;
			coords[i][1] = y0 + l * (n - 4) / 10;
			coords[60 - i][0] = o1 - (k + 3 / 2) * step;
			coords[60 - i][1] = y0 + l - l * (n - 4) / 10;
			k++;
			if (k == n)
				k = 0;
		}
		for (int i = 26; i < 35; i++) {
			coords[i][0] = x0 + (i - 26 + 3 / 2) * step;
			coords[i][1] = y0 + l / 2;
		}

		horiz.add(A);
		horiz.add(B);
		horiz.add(C);
		horiz.add(D);
		horiz.add(E);
		horiz.add(F);
		horiz.add(G);
		horiz.add(H);
		horiz.add(I);

		diag1.add(d9);
		diag1.add(d8);
		diag1.add(d7);
		diag1.add(d6);
		diag1.add(d5);
		diag1.add(d4);
		diag1.add(d3);
		diag1.add(d2);
		diag1.add(d1);

		diag2.add(e9);
		diag2.add(e8);
		diag2.add(e7);
		diag2.add(e6);
		diag2.add(e5);
		diag2.add(e4);
		diag2.add(e3);
		diag2.add(e2);
		diag2.add(e1);
	}

	List<Integer> I = Arrays.asList(0, 1, 2, 3, 4);
	List<Integer> H = Arrays.asList(5, 6, 7, 8, 9, 10);
	List<Integer> G = Arrays.asList(11, 12, 13, 14, 15, 16, 17);
	List<Integer> F = Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25);
	List<Integer> E = Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34);
	List<Integer> D = Arrays.asList(35, 36, 37, 38, 39, 40, 41, 42);
	List<Integer> C = Arrays.asList(43, 44, 45, 46, 47, 48, 49);
	List<Integer> B = Arrays.asList(50, 51, 52, 53, 54, 55);
	List<Integer> A = Arrays.asList(56, 57, 58, 59, 60);
	List<List<Integer>> horiz = new ArrayList<List<Integer>>();

	List<Integer> d9 = Arrays.asList(4, 10, 17, 25, 34);
	List<Integer> d8 = Arrays.asList(3, 9, 16, 24, 33, 42);
	List<Integer> d7 = Arrays.asList(2, 8, 15, 23, 32, 41, 49);
	List<Integer> d6 = Arrays.asList(1, 7, 14, 22, 31, 40, 48, 55);
	List<Integer> d5 = Arrays.asList(0, 6, 13, 21, 30, 39, 47, 54, 60);
	List<Integer> d4 = Arrays.asList(5, 12, 20, 29, 38, 46, 53, 59);
	List<Integer> d3 = Arrays.asList(11, 19, 28, 37, 45, 52, 58);
	List<Integer> d2 = Arrays.asList(18, 27, 36, 44, 51, 57);
	List<Integer> d1 = Arrays.asList(26, 35, 43, 50, 56);
	List<List<Integer>> diag1 = new ArrayList<List<Integer>>();

	List<Integer> e9 = Arrays.asList(26, 18, 11, 5, 0);
	List<Integer> e8 = Arrays.asList(35, 27, 19, 12, 6, 1);
	List<Integer> e7 = Arrays.asList(43, 36, 28, 20, 13, 7, 2);
	List<Integer> e6 = Arrays.asList(50, 44, 37, 29, 21, 14, 8, 3);
	List<Integer> e5 = Arrays.asList(56, 51, 45, 38, 30, 22, 15, 9, 4);
	List<Integer> e4 = Arrays.asList(57, 52, 46, 39, 31, 23, 16, 10);
	List<Integer> e3 = Arrays.asList(58, 53, 47, 40, 32, 24, 17);
	List<Integer> e2 = Arrays.asList(59, 54, 48, 41, 33, 25);
	List<Integer> e1 = Arrays.asList(60, 55, 49, 42, 34);
	List<List<Integer>> diag2 = new ArrayList<List<Integer>>();

	List<Integer> bound = Arrays.asList(26, 18, 11, 5, 0, 1, 2, 3, 4, 10, 17,
			25, 34, 42, 49, 55, 60, 59, 58, 57, 56, 50, 43, 35);

	float coords[][] = new float[61][2];
	int initSt[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1 };
	int state[] = new int[61];
	int prevState[] = new int[61];
	float r = 2;

}

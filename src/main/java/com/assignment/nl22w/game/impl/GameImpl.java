package com.assignment.nl22w.game.impl;

import com.assignment.nl22w.game.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class GameImpl implements Game {

	@Override
	public int escapeFromTheWoods(Resource resource) throws IOException {
		String[][] matrix = readGameFileTo2DArray(resource.getFile());
		int shortestPath = findShortestDistance(matrix);
		return shortestPath;
	}


	private static String[][] readGameFileTo2DArray(File file) throws IOException {
		int rowLength = 0;
		String line = "";
		Scanner scanner = new Scanner(file);
		List<String> readFileList = new ArrayList<>();
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			readFileList.add(line);
			++rowLength;
		}
		int columnLength = readFileList.get(0).trim().split("").length;
		String[] array;
		String[][] matrix = new String[rowLength][columnLength];
		for (int i = 0; i < matrix.length; i++) {
			array = readFileList.get(i).trim().split("");
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = array[j];
			}
		}
		return matrix;
	}


	private static int findShortestDistance(String[][] matrix) {
		int shortestDistance = Integer.MAX_VALUE;
		boolean hasExit = false;
		int[] start = findStartPosition(matrix);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if ((i == 0 || j == 0 || i == matrix.length - 1 || j == matrix[0].length - 1) && " ".equals(matrix[i][j])) {
					hasExit = true;
					int[] end = new int[]{i, j};
					int distance = searchShortestPath(matrix, start, end);
					if (shortestDistance > distance) {
						shortestDistance = distance;
					}
				}
			}
		}
		if (hasExit) {
			return shortestDistance;
		}
		return 0;
	}


	private static int searchShortestPath(String[][] matrix, int[] start, int[] end) {
		int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
		int m = matrix.length;
		int n = matrix[0].length;
		int startX = start[0];
		int startY = start[1];
		int exitX = end[0];
		int exitY = end[1];
		if ("1".equals(matrix[startX][startY]) || "1".equals(matrix[exitX][exitY])) {
			return -1;
		}
		boolean[][] visited = new boolean[m][n];
		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[]{startX, startY});
		int count = 0;
		while(!queue.isEmpty()) {
			int queueSize = queue.size();
			for (int i = 0; i < queueSize; i++) {
				int[] poll = queue.poll();
				if (poll[0] == exitX && poll[1] == exitY) {
					return count;
				}
				for (int[] direction : directions) {
					int x = poll[0] + direction[0];
					int y = poll[1] + direction[1];
					if (x >= 0 && x < m && y >= 0 && y < n && " ".equals(matrix[x][y]) && !visited[x][y]) {
						queue.add(new int[]{x, y});
						visited[x][y] = true;
					}
				}
			}
			count++;
		}
		return -1;
	}


	private static int[] findStartPosition(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if ("X".equals(matrix[i][j])) {
					return new int[]{i, j};
				}
			}
		}
		return new int[]{-1, -1};
	}
}

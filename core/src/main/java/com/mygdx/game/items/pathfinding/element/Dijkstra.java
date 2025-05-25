package com.mygdx.game.items.pathfinding.element;

import static com.mygdx.game.Settings.print;
import static java.lang.Integer.MAX_VALUE;

public class Dijkstra {
	static final int V = 9;

	private static int minDistance(int[] dist, boolean[] verticeYaProcesado) {
		int min = MAX_VALUE;
		int min_index = 0;
		for (; min_index < V; min_index++)
			if (!verticeYaProcesado[min_index] && dist[min_index] <= min)
				min = dist[min_index];

		return min_index;
	}


	private static void printSolution(int[] dist) {
		print("Distancia desde el vertice hasta el origen");
		for (int i = 0; i < V; i++) {
			print(i + "\t\t" + dist[i]);
		}
	}

	private static void dijkstra(int[][] grafo, int src) {
		int[] dist = new int[V];
		boolean[] verticeYaProcesado = new boolean[V];

		for (int i = 0; i < V; i++) {
			dist[i] = MAX_VALUE;
			verticeYaProcesado[i] = false;
		}
		dist[src] = 0;

		for (int i = 0; i <= V; i++) {
			int u = minDistance(dist, verticeYaProcesado);
			verticeYaProcesado[u] = true;
			for (int j = 0; j < V; j++)
				if (!verticeYaProcesado[i] && grafo[u][j] > 0 && dist[u] != MAX_VALUE && dist[u] + grafo[u][j] < dist[j])
					dist[j] = dist[u] + grafo[u][j];
		}
		printSolution(dist);

	}


		public static void main(String... args) {
		// Let us create the example graph discussed above
   int[][] graph = {{0, 1, 2, 3, 4, 5, 6, 7, 8},
					{0, 0, 11, 12, 13, 14, 15, 16, 17},
					{18, 19, 0, 0, 22, 23, 24, 25, 26},
					{27, 28, 29, 0, 31, 32, 33, 34, 35},
					{36, 37, 38, 0, 0, 41, 42, 43, 44},
					{45, 46, 47, 48, 0, 0, 51, 52, 53},
					{54, 55, 56, 57, 58, 0, 0, 61, 62},
					{63, 64, 65, 66, 67, 68, 0, 0, 71},
					{72, 73, 74, 75, 76, 77, 78, 0, 0}};
			dijkstra(graph, 0);

		}
}
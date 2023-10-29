package agente;

import algoritmo.ProgramaLadrao;
import buscas.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static buscas.AStar.aStar;

public class Ladrao extends ProgramaLadrao {

	List<Node> path;

	public int acao() {
		return walk();
	}

	public int walk() {
		if (path == null || path.isEmpty()) {
			int[][] vision = getVision();
			path = aStar(vision, new Node(2, 2), heuristic(vision));
			if (path != null) return path.remove(0).act;
		}
		else {
			return path.remove(0).act;
		}
		return (int) (Math.random() * 5);
	}

	public Node heuristic(int[][] vision){
		/* por enquanto a gente só ta verificando os poupadores, porém temos que verificar os rastros e os ladroes para fazer
		a coordenação */

		HashMap<int[], Integer> direction = checkDirections(directions(vision)); // melhor direção

		ArrayList<int[]> positions = new ArrayList<>(); // posicoes dos poupadores encontrados nessa direção

		for (Map.Entry<int[], Integer> entry : direction.entrySet()) {
			int[] key = entry.getKey();
			int value = entry.getValue();
			if(value >= 100 && value < 200){ // verificando se tem poupador
				positions.add(key);
			}
		}

		int[] goalPosition = new int[2];

		if(!positions.isEmpty()){ // verificar qual dos poupadores ta mais perto
			int smaller = Manhattan(new int[] {2, 2}, positions.get(0));
			goalPosition = positions.get(0);
			int aux;

			for(int i = 1; i<positions.size(); i++){
				aux = Manhattan(new int[] {2, 2}, positions.get(i));
				if(smaller > aux){
					smaller = aux;
					goalPosition = positions.get(i);
				}
			}
		}
		else{
			do {
				goalPosition[0] = (int) (Math.random() * 5);
				goalPosition[1] = (int) (Math.random() * 5);
		} while (goalPosition[0] == 2 && goalPosition[1] == 2); // verificando se nao é a posição do proprio ladrao
		}

		return new Node(goalPosition[0], goalPosition[1]);
	}

	public int Manhattan(int[] a, int[] b){ // calcular distancia
		return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
	}


	public HashMap<int[], Integer> checkDirections(ArrayList<HashMap<int[], Integer>> directions){
		// directions posições
		// posição 0 -> cima
		// posição 1 -> baixo
		// posição 2 -> direita
		// posição 3 -> esquerda

		int bigger = calculateWeight(directions.get(0));
		int index = 0;

		for(int i = 1; i<directions.size(); i++){
			if(bigger < calculateWeight(directions.get(i))){
				bigger = calculateWeight(directions.get(i));
				index = i;
			}
		}

		return directions.get(index);
	}

	public int calculateWeight(HashMap<int[], Integer> direction){
        // função de teste ainda, somente contando quantos poupadores tem na visao
		int count = 0;
		for(int i = 0, k = 0; i<2; i++, k+=10){
			if(direction.containsValue(100 + k)){
				count++;
			}
		}

		return count;
	}
	public ArrayList<HashMap<int[], Integer>> directions(int[][] vision) {
		// Cima, baixo, direita, esquerda
		//  0      1       2         3
		ArrayList<HashMap<int[], Integer>> directions = new ArrayList<HashMap<int[], Integer>>();

		HashMap<int[], Integer> up = new HashMap<>(); // posição e valor
		HashMap<int[], Integer> down = new HashMap<>();
		HashMap<int[], Integer> left = new HashMap<>();
		HashMap<int[], Integer> right = new HashMap<>();

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				up.put(new int[] {i, j}, vision[i][j]);
			}
		}

		left.put(new int[] {2, 0}, vision[2][0]);
		left.put(new int[] {2, 1}, vision[2][1]);
		right.put(new int[] {2, 3}, vision[2][3]);
		right.put(new int[] {2, 4}, vision[2][4]);

		for (int i = 3; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				down.put(new int[] {i, j}, vision[i][j]);
			}
		}

		directions.add(up);
		directions.add(down);
		directions.add(right);
		directions.add(left);
		return directions;
	}


	public int[][] getVision(){
		int[][] matrix = new int[5][5];
		int[] array = sensor.getVisaoIdentificacao();

		for(int i = 0, j = 0; i<5; i++){
			for(int k = 0; k<5; k++, j++){
				if(i == 2 && k == 2){
					matrix[i][k] = 0;
					j--;
				} else{
					matrix[i][k] = array[j];
				}
			}
		}

		return matrix;
	}
	public void printVision(int[][] vision){
		for(int i = 0; i < vision.length; i++){
			for(int j = 0; j < vision.length; j++){
				System.out.print(vision[i][j] + " ");
			}System.out.println();
		}System.out.println();
	}
}
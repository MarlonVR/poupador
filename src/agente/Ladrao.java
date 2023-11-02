package agente;

import algoritmo.ProgramaLadrao;
import buscas.Node;

import java.util.*;


import static buscas.AStar.aStar;

public class Ladrao extends ProgramaLadrao {

	List<Node> caminho;
	public int acao() {

		return andar();
	}

	public int andar() {
		if (caminho == null || caminho.isEmpty()) {
			int[][] visao = getVisao();
			caminho = aStar(visao, new Node(2, 2), heuristica(visao));
			if (caminho != null) return caminho.remove(0).acao;
		}
		else {
			return caminho.remove(0).acao;
		}
		return (int) (Math.random() * 5);
	}

	public Node heuristica(int[][] visao){
		HashMap<int[], Integer> melhorDirecao = melhorDirecao(direcoes(visao));

		ArrayList<int[]> posicoes = new ArrayList<>(); // posicoes dos elementos encontrados na melhorDirecao

		int[] chaveAux = {-100}; // seguindo rastro
		if(melhorDirecao.containsKey(chaveAux)){
			switch (melhorDirecao.get(chaveAux)){
				case 1:
					return new Node(1, 2);
				case 2:
					return new Node(3, 2);
				case 3:
					return new Node(2, 3);
				case 4:
					return new Node(2, 1);
			}
		}

		for (Map.Entry<int[], Integer> entry : melhorDirecao.entrySet()) {
			int[] key = entry.getKey();
			int value = entry.getValue();
			if(value >= 100 && value < 200){ // verificando se tem poupador
				posicoes.add(key);
			}
		}

		int[] posicaoObjetivo = new int[2];

		if(!posicoes.isEmpty()){ // verificar qual dos elementos esta mais perto
			int menor = Manhattan(new int[] {2, 2}, posicoes.get(0));
			posicaoObjetivo = posicoes.get(0);
			int aux;

			for(int i = 1; i<posicoes.size(); i++){
				aux = Manhattan(new int[] {2, 2}, posicoes.get(i));
				if(menor > aux){
					menor = aux;
					posicaoObjetivo = posicoes.get(i);
				}
			}
		}
		else{
			do {
				posicaoObjetivo[0] = (int) (Math.random() * 5);
				posicaoObjetivo[1] = (int) (Math.random() * 5);
			} while (posicaoObjetivo[0] == 2 && posicaoObjetivo[1] == 2); // verificando se nao é a posição do proprio ladrao
		}

		return new Node(posicaoObjetivo[0], posicaoObjetivo[1]);
	}
	public int Manhattan(int[] a, int[] b){ // calcular distancia
		return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
	}

	public HashMap<int[], Integer> melhorDirecao(ArrayList<HashMap<int[], Integer>> direcoes){
		// Arraylist com as posicoes e os olfatos
		// posição 0 -> cima
		// posição 1 -> baixo
		// posição 2 -> direita
		// posição 3 -> esquerda
		//                                       olfatoCimaPoupador  olfatoCimaLadrao
		int bigger = calcularPeso(direcoes.get(0), direcoes.get(4), direcoes.get(8));
		int index = 0;

		for(int i = 1; i<4; i++){
			if(bigger < calcularPeso(direcoes.get(i), direcoes.get(i+4), direcoes.get(i+8))){
				bigger = calcularPeso(direcoes.get(i), direcoes.get(i+4), direcoes.get(i+8));
				index = i;
			}
		}

		/*
		* 0 - Ficar Parado() – o agente fica parado e não perde energia
		  1 - MoverCima() – move o agente uma posição acima da atual
	      2 - MoverBaixo() – move o agente uma posição abaixo da atual
	      3 - MoverDireita() – move o agente uma posição a direita da atual
	      4 - MoverEsquerda() – move o agente uma posição a esquerda da atual */

		if(bigger < 100){ // Se tiver somente rastro na visao
			HashMap<int[], Integer> aux = new HashMap<int[], Integer>();
			aux.put(new int []{-100}, index + 1);
			// colocando -100 na chave para saber que o hashmap de retorno não é com valores da visao
			// e sim do olfato. Sendo o seu valor o movimento a se fazer. (valores de 1 a 4)
		}
		return direcoes.get(index);
	}

	public int calcularPeso(HashMap<int[], Integer> direcao, HashMap<int[], Integer> olfatoPoupador, HashMap<int[], Integer> olfatoLadrao){
		// peso poupador -> 200
		// peso rastro poupador -> 15
		// peso ladrao -> 10
		// peso rastro ladrao -> 1

		int contador = 0;
		for(int i = 0; i<=10; i+=10){
			if(direcao.containsValue(100 + i)){ //contando quant. poupadores
				contador+=200;
			}
		}
		for(int i = 0; i<=30; i+=10){
			if(direcao.containsValue(200 + i)){//contando quant. ladroes
				contador+=10;
			}
		}

		if(contador == 0){ // caso nao tenha poupadores ou ladroes, vamos verificar se tem rastros
			for(int i = 1; i<=5; i++){
				if(olfatoPoupador.containsValue(i)){
					contador+=15;
					break;
				}
			}
			/*for(int i = 1; i<=5; i++){   Por enquanto ta seguindo o proprio rastro
				if(olfatoLadrao.containsValue(i)){
					contador+=1;
					return contador;
				}
			}*/
		}

		return contador;
	}
	public ArrayList<HashMap<int[], Integer>> direcoes(int[][] visao) {
		// Cima, baixo, direita, esquerda
		//  0      1       2         3
		ArrayList<HashMap<int[], Integer>> direcoes = new ArrayList<HashMap<int[], Integer>>();

		HashMap<int[], Integer> cima = new HashMap<>(); // posição e valor
		HashMap<int[], Integer> baixo = new HashMap<>();
		HashMap<int[], Integer> esquerda = new HashMap<>();
		HashMap<int[], Integer> direita = new HashMap<>();

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				cima.put(new int[] {i, j}, visao[i][j]);
			}
		}

		esquerda.put(new int[] {2, 0}, visao[2][0]);
		esquerda.put(new int[] {2, 1}, visao[2][1]);
		direita.put(new int[] {2, 3}, visao[2][3]);
		direita.put(new int[] {2, 4}, visao[2][4]);

		for (int i = 3; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				baixo.put(new int[] {i, j}, visao[i][j]);
			}
		}

		HashMap<int[], Integer> olfatoCimaPoupador = new HashMap<>();
		HashMap<int[], Integer> olfatoBaixoPoupador = new HashMap<>();
		HashMap<int[], Integer> olfatoEsquerdaPoupador = new HashMap<>();
		HashMap<int[], Integer> olfatoDireitaPoupador = new HashMap<>();

		HashMap<int[], Integer> olfatoCimaLadrao = new HashMap<>();
		HashMap<int[], Integer> olfatoBaixoLadrao = new HashMap<>();
		HashMap<int[], Integer> olfatoEsquerdaLadrao = new HashMap<>();
		HashMap<int[], Integer> olfatoDireitaLadrao = new HashMap<>();

		int[][] olfatoPoupador = getOlfatoPoupador();
		int[][] olfatoLadrao = getOlfatoLadrao();

		olfatoCimaPoupador.put(new int[] {0, 0}, olfatoPoupador[0][0]);
		olfatoCimaPoupador.put(new int[] {0, 1}, olfatoPoupador[0][1]);
		olfatoCimaPoupador.put(new int[] {0, 2}, olfatoPoupador[0][2]);
		olfatoEsquerdaPoupador.put(new int[] {0, 0}, olfatoPoupador[0][0]);
		olfatoEsquerdaPoupador.put(new int[] {1, 0}, olfatoPoupador[1][0]);
		olfatoEsquerdaPoupador.put(new int[] {2, 0}, olfatoPoupador[2][0]);
		olfatoDireitaPoupador.put(new int[] {0, 2}, olfatoPoupador[0][2]);
		olfatoDireitaPoupador.put(new int[] {1, 2}, olfatoPoupador[1][2]);
		olfatoDireitaPoupador.put(new int[] {2, 2}, olfatoPoupador[2][2]);
		olfatoBaixoPoupador.put(new int[] {2, 0}, olfatoPoupador[2][0]);
		olfatoBaixoPoupador.put(new int[] {2, 1}, olfatoPoupador[2][1]);
		olfatoBaixoPoupador.put(new int[] {2, 2}, olfatoPoupador[2][2]);

		olfatoCimaLadrao.put(new int[] {0, 0}, olfatoLadrao[0][0]);
		olfatoCimaLadrao.put(new int[] {0, 1}, olfatoLadrao[0][1]);
		olfatoCimaLadrao.put(new int[] {0, 2}, olfatoLadrao[0][2]);
		olfatoEsquerdaLadrao.put(new int[] {0, 0}, olfatoLadrao[0][0]);
		olfatoEsquerdaLadrao.put(new int[] {1, 0}, olfatoLadrao[1][0]);
		olfatoEsquerdaLadrao.put(new int[] {2, 0}, olfatoLadrao[2][0]);
		olfatoDireitaLadrao.put(new int[] {0, 2}, olfatoLadrao[0][2]);
		olfatoDireitaLadrao.put(new int[] {1, 2}, olfatoLadrao[1][2]);
		olfatoDireitaLadrao.put(new int[] {2, 2}, olfatoLadrao[2][2]);
		olfatoBaixoLadrao.put(new int[] {2, 0}, olfatoLadrao[2][0]);
		olfatoBaixoLadrao.put(new int[] {2, 1}, olfatoLadrao[2][1]);
		olfatoBaixoLadrao.put(new int[] {2, 2}, olfatoLadrao[2][2]);



		direcoes.add(cima);
		direcoes.add(baixo);
		direcoes.add(direita);
		direcoes.add(esquerda);

		direcoes.add(olfatoCimaPoupador);
		direcoes.add(olfatoBaixoPoupador);
		direcoes.add(olfatoEsquerdaPoupador);
		direcoes.add(olfatoDireitaPoupador);

		direcoes.add(olfatoCimaLadrao);
		direcoes.add(olfatoBaixoLadrao);
		direcoes.add(olfatoEsquerdaLadrao);
		direcoes.add(olfatoDireitaLadrao);

		return direcoes;
	}


	public int[][] getVisao(){
		int[][] matriz = new int[5][5];
		int[] array = sensor.getVisaoIdentificacao();

		for(int i = 0, j = 0; i<5; i++){
			for(int k = 0; k<5; k++, j++){
				if(i == 2 && k == 2){
					matriz[i][k] = 0;
					j--;
				} else{
					matriz[i][k] = array[j];
				}
			}
		}

		return matriz;
	}

	public int[][] getOlfatoPoupador(){
		int[][] matriz = new int[3][3];
		int[] array = sensor.getAmbienteOlfatoPoupador();

		for(int i = 0, j = 0; i<3; i++){
			for(int k = 0; k<3; k++, j++){
				if(i == 1 && k == 1){
					matriz[i][k] = 0;
					j--;
				} else{
					matriz[i][k] = array[j];
				}
			}
		}

		return matriz;
	}

	public int[][] getOlfatoLadrao(){
		int[][] matriz = new int[3][3];
		int[] array = sensor.getAmbienteOlfatoLadrao();

		for(int i = 0, j = 0; i<3; i++){
			for(int k = 0; k<3; k++, j++){
				if(i == 1 && k == 1){
					matriz[i][k] = 0;
					j--;
				} else{
					matriz[i][k] = array[j];
				}
			}
		}

		return matriz;
	}
	public void imprimirVisao(int[][] visao){
		for(int i = 0; i < visao.length; i++){
			for(int j = 0; j < visao.length; j++){
				System.out.print(visao[i][j] + " ");
			}System.out.println();
		}System.out.println();
	}
}
package agente;

import algoritmo.ProgramaLadrao;
import buscas.Node;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import static buscas.AStar.aStar;

public class Ladrao extends ProgramaLadrao {

	List<Node> caminho;
	private LinkedList<double[]> ultimasPosicoes = new LinkedList<>();
	int[] chaveAux = {-100};
	double x,y;
	int moedas = 0;

	private Timer timer;
	private final Object lock = new Object();
	TimerTask task;

	Boolean assaltoLiberado = true;

	public double[] coordenadasRelativasParaAbsolutas(double[] coordenadasRelativas, double x, double y) {
		double linhaAbsoluta = sensor.getPosicao().getY() - 2 + coordenadasRelativas[0];
		double colunaAbsoluta = sensor.getPosicao().getX() - 2 + coordenadasRelativas[1];
		return new double[]{colunaAbsoluta, linhaAbsoluta};
	}

	public int acao() {
		synchronized(lock) {
			if(moedas < sensor.getNumeroDeMoedas() && (task == null || task.scheduledExecutionTime() == 0)) {
				// Cancela a task anterior se ela existir
				if (task != null) {
					task.cancel();
				}
				task = new TimerTask() {
					@Override
					public void run() {
						synchronized(lock) {
							System.out.println("Poupador assaltado!");
							assaltoLiberado = true;
							task = null; // Reset task
						}
					}
				};
				if (timer == null) {
					timer = new Timer();
				}
				assaltoLiberado = false;
				timer.schedule(task, 5000);
			}
		}

		moedas = sensor.getNumeroDeMoedas();
		x = sensor.getPosicao().getX();
		y = sensor.getPosicao().getY();

		double[] posicaoAtual = {x, y};
		ultimasPosicoes.add(posicaoAtual);

		if (ultimasPosicoes.size() > 10) {
			ultimasPosicoes.removeFirst();
		}
		return andar();
	}

	public int andar() {
		/*if(sensor.getPosicao().getX() == 0 && sensor.getPosicao().getY() == 0 || sensor.getPosicao().getX() == 0 && sensor.getPosicao().getY() == 29 || sensor.getPosicao().getX() == 29 && sensor.getPosicao().getY() == 0){
			return 0; //fazendo os outros ladroes nao serem executados para nao atrapalhar no debug
		}*/

		if (caminho == null || caminho.isEmpty()) {
			int[][] visao = getVisao();
			Node objetivo = heuristica(visao);

			if(objetivo != null){
				caminho = aStar(visao, new Node(2, 2), objetivo);
				if (caminho != null) {
					int aux = caminho.remove(0).acao;
					return caminho.remove(0).acao;
				}
			}
		}
		else {
			return caminho.remove(0).acao;
		}

		int direcao;
		do{
			direcao = (int) (Math.random() * 5);
		}while (direcaoVisitada(direcao));

		return direcao;
	}
	public boolean direcaoVisitada(int direcao) {
		double[] coordenadasRelativas;
		switch (direcao) {
			case 1: // cima
				coordenadasRelativas = new double[]{-1, 0};
				break;
			case 2: // baixo
				coordenadasRelativas = new double[]{1, 0};
				break;
			case 3: // direita
				coordenadasRelativas = new double[]{0, 1};
				break;
			case 4: // esquerda
				coordenadasRelativas = new double[]{0, -1};
				break;
			default:
				return false;
		}

		double[] coordenadaProxima = coordenadasRelativasParaAbsolutas(coordenadasRelativas, x, y);

		for (double[] posicao : ultimasPosicoes) {
			if (Arrays.equals(posicao, coordenadaProxima)) {
				return true;
			}
		}
		return false;
	}


	public Node heuristica(int[][] visao){
		HashMap<int[], Integer> melhorDirecao = melhorDirecao(direcoes(visao));

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

		ArrayList<int[]> posicoes = new ArrayList<>(); // posicoes dos elementos encontrados na melhorDirecao

		for (Map.Entry<int[], Integer> entry : melhorDirecao.entrySet()) {
			int[] key = entry.getKey();
			int value = entry.getValue();
			if(value >= 100 && value < 200){ // verificando se tem poupador
				posicoes.add(key);
			}
		}

		int[] posicaoObjetivo;

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

			return new Node(posicaoObjetivo[0], posicaoObjetivo[1]);
		}
		return null;
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
		int peso;

		for(int i = 1; i<4; i++){
			peso = calcularPeso(direcoes.get(i), direcoes.get(i+4), direcoes.get(i+8));

			if(bigger < peso){
				bigger = peso;
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
			aux.put(chaveAux, index + 1);
			return aux;
			// colocando -100 na chave para saber que o hashmap de retorno não é com valores da visao
			// Sendo o seu valor o movimento a ser feito. (valores de 1 a 4)
		}

		return direcoes.get(index);
	}

	public int calcularPeso(HashMap<int[], Integer> direcao, HashMap<int[], Integer> olfatoPoupador, HashMap<int[], Integer> olfatoLadrao){
		// peso poupador -> 200
		// peso rastro poupador -> 15
		// peso rastro ladrao -> 1

		int contador = 0;

		if(assaltoLiberado){
			for(int i = 0; i<=10; i+=10){
				if(direcao.containsValue(100 + i)){ //contando quant. poupadores
					contador+=200;
				}
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

			int valor;
			for (Map.Entry<int[], Integer> elementosDaDirecao : direcao.entrySet()) { //verifica obstaculos
				valor = elementosDaDirecao.getValue();
				if (valor == -2 || valor == 1 || valor == 3 || valor == 4 || valor == 5) {
					contador-=1;
				}
				else if (valor == -1){
					contador -=5;
				}

				double[] valoresInteiros = new double[elementosDaDirecao.getKey().length]; // transformando em double para comparar
				for (int i = 0; i < elementosDaDirecao.getKey().length; i++) {
					valoresInteiros[i] = elementosDaDirecao.getKey()[i];
				}

				double[] coordenadaReal = coordenadasRelativasParaAbsolutas(valoresInteiros, x ,y);
				for (double[] posicao : ultimasPosicoes) {
					if (Arrays.equals(posicao, coordenadaReal)) {
						contador -= 2; // Subtrai um valor se a posição estiver nas últimas 5 posições.
					}
				}
			}

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

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				esquerda.put(new int[] {i, j}, visao[i][j]);
			}
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 3; j < 5; j++) {
				direita.put(new int[] {i, j}, visao[i][j]);
			}
		}

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
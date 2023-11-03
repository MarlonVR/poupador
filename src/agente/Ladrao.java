package agente;
import algoritmo.ProgramaLadrao;
import java.util.*;

import static agente.Ladrao.AStar.aStar;

public class Ladrao extends ProgramaLadrao {
	public static class Node {
		public int linha, coluna;  // Coordenadas do nó no grafo
		int g;     // Custo real do início até este nó
		int h;     // Estimativa heurística do custo deste nó até o destino
		Node parent;  // Nó pai no caminho
		public int acao;
		public Node(int x, int y) {
			this.linha = x;
			this.coluna = y;
		}

		public int f() {
			return g + h;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			Node node = (Node) obj;
			return linha == node.linha && coluna == node.coluna;
		}

		@Override
		public int hashCode() {
			return Objects.hash(linha, coluna);
		}
	}

	public class AStar {
		public static List<Node> aStar(int[][] mapa, Node inicio, Node objetivo) {
			Set<Node> aberto = new HashSet<>();
			Set<Node> fechado = new HashSet<>();
			aberto.add(inicio);

			while (!aberto.isEmpty()) {
				Node atual = null;
				for (Node node : aberto) {
					if (atual == null || node.f() < atual.f()) {
						atual = node;
					}
				}

				if (atual.equals(objetivo)) {
					return reconstruirCaminho(atual);
				}

				aberto.remove(atual);
				fechado.add(atual);

				for (Node vizinho : getVizinhos(atual, mapa)) {
					if (fechado.contains(vizinho)) {
						continue;
					}

					int custoG = atual.g + 1;  // Distância entre nós é 1
					// custo do nó atual para o vizinho

					if (!aberto.contains(vizinho) || custoG < vizinho.g) {
						vizinho.parent = atual;
						vizinho.g = custoG;
						vizinho.h = heuristica(vizinho, objetivo);
						aberto.add(vizinho);
					}
				}
			}

			return null;  // Caminho não encontrado
		}

		public static List<Node> reconstruirCaminho(Node current) {
			List<Node> path = new ArrayList<>();
			while (current != null) {
				path.add(current);
				current = current.parent;
			}
			Collections.reverse(path);
			return path;
		}

		public static int heuristica(Node a, Node b) {
			return Math.abs(a.linha - b.linha) + Math.abs(a.coluna - b.coluna);  // Heurística de Manhattan
		}

		public static List<Node> getVizinhos(Node node, int[][] mapa) {
			List<Node> vizinhos = new ArrayList<>();
			int linhas = mapa.length;
			int colunas = mapa[0].length;
			int x = node.linha;
			int y = node.coluna;

			if (x > 0 && mapa[x - 1][y] == 0 || x > 0 && mapa[x - 1][y] >= 100 && mapa[x - 1][y] < 200) { // cima
				vizinhos.add(new Node(x - 1, y));
				vizinhos.get(vizinhos.size()-1).acao = 1;
			}
			if (x < linhas - 1 && mapa[x + 1][y] == 0 || x < linhas - 1 && mapa[x + 1][y] >= 100 && mapa[x + 1][y] < 200) { // baixo
				vizinhos.add(new Node(x + 1, y));
				vizinhos.get(vizinhos.size()-1).acao = 2;
			}
			if (y > 0 && mapa[x][y - 1] == 0 || y > 0 && mapa[x][y - 1] >= 100 && mapa[x][y - 1] < 200) { // esquerda
				vizinhos.add(new Node(x, y - 1));
				vizinhos.get(vizinhos.size()-1).acao = 4;
			}
			if (y < colunas - 1 && mapa[x][y + 1] == 0 || y < colunas - 1 && mapa[x][y + 1] >= 100 && mapa[x][y + 1] < 200) { // direita
				vizinhos.add(new Node(x, y + 1));
				vizinhos.get(vizinhos.size()-1).acao = 3;
			}

			return vizinhos;
		}
	}

	public class Elemento{
		Coordenada posicao;
		int valor;

		public Elemento(Coordenada posicao, int valor){
			this.posicao = posicao;
			this.valor = valor;
		}

		public Coordenada getPosicao(){
			return posicao;
		}

		public int getValor(){
			return valor;
		}
	}

	public class Coordenada{
		int x, y;

		public Coordenada(int x, int y){
			this.x = x;
			this.y = y;

		}

		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
	}
	List<Node> caminho;
	public int acao() {

		return andar();
	}

	public int andar() {
		if(sensor.getPosicao().getX() == 0 && sensor.getPosicao().getY() == 0 || sensor.getPosicao().getX() == 0 && sensor.getPosicao().getY() == 29 || sensor.getPosicao().getX() == 29 && sensor.getPosicao().getY() == 0){
			return 0; //fazendo os outros ladroes nao serem executados para nao atrapalhar no debug
		}

		if (caminho == null || caminho.isEmpty()) {
			int[][] visao = getVisao();
			caminho = aStar(visao, new Node(2, 2), heuristica(visao));

			if (caminho != null) {
				int removido = caminho.remove(0).acao; //tirando a primeira posicao, pois sempre a ação do primeiro nó é = 0 (ficar parado)
				return caminho.remove(0).acao;
			}
		}
		else {
			return caminho.remove(0).acao;
		}
		return (int) (Math.random() * 5);
	}

	public Node heuristica(int[][] visao){
		ArrayList<Elemento> melhorDirecao = melhorDirecao(direcoes(visao));

		ArrayList<Coordenada> posicoes = new ArrayList<>();

		//se o tamanho da lista(melhorDirecao) for igual a 1, o valor que está nessa posicao significa o movimento a ser feito
		if(melhorDirecao.size() == 1){
			switch (melhorDirecao.get(0).getValor()){
				case 0: //cima
					return new Node(1, 2);
				case 1: // baixo
					return new Node(3, 2);
				case 2: // direita
					return new Node(2, 3);
				case 3: // esquerda
					return new Node(2, 1);
			}
		}

		for (Elemento elemento : melhorDirecao) {
			if(elemento.getValor() >= 100 && elemento.getValor() < 200){ // verificando se tem poupadores
				posicoes.add(elemento.getPosicao());
			}
		}

		Coordenada posicaoObjetivo = new Coordenada(2, 2); // valores qualquer para inicializar

		if(!posicoes.isEmpty()){ // verificar qual dos elementos esta mais perto
			int menor = Manhattan(new Coordenada(2, 2), posicoes.get(0));
			posicaoObjetivo = posicoes.get(0);
			int aux;

			for(int i = 1; i<posicoes.size(); i++){
				aux = Manhattan(new Coordenada(2, 2), posicoes.get(i));
				if(menor > aux){
					menor = aux;
					posicaoObjetivo = posicoes.get(i);
				}
			}
		}
		else{
			do {
				posicaoObjetivo.x = (int) (Math.random() * 5);
				posicaoObjetivo.y = (int) (Math.random() * 5);
			} while (posicaoObjetivo.getX() == 2 && posicaoObjetivo.getY() == 2); // verificando se nao é a posição do proprio ladrao
		}

		return new Node(posicaoObjetivo.getX(), posicaoObjetivo.getY());
	}
	public int Manhattan(Coordenada a, Coordenada b){ // calcular distancia

		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}

	public ArrayList<Elemento> melhorDirecao(HashMap<String, ArrayList<Elemento>> direcoes){

		int bigger = calcularPeso(direcoes.get("cima"), direcoes.get("olfatoCimaPoupador"), direcoes.get("olfatoCimaLadrao"));
		int index = 0;
		int peso;

		for(int i = 1; i < 4; i++) {
			if(i == 1) {
				peso = calcularPeso(direcoes.get("baixo"), direcoes.get("olfatoBaixoPoupador"), direcoes.get("olfatoBaixoLadrao"));
			} else if(i == 2) {
				peso = calcularPeso(direcoes.get("direita"), direcoes.get("olfatoDireitaPoupador"), direcoes.get("olfatoDireitaLadrao"));
			} else {
				peso = calcularPeso(direcoes.get("esquerda"), direcoes.get("olfatoEsquerdaPoupador"), direcoes.get("olfatoEsquerdaLadrao"));
			}

			if(bigger < peso) {
				bigger = peso;
				index = i;
			}
		}


		if(bigger < 100){ // Se tiver somente rastro na visao
			ArrayList<Elemento> aux = new ArrayList<>();
			aux.add(new Elemento(new Coordenada(99, 99), index)); //coordenada qualquer, pq o importante é só o valor
			return aux;
			// quando retornar uma lista com somente 1 posicao, quer dizer que o valor dessa posicao vai ser o movimento a ser feito
			//0 -> cima, 1 -> baixo, 2 -> direita, 3 -> esquerda
		}

		if(index == 0){
			System.out.println("melhor direção é a para cima. Peso -> " + bigger);
			return direcoes.get("cima");
		}
		else if(index == 1){
			System.out.println("melhor direção é a para baixo. Peso -> " + bigger);
			return direcoes.get("baixo");
		}
		else if(index == 2){
			System.out.println("melhor direção é a para direita. Peso -> " + bigger);
			return direcoes.get("direita");
		}
		else{
			System.out.println("melhor direção é a para esquerda. Peso -> " + bigger);
			return direcoes.get("esquerda");
		}
	}

	public int calcularPeso(ArrayList<Elemento> direcao, ArrayList<Elemento> olfatoPoupador, ArrayList<Elemento> olfatoLadrao){
		// peso poupador -> 200
		// peso rastro poupador -> 15
		// peso ladrao -> 10
		// peso rastro ladrao -> 1

		int contador = 0;

		for (Elemento elemento : direcao) {
			if(elemento.getValor() >= 100 &&  elemento.getValor() < 200){ // procurando poupador
				contador+=200;
			}
			else if(elemento.getValor() >= 200){ // procurando ladrao
				contador+=10;
			}
		}

		if(contador == 0){ // caso nao tenha poupadores ou ladroes, vamos verificar se tem rastros
			for (Elemento elemento : olfatoPoupador) {
				if(elemento.getValor() >= 1 &&  elemento.getValor() < 5){
					contador+=15;
					break;
				}
			}
			/*for (Elemento elemento : olfatoLadrao) {
				if(elemento.getValor() >= 1 &&  elemento.getValor() < 5){
					contador+=1;
					break;
				}
			}*/

			if(contador == 0){
				for (Elemento elemento : direcao) {
					if(elemento.getValor() == -1 ||  elemento.getValor() == -2 || elemento.getValor() == 1 ){
						contador-=1;
					}
				}
			}
		}

		return contador;
	}
	public HashMap<String, ArrayList<Elemento>> direcoes(int[][] visao) {
		// Cima, baixo, direita, esquerda
		//  0      1       2         3
		HashMap<String, ArrayList<Elemento>> direcoes = new HashMap<>();

		ArrayList<Elemento> cima = new ArrayList<Elemento>();
		ArrayList<Elemento> baixo = new ArrayList<Elemento>();
		ArrayList<Elemento> esquerda = new ArrayList<Elemento>();
		ArrayList<Elemento> direita = new ArrayList<Elemento>();

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				cima.add(new Elemento(new Coordenada(i, j), visao[i][j]));
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				esquerda.add(new Elemento(new Coordenada(i, j), visao[i][j]));
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 3; j < 5; j++) {
				direita.add(new Elemento(new Coordenada(i, j), visao[i][j]));
			}
		}
		for (int i = 3; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				baixo.add(new Elemento(new Coordenada(i, j), visao[i][j]));
			}
		}

		ArrayList<Elemento> olfatoCimaPoupador = new ArrayList<Elemento>();
		ArrayList<Elemento> olfatoBaixoPoupador = new ArrayList<Elemento>();
		ArrayList<Elemento> olfatoEsquerdaPoupador = new ArrayList<Elemento>();
		ArrayList<Elemento> olfatoDireitaPoupador = new ArrayList<Elemento>();

		ArrayList<Elemento> olfatoCimaLadrao = new ArrayList<Elemento>();
		ArrayList<Elemento> olfatoBaixoLadrao = new ArrayList<Elemento>();
		ArrayList<Elemento> olfatoEsquerdaLadrao = new ArrayList<Elemento>();
		ArrayList<Elemento> olfatoDireitaLadrao = new ArrayList<Elemento>();

		int[][] olfatoPoupador = getOlfatoPoupador();
		int[][] olfatoLadrao = getOlfatoLadrao();

		for (int i = 0; i < olfatoPoupador.length; i++) {
			for (int j = 0; j < olfatoPoupador[i].length; j++) {
				// Cima e Baixo compartilham a mesma coluna (j = 1)
				if (i == 0) {
					olfatoCimaPoupador.add(new Elemento(new Coordenada(i, j), olfatoPoupador[i][j]));
				} else if (i == 2) {
					olfatoBaixoPoupador.add(new Elemento(new Coordenada(i, j), olfatoPoupador[i][j]));
				}

				// Esquerda e Direita compartilham a mesma linha (i = 1)
				if (j == 0) {
					olfatoEsquerdaPoupador.add(new Elemento(new Coordenada(i, j), olfatoPoupador[i][j]));
				} else if (j == 2) {
					olfatoDireitaPoupador.add(new Elemento(new Coordenada(i, j), olfatoPoupador[i][j]));
				}
			}
		}

		for (int i = 0; i < olfatoLadrao.length; i++) {
			for (int j = 0; j < olfatoLadrao[i].length; j++) {
				if (i == 0) {
					olfatoCimaLadrao.add(new Elemento(new Coordenada(i, j), olfatoLadrao[i][j]));
				} else if (i == 2) {
					olfatoBaixoLadrao.add(new Elemento(new Coordenada(i, j), olfatoLadrao[i][j]));
				}

				if (j == 0) {
					olfatoEsquerdaLadrao.add(new Elemento(new Coordenada(i, j), olfatoLadrao[i][j]));
				} else if (j == 2) {
					olfatoDireitaLadrao.add(new Elemento(new Coordenada(i, j), olfatoLadrao[i][j]));
				}
			}
		}


		direcoes.put("cima", cima);
		direcoes.put("baixo", baixo);
		direcoes.put("direita", direita);
		direcoes.put("esquerda", esquerda);

		direcoes.put("olfatoCimaLadrao", olfatoCimaLadrao);
		direcoes.put("olfatoBaixoLadrao", olfatoBaixoLadrao);
		direcoes.put("olfatoEsquerdaLadrao", olfatoEsquerdaLadrao);
		direcoes.put("olfatoDireitaLadrao", olfatoDireitaLadrao);

		direcoes.put("olfatoCimaPoupador", olfatoCimaPoupador);
		direcoes.put("olfatoBaixoPoupador", olfatoBaixoPoupador);
		direcoes.put("olfatoEsquerdaPoupador", olfatoEsquerdaPoupador);
		direcoes.put("olfatoDireitaPoupador", olfatoDireitaPoupador);


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
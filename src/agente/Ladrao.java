package agente;
import algoritmo.ProgramaLadrao;
import java.util.*;

<<<<<<< HEAD
import static agente.Ladrao.AStar.aStar;
=======
>>>>>>> bugfix

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

<<<<<<< HEAD
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
=======
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
>>>>>>> bugfix

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

	List<Node> caminho;
	private LinkedList<double[]> ultimasPosicoes = new LinkedList<>();
	int[] chaveAux = {-100};
	double coluna, linha;
	int moedas = 0;

	private Timer timer;
	private final Object lock = new Object();
	TimerTask task;

	Boolean assaltoLiberado = true;
	Boolean poupadorNaMira = false;

	public double[] coordenadasRelativasParaAbsolutas(double[] coordenadasRelativas, double coluna, double linha) {
		double linhaAbsoluta = sensor.getPosicao().getY() - 2 + coordenadasRelativas[0];
		double colunaAbsoluta = sensor.getPosicao().getX() - 2 + coordenadasRelativas[1];
		return new double[]{colunaAbsoluta, linhaAbsoluta};
	}

	public int acao() {
		double[] posicaoAtual = {sensor.getPosicao().getX(), sensor.getPosicao().getY()};

		ultimasPosicoes.add(posicaoAtual);

		if (ultimasPosicoes.size() > 10) {
			ultimasPosicoes.removeFirst();
		}
		return andar();
	}

	public int andar() {
<<<<<<< HEAD
		if(sensor.getPosicao().getX() == 0 && sensor.getPosicao().getY() == 0 || sensor.getPosicao().getX() == 0 && sensor.getPosicao().getY() == 29 || sensor.getPosicao().getX() == 29 && sensor.getPosicao().getY() == 0){
			return 0; //fazendo os outros ladroes nao serem executados para nao atrapalhar no debug
=======
		if(caminho != null && caminho.isEmpty() && poupadorNaMira){
			poupadorNaMira = false;
			temporizadorAssalto();
>>>>>>> bugfix
		}

		if (caminho == null || caminho.isEmpty()) {
			int[][] visao = getVisao();
<<<<<<< HEAD
			caminho = aStar(visao, new Node(2, 2), heuristica(visao));

			if (caminho != null) {
				int removido = caminho.remove(0).acao; //tirando a primeira posicao, pois sempre a ação do primeiro nó é = 0 (ficar parado)
				return caminho.remove(0).acao;
=======
			Node objetivo = heuristica(visao);

			if(objetivo != null){
				caminho = AStar.aStar(visao, new Node(2, 2), objetivo);
				if (caminho != null) {
					int aux = caminho.remove(0).acao;
					return caminho.remove(0).acao;
				}
>>>>>>> bugfix
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

		double[] coordenadaProxima = coordenadasRelativasParaAbsolutas(coordenadasRelativas, coluna, linha);

		for (double[] posicao : ultimasPosicoes) {
			if (Arrays.equals(posicao, coordenadaProxima)) {
				return true;
			}
		}
		return false;
	}

	public void temporizadorAssalto(){
		double[] posicaoAnterior = new double[2];

		if(!ultimasPosicoes.isEmpty()){
			posicaoAnterior = ultimasPosicoes.getLast();
		}
		double[] posicaoAtual = {sensor.getPosicao().getX(), sensor.getPosicao().getY()};

		synchronized(lock) {

			if(moedas < sensor.getNumeroDeMoedas() && (task == null || task.scheduledExecutionTime() == 0) || posicaoAnterior[0] == posicaoAtual[0] && posicaoAnterior[1] == posicaoAtual[1]) {
				if (task != null) {
					task.cancel();
				}
				task = new TimerTask() {
					@Override
					public void run() {
						synchronized(lock) {
							System.out.println("Poupador assaltado!");
							assaltoLiberado = true;
							task = null;
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
	}
	public Node heuristica(int[][] visao){
		ArrayList<Elemento> melhorDirecao = melhorDirecao(direcoes(visao));

<<<<<<< HEAD
		ArrayList<Coordenada> posicoes = new ArrayList<>();

		//se o tamanho da lista(melhorDirecao) for igual a 1, o valor que está nessa posicao significa o movimento a ser feito
		if(melhorDirecao.size() == 1){
			switch (melhorDirecao.get(0).getValor()){
				case 0: //cima
=======
		if(melhorDirecao.containsKey(chaveAux)){
			switch (melhorDirecao.get(chaveAux)){
				case 1:
>>>>>>> bugfix
					return new Node(1, 2);
				case 1: // baixo
					return new Node(3, 2);
				case 2: // direita
					return new Node(2, 3);
				case 3: // esquerda
					return new Node(2, 1);
			}
		}

<<<<<<< HEAD
		for (Elemento elemento : melhorDirecao) {
			if(elemento.getValor() >= 100 && elemento.getValor() < 200){ // verificando se tem poupadores
				posicoes.add(elemento.getPosicao());
			}
		}

		Coordenada posicaoObjetivo = new Coordenada(2, 2); // valores qualquer para inicializar
=======
		ArrayList<int[]> posicoes = new ArrayList<>(); // posicoes dos elementos encontrados na melhorDirecao

		for (Map.Entry<int[], Integer> entry : melhorDirecao.entrySet()) {
			int[] key = entry.getKey();
			int value = entry.getValue();
			if(value >= 100 && value < 200){ // verificando se tem poupador
				posicoes.add(key);
			}
		}

		int[] posicaoObjetivo;
>>>>>>> bugfix

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
<<<<<<< HEAD
		}
		else{
			do {
				posicaoObjetivo.x = (int) (Math.random() * 5);
				posicaoObjetivo.y = (int) (Math.random() * 5);
			} while (posicaoObjetivo.getX() == 2 && posicaoObjetivo.getY() == 2); // verificando se nao é a posição do proprio ladrao
		}

		return new Node(posicaoObjetivo.getX(), posicaoObjetivo.getY());
=======

			return new Node(posicaoObjetivo[0], posicaoObjetivo[1]);
		}
		return null;
>>>>>>> bugfix
	}
	public int Manhattan(Coordenada a, Coordenada b){ // calcular distancia

		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}

	public ArrayList<Elemento> melhorDirecao(HashMap<String, ArrayList<Elemento>> direcoes){

		int bigger = calcularPeso(direcoes.get("cima"), direcoes.get("olfatoCimaPoupador"), direcoes.get("olfatoCimaLadrao"));
		int index = 0;
		int peso;

<<<<<<< HEAD
		for(int i = 1; i < 4; i++) {
			if(i == 1) {
				peso = calcularPeso(direcoes.get("baixo"), direcoes.get("olfatoBaixoPoupador"), direcoes.get("olfatoBaixoLadrao"));
			} else if(i == 2) {
				peso = calcularPeso(direcoes.get("direita"), direcoes.get("olfatoDireitaPoupador"), direcoes.get("olfatoDireitaLadrao"));
			} else {
				peso = calcularPeso(direcoes.get("esquerda"), direcoes.get("olfatoEsquerdaPoupador"), direcoes.get("olfatoEsquerdaLadrao"));
			}

			if(bigger < peso) {
=======
		for(int i = 1; i<4; i++){
			peso = calcularPeso(direcoes.get(i), direcoes.get(i+4), direcoes.get(i+8));

			if(bigger < peso){
>>>>>>> bugfix
				bigger = peso;
				index = i;
			}
		}


		if(bigger < 100){ // Se tiver somente rastro na visao
<<<<<<< HEAD
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
=======
			HashMap<int[], Integer> aux = new HashMap<int[], Integer>();
			aux.put(chaveAux, index + 1);
			return aux;
			// colocando -100 na chave para saber que o hashmap de retorno não é com valores da visao
			// Sendo o seu valor o movimento a ser feito. (valores de 1 a 4)
		}

		return direcoes.get(index);
>>>>>>> bugfix
	}

	public int calcularPeso(ArrayList<Elemento> direcao, ArrayList<Elemento> olfatoPoupador, ArrayList<Elemento> olfatoLadrao){
		// peso poupador -> 200
		// peso rastro poupador -> 15
		// peso rastro ladrao -> 1

		int contador = 0;

<<<<<<< HEAD
		for (Elemento elemento : direcao) {
			if(elemento.getValor() >= 100 &&  elemento.getValor() < 200){ // procurando poupador
				contador+=200;
			}
			else if(elemento.getValor() >= 200){ // procurando ladrao
				contador+=10;
=======
		if(assaltoLiberado){
			for(int i = 0; i<=10; i+=10){
				if(direcao.containsValue(100 + i)){ //contando quant. poupadores
					contador+=200;
					poupadorNaMira = true;
				}
>>>>>>> bugfix
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

<<<<<<< HEAD
			if(contador == 0){
				for (Elemento elemento : direcao) {
					if(elemento.getValor() == -1 ||  elemento.getValor() == -2 || elemento.getValor() == 1 ){
						contador-=1;
					}
				}
			}
=======
			int valor;
			for (Map.Entry<int[], Integer> elementosDaDirecao : direcao.entrySet()) { //verifica obstaculos
				valor = elementosDaDirecao.getValue();
				if (valor == -2 || valor == 1 || valor == 3 || valor == 4 || valor == 5) {
					contador--;
				}
				else if (valor == -1){
					contador -=5;
				}

				double[] valoresInteiros = new double[elementosDaDirecao.getKey().length]; // transformando em double para comparar
				for (int i = 0; i < elementosDaDirecao.getKey().length; i++) {
					valoresInteiros[i] = elementosDaDirecao.getKey()[i];
				}

				double[] coordenadaReal = coordenadasRelativasParaAbsolutas(valoresInteiros, coluna, linha);
				for (double[] posicao : ultimasPosicoes) {
					if (Arrays.equals(posicao, coordenadaReal)) {
						contador -= 2; // Subtrai um valor se a posição estiver nas últimas 5 posições.
					}
				}
			}

>>>>>>> bugfix
		}

		return contador;
	}
<<<<<<< HEAD
	public HashMap<String, ArrayList<Elemento>> direcoes(int[][] visao) {
=======


	public ArrayList<HashMap<int[], Integer>> direcoes(int[][] visao) {
>>>>>>> bugfix
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
<<<<<<< HEAD
=======

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

>>>>>>> bugfix
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
}
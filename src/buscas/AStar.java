package buscas;

import java.util.*;

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
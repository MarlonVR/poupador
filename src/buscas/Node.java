package buscas;

import java.util.Objects;

public class Node {
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

package buscas;

import java.util.Objects;

public class Node {
    public int row, col;  // Coordenadas do nó no grafo
    int g;     // Custo real do início até este nó
    int h;     // Estimativa heurística do custo deste nó até o destino
    Node parent;  // Nó pai no caminho
    public int act;
    public Node(int x, int y) {
        this.row = x;
        this.col = y;
    }

    public int f() {
        return g + h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return row == node.row && col == node.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

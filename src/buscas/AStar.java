package buscas;

import java.util.*;

public class AStar {
    public static List<Node> aStar(int[][] grid, Node start, Node goal) {
        Set<Node> openSet = new HashSet<>();
        Set<Node> closedSet = new HashSet<>();
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = null;
            for (Node node : openSet) {
                if (current == null || node.f() < current.f()) {
                    current = node;
                }
            }

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Node neighbor : getNeighbors(current, grid)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeG = current.g + 1;  // Distância entre nós é 1
                // custo do nó atual para o vizinho

                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(neighbor, goal);
                    openSet.add(neighbor);
                }
            }
        }

        return null;  // Caminho não encontrado
    }

    public static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static int heuristic(Node a, Node b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);  // Heurística de Manhattan
    }

    public static List<Node> getNeighbors(Node node, int[][] grid) {
        List<Node> neighbors = new ArrayList<>();
        int rows = grid.length;
        int cols = grid[0].length;
        int x = node.row;
        int y = node.col;

        if (x > 0 && grid[x - 1][y] == 0 || x > 0 && grid[x - 1][y] >= 100 && grid[x - 1][y] < 200) { // cima
            neighbors.add(new Node(x - 1, y));
            neighbors.get(neighbors.size()-1).act = 1;
        }
        if (x < rows - 1 && grid[x + 1][y] == 0 || x < rows - 1 && grid[x + 1][y] >= 100 && grid[x + 1][y] < 200) { // baixo
            neighbors.add(new Node(x + 1, y));
            neighbors.get(neighbors.size()-1).act = 2;
        }
        if (y > 0 && grid[x][y - 1] == 0 || y > 0 && grid[x][y - 1] >= 100 && grid[x][y - 1] < 200) { // esquerda
            neighbors.add(new Node(x, y - 1));
            neighbors.get(neighbors.size()-1).act = 4;
        }
        if (y < cols - 1 && grid[x][y + 1] == 0 || y < cols - 1 && grid[x][y + 1] >= 100 && grid[x][y + 1] < 200) { // direita
            neighbors.add(new Node(x, y + 1));
            neighbors.get(neighbors.size()-1).act = 3;
        }

        return neighbors;
    }
}
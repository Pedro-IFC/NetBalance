package interpreter;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AnimateFromFile {

    public static void main(String[] args) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("historico_individuos.json")));
        JSONArray individuos = new JSONArray(content);

        Path pastaGrafos = Paths.get("grafos");
        if (!Files.exists(pastaGrafos)) {
            Files.createDirectory(pastaGrafos);
        }

        for (int indice = 0; indice < individuos.length(); indice++) {
            JSONObject individuo = individuos.getJSONObject(indice);
            JSONArray matrizJson = individuo.getJSONArray("grafo");

            int n = matrizJson.length();
            double[][] grafo = new double[n][];
            for (int i = 0; i < n; i++) {
                JSONArray linha = matrizJson.getJSONArray(i);
                grafo[i] = new double[linha.length()];
                for (int j = 0; j < linha.length(); j++) {
                    grafo[i][j] = linha.getDouble(j);
                }
            }

            DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> g =
                    new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

            for (int i = 0; i < n; i++) {
                g.addVertex("V" + i);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < grafo[i].length; j++) {
                    if (i != j && grafo[i][j] != 0.0) {
                        DefaultWeightedEdge e = g.addEdge("V" + i, "V" + j);
                        g.setEdgeWeight(e, grafo[i][j]);
                    }
                }
            }

            DOTExporter<String, DefaultWeightedEdge> exporter =
                    new DOTExporter<>(v -> v);

            exporter.setEdgeAttributeProvider(edge -> {
                Map<String, Attribute> map = new HashMap<>();
                map.put("label", DefaultAttribute.createAttribute(String.format("%.2f", g.getEdgeWeight(edge))));
                return map;
            });

            String nomeArquivo = "grafo_" + indice + ".dot";
            try (FileWriter writer = new FileWriter(pastaGrafos.resolve(nomeArquivo).toFile())) {
                exporter.exportGraph(g, writer);
            }

            System.out.println("Exportado: " + nomeArquivo);
        }

        System.out.println("Todos os grafos foram exportados para a pasta 'grafos'.");
    }
}

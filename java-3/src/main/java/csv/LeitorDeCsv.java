package csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeitorDeCsv {

    private static final String SEPARADOR = ",";

    private CabecalhoCsv cabecalhos;
    private LinhasCsv linhas;

    public LeitorDeCsv(String caminhoArquivo) {
        lerArquivo(caminhoArquivo);
    }

    private void lerArquivo(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream stream = classLoader.getResourceAsStream(fileName)) {

            if (stream == null) throw new FileNotFoundException();

            try (Scanner scanner = new Scanner(stream)) {

                // Lê cabeçalho do CSV
                this.cabecalhos = new CabecalhoCsv(scanner.nextLine().split(SEPARADOR));
                this.linhas = new LinhasCsv();

                // Lê linhas do CSV
                while (scanner.hasNextLine()) {
                    linhas.adiciona(scanner.nextLine().split(SEPARADOR));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int contarUnicos(String cabecalho) {
        int indice = this.cabecalhos.indiceDo(cabecalho);

        if (indice < 0) return 0;

        return (int) linhas.pegaColuna(indice)
                .filter(value -> !value.isEmpty())
                .distinct()
                .count();
    }

    public List<String> lerColuna(String cabecalho, int limite) {
        int indice = this.cabecalhos.indiceDo(cabecalho);

        if (indice >= 0) {

            return linhas.pegaColuna(indice)
                    .limit(limite)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<String> lerColunaOrdenandoReverse(String cabecalho, int limite, String... cabecalhosOrdenar) {

        int index = cabecalhos.indiceDo(cabecalho);
        List<Integer> indices = cabecalhos.indicesDos(cabecalhosOrdenar);
        indices.add(0, index); // adiciona cabeçalho principal ao inicio da lista.

        if (index >= 0) {
            return linhas
                    .pegaColunas(indices)
                    .filter(strings -> !strings[1].isEmpty())
                    .sorted(Comparator.<String[], Double>comparing(o -> Double.parseDouble(o[1])).reversed())
                    .map(strings -> strings[0])
                    .limit(limite)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();

    }

    public List<String> lerColunaOrdenando(String cabecalho, String secondSortHeader, String thirdSortHeader,
                                           int limit) {
//        int headerIndex = cabecalhos.indiceDo(cabecalho);
//        int secondSortIndex = cabecalhos.indiceDo(secondSortHeader);
//        int thirdSortIndex = cabecalhos.indiceDo(thirdSortHeader);

        List<Integer> indices = cabecalhos.indicesDos(cabecalho, secondSortHeader, thirdSortHeader);

        if (!indices.isEmpty()) {
            return linhas
                    .pegaColunas(indices)
                    .sorted(Comparator.<String[], String>comparing(strings -> strings[1]).thenComparing(o -> o[2]))
                    .limit(limit)
                    .map(strings -> strings[0])
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Map<Integer, Integer> contarAgregandoPor(String cabecalho) {
        int indiceCabecalho = cabecalhos.indiceDo(cabecalho);

        if (indiceCabecalho >= 0) {
            return linhas.pegaColuna(indiceCabecalho)
                    .map(Integer::parseInt)
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            Collectors.reducing(0, e -> 1, Integer::sum)
                    ));
        }

        return Collections.emptyMap();
    }

    public Stream<String[]> lerColunas(String... cabecalhos) {
        return linhas.pegaColunas(this.cabecalhos.indicesDos(cabecalhos));
    }
}

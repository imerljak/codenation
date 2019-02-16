package csv;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class LinhasCsv {

    private final List<String[]> linhas = new ArrayList<>();

    public void adiciona(String[] linha) {
        linhas.add(linha);
    }

    public Stream<String> pegaColuna(int indice) {
        return linhas.stream()
                .map(s -> s[indice]);
    }

    public Stream<String[]> pegaColunas(List<Integer> indices) {

        Function<String[], String[]> mapColunas = getMapColunas(indices);

        return linhas.stream()
                .map(mapColunas);
    }

    public Stream<String[]> stream() {
        return linhas.stream();
    }

    private Function<String[], String[]> getMapColunas(List<Integer> indices) {
        return strings -> {
            List<String> colunas = new ArrayList<>();

            for (Integer index : indices) {
                colunas.add(strings[index]);
            }

            return colunas.toArray(new String[]{});
        };
    }

}

package csv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CabecalhoCsv {

    private final List<String> valores;

    CabecalhoCsv(String[] valores) {
        this.valores = Arrays.asList(valores);
    }

    /**
     * Retorna o indice do cabeçalho pesquisado. Caso não encontrar, retorna -1;
     *
     * @param cabecalho valor do cabeçalho
     * @return indice
     */
    public int indiceDo(String cabecalho) {
        for (int i = 0; i < valores.size(); i++) {
            if (valores.get(i).equalsIgnoreCase(cabecalho)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Retorna um int[] contendo os indices de todas as colunas informadas.
     *
     * @param cabecalhosOrdenar cabecalhos
     * @return array de indices
     */
    public List<Integer> indicesDos(String... cabecalhosOrdenar) {
        return Stream.of(cabecalhosOrdenar)
                .map(this::indiceDo)
                .collect(Collectors.toList());
    }
}

package challenge;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReader {

    private static final String CSV_SEPARATOR = ",";

    private String headers[];
    private List<String[]> lines = new ArrayList<>();

    private final String csvFile;

    public CsvReader(String csvFile) {
        this.csvFile = csvFile;

        readFile(this.csvFile);
    }

    private void readFile(String fileName) {

        final ClassLoader classLoader = getClass().getClassLoader();

        try (Scanner scanner = new Scanner(classLoader.getResourceAsStream(fileName))) {

            this.headers = scanner.nextLine().split(CSV_SEPARATOR);

            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine().split(CSV_SEPARATOR));
            }

        }
    }


    public int countUnique(String headerName) {
        int index = headerIndex(headerName);

        if (index >= 0) {
            Set<String> uniqueSet = new HashSet<>();

            lines.stream()
                    .filter(strings -> !strings[index].isEmpty())
                    .forEach(strings -> uniqueSet.add(strings[index]));

            return uniqueSet.size();
        }

        return 0;
    }

    private int headerIndex(String headerName) {
        for (int i = headers.length - 1; i >= 0; i--) {
            if (headers[i].equalsIgnoreCase(headerName)) {
                return i;
            }
        }

        return -1;
    }

    public List<String> readCols(String headerName, int limit) {
        int index = headerIndex(headerName);

        if (index >= 0) {

            return lines.stream()
                    .map(strings -> strings[index])
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<String> readColsSorted(String headerName, String sortHeader, int limit) {

        int index = headerIndex(headerName);
        int sortHeaderIndex = headerIndex(sortHeader);

        if (index >= 0 && sortHeaderIndex >= 0) {
            return lines.stream()
                    .map(strings -> new String[]{strings[index], strings[sortHeaderIndex]})
                    .filter(strings -> !strings[1].isEmpty())
                    .sorted(Comparator.<String[], Double>comparing(o -> Double.parseDouble(o[1])).reversed())
                    .map(strings -> strings[0])
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();

    }

    public List<String> readColsBiSorted(String headerName, String secondSortHeader, String thirdSortHeader, int limit) {
        int headerIndex = headerIndex(headerName);
        int secondSortIndex = headerIndex(secondSortHeader);
        int thirdSortIndex = headerIndex(thirdSortHeader);

        if (headerIndex >= 0 && secondSortIndex >= 0 && thirdSortIndex >= 0) {
            return lines.stream()
                    .map(strings -> new String[]{strings[headerIndex], strings[secondSortIndex], strings[thirdSortIndex]})
                    .sorted(Comparator.<String[], String>comparing(strings -> strings[1]).thenComparing(o -> o[2]))
                    .limit(limit)
                    .map(strings -> strings[0])
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Map<Integer, Integer> countMapBy(String headerName) {
        int headerIndex = headerIndex(headerName);

        if (headerIndex >= 0) {
            return lines.stream()
                    .map(strings -> strings[headerIndex])
                    .map(Integer::parseInt)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0, e -> 1, Integer::sum)));
        }

        return Collections.emptyMap();
    }
}

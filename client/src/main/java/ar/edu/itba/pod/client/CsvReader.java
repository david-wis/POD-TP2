package ar.edu.itba.pod.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvReader {
    public static <T> void readFile(String path, Function<String[], T> lineConverter, Consumer<T> lineConsumer) throws IOException {
        try (Stream<String> lineStream = Files.lines(Path.of(path))) {
            lineStream
                .skip(1) // Skip header line
                .map(s -> s.split(";"))
                .map(lineConverter)
                .forEach(lineConsumer);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

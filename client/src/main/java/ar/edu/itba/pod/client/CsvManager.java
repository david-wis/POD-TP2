package ar.edu.itba.pod.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvManager {
    public static <T> void readFile(Path path, Function<String[], T> lineConverter, Consumer<T> lineConsumer) throws IOException {
        try (Stream<String> lineStream = Files.lines(path)) {
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


    public static void writeLines(Path path, Stream<String> lines) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            lines.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Error writing line to file: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            System.err.println("Error writing lines to file: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error while writing lines: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

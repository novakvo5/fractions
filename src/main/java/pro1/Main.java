package pro1;

import java.io.*;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {

        Path inputDir = Paths.get("input");
        Path outputDir = Paths.get("output");

        try {

            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            DirectoryStream<Path> files = Files.newDirectoryStream(inputDir, "*.csv");

            for (Path file : files) {

                BufferedReader reader = Files.newBufferedReader(file);
                BufferedWriter writer = Files.newBufferedWriter(
                        outputDir.resolve(file.getFileName())
                );

                String line;

                while ((line = reader.readLine()) != null) {

                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split("\\s*[;,|:=]\\s*", 2);
                    if (parts.length < 2) continue;

                    String name = parts[0].trim();
                    String expression = parts[1].trim();

                    String[] elements = expression.split("\\s*\\+\\s*");

                    int numerator = 0;
                    int denominator = 1;

                    for (String el : elements) {

                        el = el.trim();

                        int n;
                        int d;

                        if (el.contains("%")) {

                            el = el.replace("%", "").trim();

                            n = Integer.parseInt(el);
                            d = 100;

                        } else {

                            String[] frac = el.split("/");

                            n = Integer.parseInt(frac[0].trim());
                            d = Integer.parseInt(frac[1].trim());
                        }

                        numerator = numerator * d + n * denominator;
                        denominator = denominator * d;
                    }

                    int g = gcd(numerator, denominator);

                    numerator /= g;
                    denominator /= g;

                    writer.write(name + "," + numerator + "/" + denominator);
                    writer.newLine();
                }

                reader.close();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int gcd(int a, int b) {

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }
}
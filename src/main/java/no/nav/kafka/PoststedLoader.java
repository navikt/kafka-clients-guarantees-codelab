package no.nav.kafka;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

class PoststedLoader {

    static Set<Poststed> hentPoststeder() {
        final String MESSAGE = "Klarte ikke å lese Postnummerregister-ansi.txt";
        //Get file from resources folder
        ClassLoader classLoader = PoststedLoader.class.getClassLoader();
        Optional<URL> fraRessurs = Optional.ofNullable(classLoader.getResource("data/Postnummerregister-ansi.txt"));
        File file = fraRessurs.map(url -> new File(url.getFile())).orElseThrow(() -> new RuntimeException(MESSAGE));
        Set<Poststed> poststeder = new HashSet<>();

        try (Scanner scanner = new Scanner(file, StandardCharsets.ISO_8859_1)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] splitted = line.split("\t");
                poststeder.add(
                    new Poststed(splitted[0], splitted[1], splitted[2])
                );

            }


        } catch (IOException e) {
            throw new RuntimeException(MESSAGE, e);
        }

        return poststeder;

    }
}

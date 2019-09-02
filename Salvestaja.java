package sample;

import java.io.*;

public class Salvestaja {

    public void salvestaMängija(String failinimi, String mängija) throws IOException {
        try (FileWriter fw = new FileWriter(failinimi,true)) {
            fw.write(mängija + "\n");
        }
    }
}

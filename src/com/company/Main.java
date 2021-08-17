package com.company;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String[] phrases = new String[] {"pizza and wings", "batman and robin", "tom and jerry", "drinking coffee", "cats and dogs"};
        ProcessAsync processAsync = new ProcessAsync(phrases);
        ProcessSync processSync = new ProcessSync(phrases);

        processAsync.run();
        processSync.run();
    }
}

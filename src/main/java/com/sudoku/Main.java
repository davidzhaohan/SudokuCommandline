package com.sudoku;

import com.sudoku.generator.PuzzleGenerator;
import com.sudoku.ui.ConsoleApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

/** Entry point: wires the puzzle generator to the console and starts playing. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Random random = new SecureRandom();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PuzzleGenerator generator = new PuzzleGenerator(PuzzleGenerator.DEFAULT_CLUES, random);
        new ConsoleApp(generator, random, in, System.out).run();
    }
}

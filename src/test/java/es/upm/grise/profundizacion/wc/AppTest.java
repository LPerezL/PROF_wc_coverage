package es.upm.grise.profundizacion.wc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AppTest {

    private static Path testFile = Paths.get("ejemplo.txt");

    @BeforeAll
    public static void setup() throws IOException {
        Files.writeString(testFile, "kjdbvws wonvwofjw\n sdnfwijf ooj    kjndfohwouer 21374 vehf\n jgfosj\n\nskfjwoief ewjf\n\n\ndkfgwoihgpw vs wepfjwfin");
    }

    @AfterAll
    public static void teardown() {
        try {
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            System.err.println("Error deleting test file: " + e.getMessage());
            try {
                Thread.sleep(100);
                Files.deleteIfExists(testFile);
            } catch (IOException | InterruptedException ex) {
                System.err.println("Failed to delete test file on retry: " + ex.getMessage());
            }
        }
    }


    @Test
    public void testUsageMessageWhenNoArgs() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] {});
        
        assertEquals("Usage: wc [-clw file]\n".trim(), output.toString().trim());
    }

  


    @Test
    public void testCountCharacters() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"-c", testFile.toString()});

        assertTrue(output.toString().contains(testFile.toString()));
    }



    @Test
    public void testCountCharactersLinesAndWords() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"-clw", testFile.toString()});

        assertTrue(output.toString().contains(testFile.toString()));
    }


    @Test
    public void testWrongNumberOfArguments() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"-c"});

        assertEquals("Wrong arguments!", output.toString().trim());
    }

    @Test
    public void testFileNotFound() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"-c", "no_existe.txt"});

        assertTrue(output.toString().contains("Cannot find file"));
    }

    @Test
    public void testCommandsWithoutDash() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"c", testFile.toString()});

        assertTrue(output.toString().contains("do not start with -"));
    }

    @Test
    public void testUnrecognizedCommand() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"-x", testFile.toString()});

        assertTrue(output.toString().contains("Unrecognized command"));
    }

    @Test
    public void testMainFullExecutionCLW() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        App.main(new String[]{"-clw", testFile.toString()});

        String result = output.toString();
        assertTrue(result.contains(testFile.toString()));
    }




}

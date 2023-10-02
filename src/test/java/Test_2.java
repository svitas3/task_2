import org.example.Ls;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class Test_2 {
    @Test
    void testExecute0() throws IOException {
        Ls ls = new Ls();

        String[] args = new String[]{"-h", "Files/ex4"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("IOException", output.toString());
    }

    @Test
    void testExecute1() throws Exception {
        Ls ls = new Ls();

        String[] args = new String[]{"-l", "Files/ex1"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals(ls.longFormat("ex1"), output.toString());
    }

    @Test
    void testExecute2() throws IOException {
        Ls ls = new Ls();

        String[] args = {"-h", "Files/ex2"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals(ls.humanReadable("ex2"), output.toString());


        args = new String[]{"-l", "gradle/wrapper"};
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("""
                gradle-wrapper.jar 111 2023-09-20 00:55:23 59821
                gradle-wrapper.properties 111 2023-09-20 00:55:23 200
                """, output.toString());
    }

    @Test
    void testExecute3() throws IOException {
        Ls ls = new Ls();

        String[] args = {"src/main/java/org/example"};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        ls.execute(args);
        assertEquals("Ls.java\nMain.java\n", output.toString());
    }
}

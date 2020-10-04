package org.example.basicapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    // Logger Sample: https://oohira.github.io/junit5-doc-jp/user-guide/
    static final Logger LOG = Logger.getLogger(AppTest.class.getName());

    // Full JUnit 5 example to test System.out
    // https://stackoverflow.com/questions/1119385
    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;

    @BeforeEach
    void redirectSystemOutStream() {

        originalSystemOut = System.out;

        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
    }

    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalSystemOut);
    }

    @Test
    public void testApp() {

        String[] args = { "hoge", "fuga", "hoge.txt", "s3://bucket/hoge.txt" };
        App.main(args);

        var expected = "第一引数には upload か download を指定してください。\n";
        var actual = systemOutContent.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testException() {

        String[] args = { "upload", "fuga", "hoge.txt", "s3://bucket/hoge.txt" };
        App.main(args);

        var expected = "失敗しました：";
        var actual = systemOutContent.toString();

        LOG.info(actual);

        assertTrue(actual.startsWith(expected));
    }
}

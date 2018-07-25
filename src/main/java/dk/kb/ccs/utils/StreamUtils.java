package dk.kb.ccs.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import dk.kb.cumulus.utils.ArgumentCheck;

public class StreamUtils {

    /**
     * Extracts the content of an input stream as a string.
     * @param is The input stream to extract.
     * @return The string of the input stream.
     * @throws IOException If the input stream cannot be read.
     */
    public static String extractInputStreamAsString(InputStream is) throws IOException {
        ArgumentCheck.checkNotNull(is, "InputStream is");
        StringBuffer res = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                res.append(line);
                res.append("\n");
            }
        }
        
        return res.toString();
    }
    
    /**
     * Extracts the content of an input stream as lines.
     * @param in The input stream.
     * @return A list of all the lines from the inputstream.
     * @throws IOException If it fails.
     */
    public static List<String> extractInputStreamAsLines(InputStream in) throws IOException {
        ArgumentCheck.checkNotNull(in, "InputStream in");
        List<String> res = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                res.add(line);
            }
        }
        return res;
    }
}

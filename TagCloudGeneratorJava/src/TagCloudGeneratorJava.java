import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Simple HelloWorld program (clear of Checkstyle and FindBugs warnings).
 *
 * @author Mohamed Mohamed and Chris Miklos
 */
public final class TagCloudGeneratorJava {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private TagCloudGeneratorJava() {
        // no code needed here
    }

    /**
     * Arranges strings in descending numerical order
     */
    private static class sortCount
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> count1,
                Map.Entry<String, Integer> count2) {

            return count2.getValue() - count1.getValue();
        }

    }

    /**
     * Arranges strings in alphabetical order
     */
    private static class sortKeys
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> key1,
                Map.Entry<String, Integer> key2) {

            return key1.getKey().toLowerCase()
                    .compareTo(key2.getKey().toLowerCase());
        }

    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        strSet.clear();
        char strPiece = 'i';
        int i = 0;
        while (i < str.length()) {
            strPiece = str.charAt(i);
            if (!strSet.contains(strPiece)) {
                strSet.add(strPiece);
            }
            i++;
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char piece = text.charAt(position);
        String pieceStr = "" + piece;
        int i = 0;
        String result = "";
        if (separators.contains(text.charAt(position))) {

            while (i < text.substring(position).length()
                    && separators.contains(piece)) {
                piece = text.charAt(position + i);
                pieceStr = "" + piece;
                if (separators.contains(piece)) {
                    result = result.concat(pieceStr);
                }
                i++;
            }

        } else {

            while (i < text.substring(position).length()
                    && !(separators.contains(piece))) {
                piece = text.charAt(position + i);
                pieceStr = "" + piece;
                if (!separators.contains(piece)) {
                    result = result.concat(pieceStr);
                }
                i++;
            }

        }

        return result;
    }

    /**
     * generate words from input file and puts them into Qwords
     *
     * @param Queue
     *            words from input file
     * @throws IOException
     * @updates Qwords
     * @requires <pre>
     * input.is_open
     *
     * </pre>
     * @ensures <pre>
     * input.is_open and
     * Qwords has all the words and does have word separators
     * </pre>
     */
    private static Queue<String> wordprocessor(BufferedReader file,
            Queue<String> Qwords, Set<Character> separatorSet)
            throws IOException {

        String line = file.readLine();
        while (line != null) {

            int position = 0;

            while (position < line.length()) {

                String token = nextWordOrSeparator(line, position,
                        separatorSet);

                if (!separatorSet.contains(token.charAt(0))) {
                    Qwords.add(token.toLowerCase());
                }

                position += token.length();
            }

            line = file.readLine();

        }

        return Qwords;
    }

    /**
     * takes words from Queue and prints the on the IndexPage
     *
     * @param record
     *            its a map that holds words from the input map
     * @updates Qwords
     * @requires <pre>
     * input.is_open and
     *
     * </pre>
     * @ensures <pre>
     * record={@code Map}'s words and occurrences
     * and
     * print out into html format]
     * </pre>
     */
    public static void wordSort(Queue<String> Qwords,
            Map<String, Integer> record, PrintWriter out, int numWords) {

        while (Qwords.size() != 0) {

            String word = Qwords.remove();

            if (!record.containsKey(word)) {

                record.put(word, 1);
            } else {

                int tempCount = record.remove(word);
                tempCount++;
                record.put(word, tempCount);

            }
        }

        Map<String, Integer> tempMap = new HashMap<String, Integer>();

        Iterator<Map.Entry<String, Integer>> iter = record.entrySet()
                .iterator();

        while (iter.hasNext()) {
            Map.Entry<String, Integer> tempPair = iter.next();
            iter.remove();

            tempMap.put(tempPair.getKey(), tempPair.getValue());

        }

        record.clear();
        //will hold the min count and max count only
        Queue<Integer> maxAndMin = new LinkedList<Integer>();

        List<Map.Entry<String, Integer>> topNwords = topNwords(maxAndMin,
                tempMap, numWords);

        int max = maxAndMin.poll();
        int min = maxAndMin.poll();

        while (topNwords.size() > 0) {
            Map.Entry<String, Integer> pair = topNwords.remove(0);

            int changeX = max - min;
            int maxFontMinusMin = 48 - 11;
            double font = (37.0 / changeX) * (pair.getValue() - min) + 11;

            out.println("<span style=\"cursor:default\" class=\"f"
                    + Math.round(font) + "\" title=\"count:" + pair.getValue()
                    + "\">" + pair.getKey() + "</span>");

        }
    }

    /**
     * generate top N words from Qwords and puts them into topNwords and takes
     * in a Queue with min and max count
     *
     *
     * @param map
     *            Holds the words and their count
     * @param numWords
     *            how many words to be be put into topNwords
     * @param maxAndMin
     *            it will hold max and min value
     * @replaces maxAndMin
     *
     *           </pre>
     * @ensures <pre>
     * input.is_open and
     * topNwords to have the N words with largest count, and N=numWords
     * </pre>
     */
    private static List<Map.Entry<String, Integer>> topNwords(
            Queue<Integer> maxAndMin, Map<String, Integer> map, int numWords) {

        List<Map.Entry<String, Integer>> sortCount = new ArrayList<>();

        List<Map.Entry<String, Integer>> topNwords = new ArrayList<>();

        for (Iterator<Map.Entry<String, Integer>> iter = map.entrySet()
                .iterator(); iter.hasNext();) {
            Map.Entry<String, Integer> pair = iter.next();
            iter.remove();

            sortCount.add(pair);
        }

        sortCount.sort(new sortCount());

        int size = sortCount.size();
        //topNwords should only add numWords
        for (int i = 0; i < size && i < numWords; i++) {
            Map.Entry<String, Integer> pair2 = sortCount.remove(0);

            if (i == 0) {
                //this must be max
                maxAndMin.add(pair2.getValue());

            } else if (i == numWords - 1) {
                //last element is min
                maxAndMin.add(pair2.getValue());
            }

            topNwords.add(pair2);

        }
        topNwords.sort(new sortKeys());

        return topNwords;
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * <html> <head> <title>Words Counted in data/gettysburg.txt</title> </head>
     * <body>
     * <h2>Words Counter</h2>
     * <hr />
     * <ul>
     *
     *
     * @param out
     *            the output stream
     * @updates out.content
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void outputHeader(PrintWriter out, BufferedReader in,
            int numWords, String fileName) {
        //assert out != null : "Violation of: out is not null";
        //assert ((Object) out).isOpen() : "Violation of: out.is_open";

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words Counted from " + "</title>");
        out.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
        out.println("<body>");
        out.println(
                "<h2>Top " + numWords + " words from " + fileName + "</h2>");
        out.println("   <hr/>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");

    }

    /**
     * Outputs the "closing" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * </table>
     * </body> </html>
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "closing" tags]
     */
    private static void outputFooter(PrintWriter out) {

        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Enter the input file:");
        String inputFile = in.readLine();
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(inputFile));
        } catch (IOException errorMsg) {
            System.err.println("Error finding the input file: " + errorMsg);
            return;
        }

        System.out.println(
                "Enter a (postive) number of words to be included in the generated tag cloud");
        int numWords = Integer.parseInt(in.readLine());
        while (numWords <= 0) {
            System.out.println(
                    "Enter a (postive) number of words to be included in the generated tag cloud");
            numWords = Integer.parseInt(in.readLine());
        }

        System.out.println("Enter the output folder");
        String outputFile = "";
        try {
            outputFile = in.readLine();
        } catch (IOException errorMsg) {
            System.err.println("Error reading the output folder: " + errorMsg);
            return;
        }

        PrintWriter indexPage = new PrintWriter(
                new BufferedWriter(new FileWriter(outputFile)));

        final String separatorStr = " \t, -- .~!@#$%^&*()_+`=-[] {}|'/\":;<>?.,";

        Set<Character> separatorSet = new HashSet<>();
        generateElements(separatorStr, separatorSet);
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        Queue<String> Qwords = new LinkedList<String>();
        wordprocessor(file, Qwords, separatorSet);

        outputHeader(indexPage, file, numWords, inputFile);
        wordSort(Qwords, myMap, indexPage, numWords);
        outputFooter(indexPage);
        file.close();
        in.close();

        indexPage.close();
    }

}

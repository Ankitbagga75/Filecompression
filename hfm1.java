import java.util.*;
import java.io.*;

public class hfm1 {
    static PriorityQueue<Huffnode> q;
    static HashMap<Character, String> newmap = new HashMap<>();
    static String encoded = "";
    static String decoded = "";

    static class Huffnode {
        int count;
        char c;
        String code = "";
        Huffnode left;
        Huffnode right;
        Huffnode parent;

    }

    static class Comp implements Comparator<Huffnode> {
        public int compare(Huffnode x, Huffnode y) {
            return x.count - y.count;
        }
    }

    private static HashMap<Character, Integer> counting(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int n = s.length();
        char[] chars = s.toCharArray();
        for (int i = 0; i < n; i++) {
            int a = 0;

            if (map.get(chars[i]) != null)
                a = map.get(chars[i]);

            map.put(chars[i], a + 1);
        }

        return map;
    }

    private static PriorityQueue<Huffnode> addd(HashMap<Character, Integer> map) {
        int m = map.size();
        q = new PriorityQueue<>(m, new Comp());

        for (char num : map.keySet()) {
            Huffnode node = new Huffnode();
            node.count = map.get(num);
            node.c = num;
            q.add(node);
        }

        return q;
    }

    private static void update(PriorityQueue<Huffnode> q) {
        Huffnode first = q.poll();
        Huffnode second = q.poll();
        int j = first.count + second.count;
        Huffnode total = new Huffnode();
        total.count = j;

        total.left = first;
        total.right = second;
        first.parent = total;
        second.parent = total;

        q.add(total);

    }

    private static void countcode(Huffnode node, String s) {

        if (node.right == null && node.left == null) {
            if (node.c == 0x001c) {
                newmap.put(' ', s);
            } else
                newmap.put(node.c, s);
            return;
        }

        countcode(node.left, s + "0");
        countcode(node.right, s + "1");
    }

    private static void decode(String en, Huffnode node) {
        Huffnode temp = node;
        for (int i = 0; i < en.length(); i++) {
            if (en.charAt(i) == '0' && temp.left != null) {
                temp = temp.left;
            }
            if (en.charAt(i) == '1' && temp.right != null) {
                temp = temp.right;
            }

            if (temp.right == null && temp.left == null) {
                decoded += temp.c;
                temp = node;
            }
        }
    }

    private static void byte2binary(String s) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (s.length() % 8 != 0) {
            int g = s.length() % 8;
            String gg = "0";
            s = s + gg.repeat(g);
        }
        int bit = 0;
        int nebit = 0;
        int bc = 0;
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0')
                bit = 0;
            if (s.charAt(i) == '1')
                bit = 1;

            nebit |= bit << (7 - bc % 8);
            if (bc % 8 == 7) {
                baos.write(nebit);
                j++;
                nebit = 0;
            }
            bc = bc + 1;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\white devil\\Desktop\\compress.txt"));
            baos.writeTo(fos);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String st = "This letter is to express my interest in your posting a Software Developer. With a Bachelors degree in Electrical Engg. and hands-on experience using Java languages to create and implement software applications, I am confident I will be an asset to your organization.I enjoy being challenged and engaging with projects that require me to work outside my comfort and knowledge set, as continuing to learn new languages and development techniques are important to me and the success of your organization. Your listed requirements closely match my background and skills. A few I would like to highlight that would enable me to contribute to your bottom line are Highly skilled in designing, testing, and developing software thorough understanding of data structures and algorithms Knowledgeable of back-end development best practices Hands-on software troubleshooting experience Proven track record of proper documentation for future maintenance and upgrades I have attached a copy of my resume that details my projects and experience in software development. I can be reached anytime via my cell phone, 555-555-5555 or via email at anthony.applicant@email.com";
        HashMap<Character, Integer> nnmap = counting(st);
        q = addd(nnmap);
        while (q.size() > 1) {
            update(q);
        }
        Huffnode temp = q.peek();
        countcode(temp, "");
        for (int i = 0; i < st.length(); i++) {
            encoded += newmap.get(st.charAt(i));
        }
        decode(encoded, temp);
        byte2binary(encoded);
    }
}

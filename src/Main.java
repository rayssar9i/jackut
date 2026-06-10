import easyaccept.EasyAccept;
public class Main {
    public static void main(String[] args) {
        String facade = "br.ufal.ic.p2.jackut.Facade";
        String[][] tests = {
            {facade, "tests/us1_1.txt"},
            {facade, "tests/us1_2.txt"},
            {facade, "tests/us2_1.txt"},
            {facade, "tests/us2_2.txt"},
            {facade, "tests/us3_1.txt"},
            {facade, "tests/us3_2.txt"},
            {facade, "tests/us4_1.txt"},
            {facade, "tests/us4_2.txt"},
        };
        for (String[] test : tests) {
            EasyAccept.main(test);
        }
    }
}
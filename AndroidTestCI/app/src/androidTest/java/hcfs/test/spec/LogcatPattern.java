package hcfs.test.spec;

public class LogcatPattern {

    public static String getHCFSEventPattern(int event) {
        return "eventID = " + event;
    }
}

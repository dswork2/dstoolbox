package ip;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;

public class Matcher {

    class Result {
        String stringA;
        String stringB;
        String matchPercentage;
        String originalString;
        String modifiedString;
    }

    double evaluate(String stringA, String stringB) {
        if (exactMatch(stringA, stringB)) return 100;
        if(stringA.equalsIgnoreCase(stringB)) return 100;
        if(stringA.length() == stringB.length()) {
            return getMatchPercentageByDistance(stringA, stringB);
        }else{
                String largerString;
                String smallerString;
            if(stringA.length() > stringB.length()){
                largerString = stringA;
                smallerString = stringB;
            }else{
                largerString = stringB;
                smallerString = stringA;
            }

        }
    }

    double getMatchPercentageByDistance(String stringA, String stringB) {
//        return (100 - StringUtils.getLevenshteinDistance(stringA,stringB)) / 100;

        StringUtils.rightPad(stringA,100,"*");
        StringUtils.rightPad(stringB,100,"*");
        double totalChars = getTotalChars(stringA, stringB);
        double differentChars = StringUtils.getLevenshteinDistance(stringA, stringB);
        return ((totalChars - differentChars) / totalChars) * 100;
    }

    private int getTotalChars(String stringA, String stringB) {
        return stringA.length() > stringB.length() ? stringA.length() : stringB.length();
    }

    int firstDifferentChar(String stringA, String stringB){
        return StringUtils.indexOfDifference(stringA, stringB);
    }

    private boolean exactMatch(String string1, String string2) {
        return string1.equals(string2);
    }

    private static String getFileBaseName(File file) {
        return file.getName().split(".")[0];
    }

    public static double match(File file1, File file2) {
        return new Matcher().evaluate(getFileBaseName(file1), getFileBaseName(file2));
    }

    public static double match(Path path1, Path path2) {
        return new Matcher().evaluate(getFileBaseName(path1.toFile()), getFileBaseName(path2.toFile()));
    }

}

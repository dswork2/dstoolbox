package ip;

import org.junit.jupiter.api.Test;

class MatcherTest {

    Matcher matcher = new Matcher();
    @Test
    void twoFiles_sameName_differentType_donot_match() {
        System.out.println(matcher.evaluate("filename-1.jpg","filename-1.dng"));
    }

    /**
     *
    twoFiles_sameName_sameType_match
    number of different chars
     **/
}
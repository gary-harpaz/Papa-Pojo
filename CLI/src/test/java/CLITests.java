import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.ppojo.Main;
import org.ppojo.OptionNames;
import org.ppojo.exceptions.FolderNotFoundException;
import org.ppojo.exceptions.FolderPathNotADirectory;

import java.nio.file.Paths;

/**
 * Created by GARY on 10/6/2015.
 */
public class CLITests {
    private static final String TEST_ROOT_SOURCE_FOLDER1="build/resources/test/test-root-source-folder";
    private static final String TEST_FILE_TXT="test-file.txt";

    @Test(expected = ParseException.class)
    public void sourcesArgumentTest1() throws ParseException {
        Main.runMain(new String[] {});
    }
    @Test(expected = MissingArgumentException.class)
    public void sourcesArgumentTest2() throws ParseException {
        Main.runMain(new String[] {"-"+ OptionNames.SOURCES});
    }
    @Test(expected = FolderNotFoundException.class)
    public void sourcesArgumentTest3() throws ParseException {
        Main.runMain(new String[] {"-"+OptionNames.SOURCES,"xyz"});
    }
    @Test(expected = FolderPathNotADirectory.class)
    public void sourcesArgumentTest4() throws ParseException {
        Main.runMain(new String[] {"-"+OptionNames.SOURCES, Paths.get(TEST_ROOT_SOURCE_FOLDER1,TEST_FILE_TXT).toString()});
    }

    @Test
    public void sourcesArgumentTest5() throws ParseException {
        Main.runMain(new String[] {"-"+OptionNames.SOURCES,TEST_ROOT_SOURCE_FOLDER1});
    }


}

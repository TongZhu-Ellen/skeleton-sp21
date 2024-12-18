package gitlet;

import java.io.File;
import java.util.Set;

import static gitlet.MyUtils.*;

import static gitlet.Repository.ADD_STAGE;
import static gitlet.Utils.*;

public class AddStage {


    static void putNameCont(String name, byte[] content) {
        writeContents(join(ADD_STAGE, name), content);
    }

    static byte[] getContent(String name) {
        return readContents(join(ADD_STAGE, name));
    }

    static Set<String> setOfFileNames() {
        return filesInDir(ADD_STAGE);
    }


    static void tryRemove(String name) {
        join(ADD_STAGE, name).delete();
    }

    static boolean isEmpty() {
        return setOfFileNames().size() == 0;
    }



    static void clear() {
        File[] files = ADD_STAGE.listFiles();
        for (File file: files) {
            file.delete();
        }
    }
}

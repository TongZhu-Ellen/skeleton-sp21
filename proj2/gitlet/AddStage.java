package gitlet;

import java.io.File;
import java.util.Set;

import static gitlet.MyUtils.*;

import static gitlet.Repository.ADD_STAGE;
import static gitlet.Utils.join;

public class AddStage {


    static void putNameCont(String name, byte[] content) {
        saveInFileNameCont(ADD_STAGE, name, content);
    }

    static byte[] getContent(String name) {
        return readInFileNameCont(ADD_STAGE, name);
    }

    static Set<String> setOfFileNames() {
        return filesInDir(ADD_STAGE);
    }


    static void tryRemove(String name) {
        join(ADD_STAGE, name).delete();
    }



    static void clear() {
        File[] files = ADD_STAGE.listFiles();
        for (File file: files) {
            file.delete();
        }
    }
}

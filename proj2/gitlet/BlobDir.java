package gitlet;

import java.io.File;

import static gitlet.MyUtils.readInFileNameCont;
import static gitlet.Repository.BLOB_DIR;
import static gitlet.Utils.*;

public class BlobDir {

    // BLOB_DIR


    static void tryAddCont(byte[] content) {
        File file = join(BLOB_DIR, sha1(content));
        if (!file.exists()) {
            writeContents(file, content);
        }
    }

    static byte[] getCont(String sha) {
        return readInFileNameCont(BLOB_DIR, sha);
    }

}

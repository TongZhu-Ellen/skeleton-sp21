package gitlet;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static gitlet.Repository.BLOBS_DIR;
import static gitlet.Utils.*;

public class Blob {

    String sha1;
    byte[] content;

    Blob(String inputSha1, byte[] inputContent) {
        this.sha1 = inputSha1;
        this.content = inputContent;
    }







}

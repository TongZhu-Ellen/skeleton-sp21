package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.*;

public class DirUtils {


    static void writeGivenContentInGivenDirWithName(byte[] content, File dir, String relPath) {
        File fullPath = join(dir, relPath);
        try {
            fullPath.createNewFile();
            writeContents(fullPath, content);
        } catch (Exception ignore) {

        }
    }

    static void writeGivenObjInGivenDir(Serializable obj, File dir) {
        String relPath = sha1(serialize(obj));
        File fullPath = join(dir, relPath);
        try {
            fullPath.createNewFile();
            writeObject(fullPath, obj);
        } catch (Exception ignore) {

        }
    }


    static boolean haveGivenFileInGivenDir(String relPath, File dir) {
        return helpFindShaSetInGivenDir(dir).contains(relPath);
    }

    static Object readGivenFileInGivenDir(String relPath, File dir, Class objectType) {
        File fullPath = join(dir, relPath);
        return readObject(fullPath, objectType);
    }

    static void tryRemoveGivenFileFromGivenDir(String relPath, File dir) {
        File fullPath = join(dir, relPath);
        fullPath.delete();
    }




    static void clearDir(File dir) {
        for (File file : dir.listFiles()) {
            file.delete(); // 删除目录下的文件
        }
    }





















    private static Set<String> helpFindShaSetInGivenDir(File searchedDir) {
        Set<String> relativePathsSet = new HashSet<>();
        for (File file : searchedDir.listFiles()) {
            String relativePath = searchedDir.toPath().relativize(file.toPath()).toString();
            relativePathsSet.add(relativePath);  // 添加到 set，自动去重
        }
        return relativePathsSet;
    }


}

package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.*;

public class DirUtils {


    static void writeGivenContentInGivenDirWithName(byte[] content, File dir, String relPath) {
        File fullPath = join(dir, relPath);
        if (!fullPath.exists()) {
            try {
                fullPath.createNewFile();
            } catch (Exception ignore) {
            }
        }
        writeContents(fullPath, content);
    }

    static void writeGivenObjInGivenDir(Serializable obj, File dir) {
        String relPath = sha1(serialize(obj));
        File fullPath = join(dir, relPath);
        if (!fullPath.exists()) {
            try {
                fullPath.createNewFile();
                writeObject(fullPath, obj);
            } catch (Exception ignore) {

            }
        }
    }




    static byte[] readGivenFileInGivenDir(String relPath, File dir) {
        File fullPath = join(dir, relPath);
        return readContents(fullPath);
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





















    static Set<String> helpFindRelPathSetInGivenDir(File searchedDir) {
        Set<String> relativePathsSet = new HashSet<>();
        for (File file : searchedDir.listFiles()) {
            String relativePath = searchedDir.toPath().relativize(file.toPath()).toString();
            relativePathsSet.add(relativePath);  // 添加到 set，自动去重
        }
        return relativePathsSet;
    }


}
package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

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
        if (!fullPath.isFile()) {
            throw new GitletException("Error: " + fullPath + " is not a file.");
        }
        return readContents(fullPath);
    }

    static Object readGivenFileInGivenDir(String relPath, File dir, Class objectType) {
        File fullPath = join(dir, relPath);
        if (!fullPath.isFile()) {
            throw new GitletException("Error: " + fullPath + " is not a file.");
        }
        return readObject(fullPath, objectType);
    }


    static boolean tryRemoveGivenFileFromGivenDir(String relPath, File dir) {
        File fullPath = join(dir, relPath);
        if (fullPath.exists()) {
            fullPath.delete();
            return true;
        } else {
            return false;
        }
    }


    static void clearDir(File dir) {
        for (File file : dir.listFiles()) {
            file.delete();
        }

    }

    static void printSetInOrder(Set<String> set) {
        List<String> list = new ArrayList<>(set); // 将 Set 转换为 List
        Collections.sort(list); // 按字典顺序排序
        // 输出排序后的 List
        for (String s : list) {
            System.out.println(s);
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

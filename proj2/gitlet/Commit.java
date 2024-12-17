package gitlet;

import java.io.Serializable;
import java.lang.reflect.AnnotatedArrayType;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

class Commit implements Serializable {

    String message;
    Date time;
    Map<String, String> nameShaMap;
    Commit parent;
    Commit otherParent;

    Commit(String inputMsg, Commit inputParent) {
        this.message = inputMsg;

        if (inputParent == null) {
            this.time = new Date(0);
        } else {
            this.time = new Date();
        }

        this.nameShaMap = new HashMap<>();

        this.parent = inputParent;
        this.otherParent = null;
    }

    Set<String> fileSet() {
        return this.nameShaMap.keySet();
    }

    void putNameSha(String name, String sha) {
        this.nameShaMap.put(name, sha);
    }

    void tryRemove(String name) {
        this.nameShaMap.remove(name);
    }


    boolean contains(String name) {
        return this.nameShaMap.keySet().contains(name);
    }

    String tryGetSha(String name) {
        return this.nameShaMap.getOrDefault(name, null);
    }

    byte[] tryGetContent(String name) {
        String sha = this.tryGetSha(name);
        return BlobDir.getCont(sha);
    }

    String tryGetContentAsString(String name) {
        String sha = this.tryGetSha(name);
        return BlobDir.getContAsString(sha);
    }

 // ATTENTION: c1 needs to contain name;
    static boolean isModified(String name, Commit c1, Commit c2) {
         return !Objects.equals(c1.tryGetSha(name), c2.tryGetSha(name));
    }

    void save() {
        writeObject(join(COMMIT_DIR, this.sha()), this);
    }

    String sha() {
        return Utils.sha1(serialize(this));
    }











    List<Commit> ancestersList() {

        List<Commit> ancestorList = new LinkedList<>();
        Queue<Commit> fringe = new LinkedList<>();
        Set<Commit> marked = new HashSet<>();

        fringe.add(this);
        marked.add(this);
        ancestorList.add(this);

        while (!fringe.isEmpty()) {
            Commit v = fringe.remove();
            List<Commit> parOfV = new ArrayList<>();
            if (v.parent != null) {
                parOfV.add(v.parent);
            }
            if (v.otherParent != null) {
                parOfV.add(v.otherParent);
            }
            for (Commit w : parOfV) {
                if (!marked.contains(w)) {
                    fringe.add(w);
                    marked.add(w);
                    ancestorList.add(w);
                }
            }
        }
        return ancestorList;
    }







    void printThisLog() {
        System.out.println("===");
        System.out.println("commit " + sha1(serialize(this)));
        // Adjust the format to remove timezone offset, and match the required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        System.out.println("Date: " + dateFormat.format(this.time));  // Prints without timezone offset
        System.out.println(this.message);
        System.out.println("");
    }

    void printLogFromThis() {
        this.printThisLog();
        if (this.parent != null) {
            this.parent.printLogFromThis();
        }
    }






    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // 引用相等，直接返回 true
        if (obj == null || getClass() != obj.getClass()) return false; // 类型不匹配
        Commit commit = (Commit) obj;
        return this.sha().equals(commit.sha()); // 使用 sha() 方法判断逻辑相等
    }

    @Override
    public int hashCode() {
        return Objects.hash(sha()); // 使用 sha() 方法生成哈希值
    }





}

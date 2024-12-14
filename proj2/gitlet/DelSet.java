package gitlet;

import java.util.HashSet;

import static gitlet.Repository.DEL_SET;
import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;

public class DelSet {



    static HashSet<String> setOfFileNames() {
        return readObject(DEL_SET, HashSet.class);
    }

    static void add(String name) {
        HashSet<String> del_set = setOfFileNames();
        del_set.add(name);
        writeObject(DEL_SET, del_set);
    }

    static void tryRemove(String name) {
        HashSet<String> del_set = setOfFileNames();
        del_set.remove(name);
        writeObject(DEL_SET, del_set);
    }


    static void clear() {
        writeObject(DEL_SET, new HashSet<String>());
    }

}

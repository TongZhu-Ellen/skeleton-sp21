package gitlet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import static gitlet.Repository.DEL_SET;
import static gitlet.Utils.*;

// this is HashSet but it saves itself automatically;
class DelSet implements Serializable {


    static void add(String name) {
        HashSet<String> curDelSet = readObject(DEL_SET, HashSet.class);
        curDelSet.add(name);
        writeObject(DEL_SET, curDelSet);
    }

    static void remove(String name) {
        HashSet<String> curDelSet = readObject(DEL_SET, HashSet.class);
        curDelSet.remove(name);
        writeObject(DEL_SET, curDelSet);
    }

   static HashSet<String> getSet() {
        return readObject(DEL_SET, HashSet.class);
   }

    static void clear() {
        writeObject(DEL_SET, new HashSet<>());
    }



}

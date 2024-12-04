package gitlet;

import java.io.File;

import java.util.Set;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.BranchUtils.*;


import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validArg(args, 1);
                if (GITLET_DIR.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                } else {
                    setDirs();
                    Commit initialCommit = new Commit("initial commit", null);
                    DirUtils.writeGivenObjInGivenDir(initialCommit, COMMITS);
                    makeBranch("master", initialCommit);
                    headThisBranch("master");
                }
                break;

            case "add":
                validArg(args, 2);
                String name1 = args[1];

                if (!join(CWD, name1).exists()) {
                    System.out.println("File does not exist.");
                    return;
                }
                byte[] contentCWD = DirUtils.readGivenFileInGivenDir(name1, CWD);
                byte[] contentInHeadCommit = null;
                if (getHeadCommit().nameShaMap.containsKey(name1)) {
                    contentInHeadCommit = getHeadCommit().getContent(name1);
                }
                if ((contentInHeadCommit != null) && (!sha1(contentCWD).equals(sha1(contentInHeadCommit)))) {
                    DirUtils.writeGivenContentInGivenDirWithName(contentCWD, ADD_STAGE, name1);
                } else {
                    DirUtils.tryRemoveGivenFileFromGivenDir(name1, ADD_STAGE);
                }
                DelSet.remove(name1);
                break;


            case "rm":
                validArg(args, 2);
                int actionCount = 0;
                String name2 = args[1];
                File nameInCWD = join(CWD, name2);

                if (DirUtils.tryRemoveGivenFileFromGivenDir(name2, ADD_STAGE)) {
                    actionCount += 1;
                }
                if (getHeadCommit().tryFindShaOfGivenName(name2) != null) {
                    DelSet.add(name2);
                    nameInCWD.delete();
                    actionCount += 1;
                }
                if (actionCount == 0) {
                    System.out.println("No reason to remove the file.");
                }
                break;



            case "commit":

                if (ADD_STAGE.listFiles().length == 0) {
                    System.out.println("No changes added to the commit.");
                    break;
                }
                validArg(args, 2);
                if (args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    break;
                }
                String message = args[1];
                Commit oldCommit = getHeadCommit();
                Commit newCommit = new Commit(message, oldCommit.sha());
                for (String name : oldCommit.nameShaMap.keySet()) {
                    String sha = oldCommit.nameShaMap.get(name);
                    newCommit.nameShaMap.put(name, sha);
                }
                for (String name : DirUtils.helpFindRelPathSetInGivenDir(ADD_STAGE)) {
                    byte[] content = DirUtils.readGivenFileInGivenDir(name, ADD_STAGE);
                    String sha = sha1(content);
                    newCommit.nameShaMap.put(name, sha);
                    DirUtils.writeGivenContentInGivenDirWithName(content, BLOBS, sha);
                }
                for (String name : DelSet.getSet()) {
                    newCommit.nameShaMap.remove(name);
                }
                DirUtils.writeGivenObjInGivenDir(newCommit, COMMITS);
                updateBranch(getHeadBranch(), newCommit);
                DirUtils.clearDir(ADD_STAGE);
                DelSet.clear();
                break;

            case "checkout":
                if ((args.length == 3) && (args[1].equals("--"))) {
                    helpCheckoutSingleFileInGivenCommit(args[2], getHeadCommit());
                } else if ((args.length == 4) && (args[2].equals("--"))) {
                    String shortenedCommitSha = args[1];
                    String commitSha = matchCommitId(DirUtils.helpFindRelPathSetInGivenDir(COMMITS), shortenedCommitSha);
                    Commit goalCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitSha, COMMITS, Commit.class);
                    helpCheckoutSingleFileInGivenCommit(args[3], goalCommit);
                } else {
                    validArg(args, 2);
                    String branchName = args[1];
                    File givenBranchFullPath = join(BRANCHES, branchName);
                    if (!givenBranchFullPath.exists()) {
                        System.out.println("No such branch exists.");
                        break;
                    } else if (branchName.equals(getHeadBranch())) {
                        System.out.println("No need to checkout the current branch.");
                        break;
                    }
                    Commit newBranchHead = findBranch(branchName);
                    helpCheckOutCommit(newBranchHead);
                    headThisBranch(branchName);
                }

                break;


            case "reset":
                validArg(args, 2);
                String shortenedCommitID = args[1];
                String commitId = matchCommitId(DirUtils.helpFindRelPathSetInGivenDir(COMMITS), shortenedCommitID);
                Commit newBranchHead = (Commit) DirUtils.readGivenFileInGivenDir(commitId, COMMITS, Commit.class);
                helpCheckOutCommit(newBranchHead);
                updateBranch(getHeadBranch(), newBranchHead);
                break;


            case "log":
                getHeadCommit().printLogFromThis();
                break;

            case "global-log":
                for (String commitRelPath : DirUtils.helpFindRelPathSetInGivenDir(COMMITS)) {
                    Commit curCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitRelPath, COMMITS, Commit.class);
                    curCommit.printThisLog();
                }
                break;




            case "status":
                validArg(args, 1);

                System.out.println("=== Branches ===");
                BranchUtils.printBranchInOrder();
                System.out.println("");



                System.out.println("=== Staged Files ===");
                DirUtils.printSetInOrder(DirUtils.helpFindRelPathSetInGivenDir(ADD_STAGE));
                System.out.println("");




                System.out.println("=== Removed Files ===");
                DirUtils.printSetInOrder(DelSet.getSet());
                System.out.println("");




                System.out.println("=== Modifications Not Staged For Commit ===");
                System.out.println("");

                System.out.println("=== Untracked Files ===");
                System.out.println("");

                break;



            case "find":
                validArg(args, 2);
                String searchedMessage = args[1];
                int matchedCount = 0;
                for (String commitRelPath : DirUtils.helpFindRelPathSetInGivenDir(COMMITS)) {
                    Commit targetCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitRelPath, COMMITS, Commit.class);
                    String commitMsg = targetCommit.getMessage();
                    if (commitMsg.contains(searchedMessage)) {
                        System.out.println(targetCommit.sha());
                        matchedCount += 1;
                    }
                }
                if (matchedCount == 0) {
                    System.out.println("Found no commit with that message.");
                }

                break;


        }
    }


        static void validArg (String[]args,int n){
            if (args.length != n) {
                throw new GitletException("Invalid number of arguments");
            }
        }

        static String matchCommitId (Set<String> set, String prefix){
            String matched = null;
            for (String str : set) {
                if (str.startsWith(prefix)) {
                    if (matched != null) {
                        // 如果已经找到一个匹配项，再找到一个，返回null
                        return null;
                    }
                    matched = str;
                }
            }

            if (matched == null) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }

            return matched;

        }


}






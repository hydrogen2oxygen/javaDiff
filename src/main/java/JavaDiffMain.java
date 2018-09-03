import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaDiffMain {

    private List<String> differences = new ArrayList<>();
    private File baseFolder1;
    private File baseFolder2;

    public JavaDiffMain(File folder1, File folder2) {

        this.baseFolder1 = folder1;
        this.baseFolder2 = folder2;
    }

    public void startDiff() throws IOException {

        diffFolders(baseFolder1, baseFolder2);

        System.out.println("-----------------------------");
    }

    private void diffFolders(File folder1, File folder2) throws IOException {

        for (File f1 : folder1.listFiles()) {

            if (f1.isDirectory()) {

                if ("target".equals(f1.getName())) {
                    continue;
                }

                if (f1.getAbsolutePath().contains("$")) {
                    continue;
                }

                if (f1.getAbsolutePath().contains(".settings")) {
                    continue;
                }

                File f2 = getEquivalentFile(f1, folder1, folder2);

                if (!(f2.isDirectory() && f2.exists())) {
                    addDifference("Folder " + f2.getAbsolutePath() + " does not exist");
                } else  {
                    diffFolders(f1, f2);
                }
            }

            if (!f1.getName().endsWith("java")) {
                continue;
            }

            File f2 = getEquivalentFile(f1, folder1, folder2);

            if (!f2.exists()) {
                addDifference("Folder " + f2.getAbsolutePath() + " does not exist");
                continue;
            }

            if (f2.exists() && f2.isDirectory()) {
                continue;
            }

            List<String> lines1 = FileUtils.readLines(f1,"UTF-8");
            List<String> lines2 = FileUtils.readLines(f2,"UTF-8");

            if (lines1.size() != lines2.size()) {
                addDifference("!= Files " + f2.getAbsolutePath() + " differs in " + (lines1.size() - lines2.size()) + " lines");
            } else {
                int count = 0;
                int y = 0;

                for (String line1 : lines1) {

                    if (!line1.equals(lines2.get(y))) {
                        count++;
                    }

                    y++;
                }

                if (count > 3)  {
                    addDifference("!= Files " + f2.getAbsolutePath() + " differs in " + count + " lines");
                }
            }
        }
    }

    private void addDifference(String s) {
        System.out.println(s);
        //differences.add(s);
    }

    public File getEquivalentFile(File f1, File base1, File base2) {
        String diffPath1 = f1.getAbsolutePath().replaceAll("\\\\","/").replaceFirst(base1.getAbsolutePath().replaceAll("\\\\","/"),"");
        String path2 = base2.getAbsolutePath().replaceAll("\\\\","/") + diffPath1;
        return new File(path2);
    }

    public static void main(String [] args) throws IOException {

        System.out.println("===============================");
        System.out.println("       J A V A   D I F F");
        System.out.println("===============================");

        if (args.length != 2) {
            System.out.println("You need two arguments, the path to folder 1 and folder 2.");
            System.exit(0);
        }

        File folder1 = new File(args[0]);
        File folder2 = new File(args[1]);

        if (!(folder1.exists() && folder1.isDirectory())) {
            System.out.println("Folder " + args[0] + " does not exist or is not a folder");
        }

        if (!(folder2.exists() && folder2.isDirectory())) {
            System.out.println("Folder " + args[1] + " does not exist or is not a folder");
        }

        JavaDiffMain javaDiffMain = new JavaDiffMain(folder1, folder2);
        javaDiffMain.startDiff();
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by mooyl on 2016-03-22.
 */
public class Test {

    public static void main (String [] args) throws FileNotFoundException {
        Scanner fin = new Scanner (new File("test.txt"));
        while (fin.hasNextLine()){
            System.out.println(fin.nextLine());
        }
    }
}

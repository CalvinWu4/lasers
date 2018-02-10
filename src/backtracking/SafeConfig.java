
package backtracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 * <p>
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */

public class SafeConfig implements Configuration {
    private char[][] table;
    private int currow;
    private int curcol;
    private int rowdim;
    private int coldim;
    private int[] colarr;
    private int[] rowarr;
    private String output;
    private String file = "";

    public SafeConfig(String filename) throws FileNotFoundException {
        file = filename;
        Scanner in = new Scanner(new File(filename));
        rowdim = in.nextInt();
        coldim = in.nextInt();

        table = new char[rowdim][coldim];
        for (currow = 0; currow < rowdim; currow++) {
            for (curcol = 0; curcol < coldim; curcol++) {
                table[currow][curcol] = in.next().charAt(0);
            }
        }

        currow = -1;
        curcol = 0;
        in.close();
    }

    public SafeConfig(SafeConfig other) {
        this.rowdim = other.rowdim;
        this.coldim = other.coldim;


        table = new char[rowdim][coldim];
        for (int row = 0; row < rowdim; row++) {
            for (int col = 0; col < coldim; col++) {
                table[row][col] = other.table[row][col];

            }

        }
    }



    @Override
    public Collection<Configuration> getSuccessors() {
        // TODO
        Collection<Configuration> successors = new ArrayList<Configuration>();
        currow++;

        if (currow == rowdim) {
            currow = 0;
            curcol++;

        }
        if (curcol == coldim) {
            return successors;

        }
        switch (table[currow][curcol]) {
            case ('.'):
                if ( !lasersgreaterthan()) {
                    SafeConfig x = new SafeConfig(this);
                    SafeConfig y = new SafeConfig(this);
                    x.table[currow][curcol] = '.';
                    y.table[currow][curcol] = 'L';
                    successors.add(y);
                    successors.add(x);
                } else {
                    table[currow][curcol] = '.';
                    successors.add(this);
                }
                break;
            default:
                successors.add(this);
                break;
        }


        return successors;  // replace
    }




    /**
     * Helper method for is valid, that checks if a placed laser doesn't come in direct contact with another laser
     *
     * @return boolean
     */
    public boolean laserHitsLaser() {
        if (table[currow][curcol] == 'L') {
            for (int i = curcol; i < rowdim; i++) {
                if (table[i][curcol] == 'L') {
                    return true;
                }
            }
            for (int j = curcol; j < coldim; j++) {
                if (table[currow][j] == 'L') {
                    return true;

                }
            }
            for (int i = currow; i > -1; i--) {
                if (table[i][curcol] == 'L') {
                    return true;
                }
            }
            for (int j = curcol; j > -1; j--) {
                if (table[currow][j] == 'L') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method for is valid, that checks if a placed laser is not next a 0 pillar
     *
     * @return boolean
     */

    /**
     * Helper method for is valid, that checks if the number lasers cardinally adjacent to
     * a placed pillar equals its value
     *
     * @return boolean
     */
    public boolean lasersgreaterthan() {
        int count = 0;
        char[] pillarvalues = new char[]{'X', '1', '2', '3', '4', '0'};
        if (Arrays.asList(pillarvalues).contains(table[currow][curcol])) {
            if (curcol - 1 >= 0 && table[currow][curcol - 1] == 'L') {
                count += 1;
            }
            else if (currow + 1 < rowdim && table[currow + 1][curcol] == 'L') {
                count += 1;
            }
            else if (curcol + 1 < coldim && table[currow][curcol + 1] == 'L') {
                count += 1;
            }
            else if (currow - 1 >= 0 && table[currow - 1][curcol] != 'L') {
                count += 1;

            }
        }

        if (table[currow][curcol] < count) {
            return false;
        }
        else {
            return true;
        }
    }



    /**
     * Helper method for is valid, that checks if the number lasers cardinally adjacent to
     * a placed pillar equals its value
     *
     * @return boolean
     */
    public boolean laserPillSetupisinvalid() {
        int count = 0;
        char[] pillarvalues = new char[]{'X', '1', '2', '3', '4', '0'};
        if (Arrays.asList(pillarvalues).contains(table[currow][curcol])) {
            if (curcol - 1 >= 0 && table[currow][curcol - 1] == 'L') {
                count += 1;
            }
            else if (currow + 1 < rowdim && table[currow + 1][curcol] == 'L') {
                count += 1;
            }
            else if (curcol + 1 < coldim && table[currow][curcol + 1] == 'L') {
                count += 1;
            }
            else if (currow - 1 >= 0 && table[currow - 1][curcol] != 'L') {
                count += 1;

            }
        }

        if (table[currow][curcol] == count) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean hasEmptySpace() {

        if (table[currow][curcol] == '.') {
            return true;
        }
        else
            return false;

    }
    @Override
    /**
     * Is the current configuration valid or not?
     *
     * @return true if valid; false otherwise
     */
    public boolean isValid() {
        for (currow = 0; currow < rowdim; currow++) {
            for (curcol = 0; curcol < coldim; curcol++) {
                if (lasersgreaterthan()){
                    System.out.println("Error verifying at: (" + currow + "," + curcol + ")");
                    return false;}

                else if (laserHitsLaser()){
                    System.out.println("Error verifying at: (" + currow + "," + curcol + ")");
                    return false;}

                else{
                    System.out.println("Safe is fully verified!");
                    return true;
                }

            }

        }

        return false;

    }

    @Override
    public boolean isGoal() {
        for (currow = 0; currow < rowdim; currow++) {
            for (curcol = 0; curcol < coldim; curcol++) {

                if (hasEmptySpace()) {

                    System.out.println("Error verifying at: (" + currow + "," + curcol + ")");
                    return false;

                }


                else if (laserPillSetupisinvalid()){
                    System.out.println("Error verifying at: (" + currow + "," + curcol + ")");
                    return false;}

                else if (laserHitsLaser()){
                    System.out.println("Error verifying at: (" + currow + "," + curcol + ")");
                    return false;}

                else{
                    System.out.println("Safe is fully verified!");
                    return true;
                }

            }

        }

        return false;
    }
}

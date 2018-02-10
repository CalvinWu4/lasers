package model;


import backtracking.Configuration;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.*;

public class LasersModel extends Observable{
    public static char[][] table;
    public static int currow;
    public static int curcol;
    public static int rowdim;
    public static int coldim;
    public static int[] colarr;
    public static int[] rowarr;
    public String output;
    public String file = "";

    public LasersModel(String filename) throws FileNotFoundException {
        output = filename + " loaded";
        file = filename;
        Scanner in = new Scanner(new File(filename));
        rowdim = in.nextInt();
        coldim = in.nextInt();
        rowarr = new int[rowdim];
        colarr = new int[coldim];
        table = new char[rowdim][coldim];
        for (currow = 0; currow < rowdim; currow++) {
            for (curcol = 0; curcol < coldim; curcol++) {
                table[currow][curcol] = in.next().charAt(0);
            }
        }
        for (int i = 0; i < rowdim; i++) {
            rowarr[i] = i;
        }
        for (int i = 0; i < coldim; i++) {
            colarr[i] = i;
        }
//        currow = -1;
//        curcol = 0;
        in.close();
        announceChange();
    }


        public String getOutput() {
        return output;
    }

    /**
     * The display command just redisplays the safe to standard output, with no status message.
     *
     * @return String table display
     */
    public String display() {
        String output = "";
        output += "  ";
        for (int i = 0; i < coldim; i++) {
            output += i % 10 + " ";
        }
        output += "\n";
        output += "  ";

        for (int i = 0; i < coldim * 2 - 1; i++) {
            output += "-";
        }
        output += "\n";
        for (int row = 0; row < rowdim; row++) {
            output += row % 10 + "|";
            for (int col = 0; col < coldim; col++) {
                output += table[row][col] + " ";
            }
            if (row != rowdim - 1) {
                output += "\n";
            }
        }
        announceChange();
        return output;
    }

    /**
     * The help command displays the help message to standard output, with no status message.
     *
     * @return String help message
     */
    public String help() {
        String output = "";
        output += "a|add r c: Add laser to (r,c)\n" +
                "d|display: Display safe\n" +
                "h|help: Print this help message\n" +
                "q|quit: Exit program\n" +
                "r|remove r c: Remove laser from (r,c)\n" +
                "v|verify: Verify safe correctness";
        announceChange();
        return output;
    }

    /**
     * Resets all placed lasers and beams in the table.
     */
    public void restart() {
        for (currow = 0; currow < rowdim; currow++) {
            for (curcol = 0; curcol < coldim; curcol++) {
                if (table[currow][curcol] == 'L'||table[currow][curcol] == '*') {
                    table[currow][curcol] = '.';
                }
            }
        }
        output = file + " has been reset";
        announceChange();
    }

    /**
     * Checks if a spot has a laser and chooses add or remove accordingly.
     *
     * @param r row of the laser
     *@param c column of the laser
     */
    public void choose(int r, int c) {
        if (table[r][c] == 'L') {
            remove(r, c);
        }
        else {
            add(r, c);
        }
    }

    boolean hasRemoved; //  Used for output message for LasersGUI

    /**
     * Adds laser at given row and column or gives an error message
     *
     * @param r row to add laser
     * @param c column to add laser
     */
    public void add(int r, int c) {
        if (table[r][c] == '.' || table[r][c] == '*') {
            if (!hasRemoved) {
                output = ("Laser added at: (" + r % 10 + ", " + c % 10 + ")");
            }
            for (int i = r; i < rowdim; i++) {
                if (table[i][c] == '.' || table[i][c] == '*') {
                    table[i][c] = '*';
                }
                else {
                    break;
                }
            }
            for (int j = c; j < coldim; j++) {
                if (table[r][j] == '.' || table[r][j] == '*') {
                    table[r][j] = '*';
                }
                else {
                    break;
                }
            }
            for (int i = r; i > -1; i--) {
                if (table[i][c] == '.' || table[i][c] == '*') {
                    table[i][c] = '*';
                }
                else {
                    break;
                }
            }
            for (int j = c; j > -1; j--) {
                if (table[r][j] == '.' || table[r][j] == '*') {
                    table[r][j] = '*';
                }
                else {
                    break;
                }
            }
            table[r][c] = 'L';
            hasRemoved = false;
        }
        else {
            output = ("Error adding laser at: (" + r % 10 + ", " + c % 10 + ")");
        }
        announceChange();
    }

    /**
     * Removes laser at given row and column or gives an error message
     *
     * @param r row to remove laser
     * @param c column to remove laser
     */
    public void remove(int r, int c) {
        if (table[r][c] == 'L') {
            output = ("Laser removed at: (" + r % 10 + ", " + c % 10 + ")");
            table[r][c] = '*';
            for (int i = 0; i < rowdim; i++) {
                if (table[i][c] == '*') {
                    table[i][c] = '.';
                }
            }
            for (int i = 0; i < coldim; i++) {
                if (table[r][i] == '*') {
                    table[r][i] = '.';
                }
            }
            for (int row = 0; row < rowdim; row++) {
                for (int col = 0; col < coldim; col++) {
                    if (table[row][col] == 'L') {
                        table[row][col] = '.';
                        hasRemoved = true;
                        add(row, col);
                    }
                }
            }

        }
        else {
            output = ("Error removing laser at: (" + r % 10 + ", " + c % 10 + ")");
        }
        announceChange();
    }

    /**
     * Helper method for is valid, that checks if a placed laser's beams hit another laser
     * it iterates through a row and collumn from the current position and compares each value to a laser
     *
     * @return boolean
     */
    public boolean laserHitsLaser() {
        if (table[currow][curcol] == 'L') {
            for (int i = currow + 1; i < rowdim; i++) {
                if (table[i][curcol] == 'L') {
                    return true;
                }
                else if (table[i][curcol] != '*' && table[i][curcol] != '.') {
                    break;
                }
            }
            for (int j = curcol + 1; j < coldim; j++) {
                if (table[currow][j] == 'L') {
                    return true;
                }
                else if (table[currow][j] != '*' && table[currow][j] != '.') {
                    break;
                }
            }
            for (int i = currow - 1; i > -1; i--) {
                if (table[i][curcol] == 'L') {
                    return true;
                }
                else if (table[i][curcol] != '*' && table[i][curcol] != '.') {
                    break;
                }
            }
            for (int j = curcol - 1; j > -1; j--) {
                if (table[currow][j] == 'L') {
                    return true;
                }
                else if (table[currow][j] != '*' && table[currow][j] != '.') {
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Helper method for is valid, that checks if the number lasers cardinally adjacent to
     * a placed pillar equals its value
     * it does this by incrementing a count variable every time a pillar's cardinal adjacent spot contains a laser
     * it then checks this value to the pillar value
     *
     * @return boolean
     */
    public boolean laserPillSetupisinvalid() {
        int count = 0;
        if (!(table[currow][curcol] == '1' || table[currow][curcol] == '2' || table[currow][curcol] == '3'
                || table[currow][curcol] == '4' || table[currow][curcol] == 'X' || table[currow][curcol] == '0')) {
            return false;
        }
        if (table[currow][curcol] == '1' || table[currow][curcol] == '2' || table[currow][curcol] == '3'
                || table[currow][curcol] == '4' || table[currow][curcol] == 'X' || table[currow][curcol] == '0') {
            if (curcol - 1 >= 0 && table[currow][curcol - 1] == 'L') {
                count += 1;
            }
            if (currow + 1 < rowdim && table[currow + 1][curcol] == 'L') {
                count += 1;
            }
            if (curcol + 1 < coldim && table[currow][curcol + 1] == 'L') {
                count += 1;
            }
            if (currow - 1 >= 0 && table[currow - 1][curcol] == 'L') {
                count += 1;
            }
        }
        if (table[currow][curcol] == (char) count + 48 || table[currow][curcol] == 'X') {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Helper method for is valid, that checks if a table tile is empty
     *
     * @return boolean
     */
    public boolean hasEmptySpace() {
        if (table[currow][curcol] == '.') {
            return true;
        }
        else
            return false;
    }

    public boolean isInvalid;
    public int[] invalidSpot = new int[2];

    /**
     * Checks if the lasers and pillars have valid configurations
     *
     * @return String Error or success message
     */
    public String verify() {
        for (currow = 0; currow < rowdim; currow++) {
            for (curcol = 0; curcol < coldim; curcol++) {
                if (hasEmptySpace() || laserHitsLaser() || laserPillSetupisinvalid()) {
                    isInvalid = true;
                    invalidSpot[0] = (currow);
                    invalidSpot[1] = (curcol);
                    return ("Error verifying at: (" + currow + "," + curcol + ")");
                }
//                else if (laserPillSetupisinvalid()){
//                    return("Error verifying at: (" + currow + "," + curcol + ")");
//                }
//                if (laserHitsLaser()){
//                    return ("Error verifying at: ("+currow+","+curcol+")");
//                }
            }
        }
        isInvalid = false;
        announceChange();
        return ("Safe is fully verified!");
    }

    /**
     * Gets the string output from verify changes it to the output message for LasersGUI
     */
    public void verify2() {
        output = verify();
        announceChange();
    }

    /**
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    private void announceChange() {
        setChanged();
        notifyObservers();
    }
}

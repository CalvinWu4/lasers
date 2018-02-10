package ptui;

import model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author Sean Strout @ RIT CS
 * @author Calvin Wu
 * @author Nahjee Sowah
 */
public class ControllerPTUI  {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) throws FileNotFoundException {
        // TODO
        System.out.print(model.display());
        System.out.print("\n");
        boolean hasQuit = false;
        if(inputFile!=null) {
            Scanner file = new Scanner(new File(inputFile));
            while (file.hasNextLine()) {
                String command = file.nextLine();
                String[] input = command.split(" ");
                System.out.println("> "+command);
                    if (input[0].equals("d") || input[0].equals("display")){
                        System.out.println(model.display());
                    }
                    else if(input[0].equals("h")|| input[0].equals("help")){
                        System.out.println(model.help());
                    }
                    else if (input[0].equals("a") || input[0].equals("add")) {
                        if (input.length == 3) {
                            try {
                                int row = Integer.parseInt(input[1]);
                                int col = Integer.parseInt(input[2]);
                                model.add(row, col);
                                System.out.println(model.getOutput());
                                System.out.println(model.display());
                            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                System.out.println("Incorrect coordinates");
                            }
                        } else {
                            System.out.println("Incorrect coordinates");
                        }
                    }
                    else if (input[0].equals("r") || input[0].equals("remove")) {
                        if (input.length == 3) {
                            try {
                                int row = Integer.parseInt(input[1]);
                                int col = Integer.parseInt(input[2]);
                                model.remove(row, col);
                                System.out.println(model.getOutput());
                                System.out.println(model.display());
                            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                System.out.println("Incorrect coordinates");
                            }
                        } else {
                            System.out.println("Incorrect coordinates");
                        }
                    }
                    else if(input[0].equals("v") || input[0].equals("verify")) {
                        System.out.println(model.verify());
                        System.out.println(model.display());
                    }
                    else if (!input[0].equals("")) {
                        System.out.println("Unrecognized command: " + input[0]);
                        System.out.println(model.display());
                    }
                }
            }
        Scanner sc = new Scanner(System.in);
        while (!hasQuit) {
            System.out.print("> ");
            String[] input = sc.nextLine().split(" ");
            if (input[0].equals("d") || input[0].equals("display")){
                System.out.println(model.display());
            }
            else if(input[0].equals("h")|| input[0].equals("help")){
                System.out.println(model.help());
            }
            else if (input[0].equals("a") || input[0].equals("add")) {
                if (input.length == 3) {
                    try {
                        int row = Integer.parseInt(input[1]);
                        int col = Integer.parseInt(input[2]);
                        model.add(row, col);
                        System.out.println(model.getOutput());
                        System.out.println(model.display());
                    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                        System.out.println("Incorrect coordinates");
                    }
                } else {
                    System.out.println("Incorrect coordinates");
                }
            }
            else if (input[0].equals("r") || input[0].equals("remove")) {
                if (input.length == 3) {
                    try {
                        int row = Integer.parseInt(input[1]);
                        int col = Integer.parseInt(input[2]);
                        model.remove(row, col);
                        System.out.println(model.getOutput());
                        System.out.println(model.display());
                    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                        System.out.println("Incorrect coordinates");
                    }
                } else {
                    System.out.println("Incorrect coordinates");
                }
            }
            else if(input[0].equals("v") || input[0].equals("verify")) {
                System.out.println(model.verify());
                System.out.println(model.display());
            }
            else if (input[0].equals("q") || input[0].equals("quit")) {
                hasQuit = true;
            }
            else if (!input[0].equals("")) {
                System.out.println("Unrecognized command: " + input[0]);
                System.out.println(model.display());
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

class MainFrame extends JFrame{
    public static void main(String[]args){
        SeriesPanel.SeriesType series;
        SeriesPanel.MovementType movement;
        Scanner kb = new Scanner(System.in);
        int side;

        System.out.println("Enter the square you'd like to see");
        do {
            try {
                side = kb.nextInt();
                //Just below the sqrt of 2147483647 (Integer.MAX_LENGTH)
                if(side > 46340){
                    System.out.println("You can't input that!");
                }
                else{
                    break;
                }
            }catch (Exception e){
                System.out.println("Your input caused an error");
            }
        }
        while (true);

        System.out.println("\nSelect a type to draw:");
        for(int x = 0; x < SeriesPanel.SeriesType.values().length; x++){
            System.out.println(x + " - "+ SeriesPanel.SeriesType.values()[x].name());
        }
        do {
            try {
                series = SeriesPanel.SeriesType.values()[kb.nextInt()];
                break;
            }catch (Exception e){
                System.out.println("Your input caused an error");
            }
        }
        while (true);

        System.out.println("\nSelect a type to move:");
        for(int x = 0; x < SeriesPanel.MovementType.values().length; x++){
            System.out.println(x + " - "+ SeriesPanel.MovementType.values()[x].name());
        }
        do {
            try {
                movement = SeriesPanel.MovementType.values()[kb.nextInt()];
                break;
            }catch (Exception e){
                System.out.println("Your input caused an error");
            }
        }
        while (true);

        new MainFrame(side, series, movement);
    }

    private MainFrame(int square, SeriesPanel.SeriesType type, SeriesPanel.MovementType movement){
        //Sequence Quarter Spiral
        super("SQS");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        SeriesPanel p = new SeriesPanel(square, type, movement);
        setPreferredSize(new Dimension(200,200));
        setLayout(null);
        add(p);
        pack();
        setVisible(true);
    }
}

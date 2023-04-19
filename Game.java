import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Random;

public class Game implements ActionListener
{

    private JFrame frame;
    private JLabel label;
    private JPanel panelText;
    private JPanel panelBtn;
    private JButton[] buttons; //the cards
    private JPanel panelMenu;
    private JButton btnNew;
    private JButton btnExit;
    private JLabel labelPoints;

    private Color base;
    private int pairs;//how many pairs we found
    private int moves; //count until 2
    private int[] twoMoves; //save the two last moves
    private int[][] numbers; //matrix of the numbres
    private int index;
    private int row;
    private int col;
    private int totalPairs;
    private boolean player1Turn;
    private int player1Pairs;


    public Game()
    {
        this(4, 4);
    }

    public Game(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.totalPairs = (this.col * this.row) / 2;
        this.player1Pairs = 0;
        init();
        JOptionPane.showMessageDialog(this.frame, "Welcome to Memory", "Memory" +
                "", JOptionPane.WARNING_MESSAGE);
        turn();
        play();
    }

    private void turn()
    {
        double turn = Math.random();
        if (turn > 0.5)
        {
            this.player1Turn = true;
        } else
        {
            this.player1Turn = false;
        }
    }

    private void nextTurn()
    {
        if (this.player1Turn)
            this.player1Turn = false;
        else
            this.player1Turn = true;
    }

    private void play()     //the main function of the game
    {
        while (true)
        {
            this.moves = 0;
            this.index = 0;
            this.pairs = 0;
            //reset all
            while (this.pairs != this.totalPairs)    //until we found all the pairs
            {
                try //delay
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                if (player1Turn)
                    this.label.setText("player 1 turn");
                else
                    this.label.setText("player 2 turn");
                if (this.moves == 2) //if we do 2 moves
                {
                    if (this.buttons[this.twoMoves[0]].getText().equals(this.buttons[this.twoMoves[1]].getText())
                            && this.twoMoves[0] != Integer.MAX_VALUE && this.twoMoves[1] != Integer.MAX_VALUE)
                    {
                        match();  //if in the two moves we found a pair
                    } else
                    {
                        worng(); //if in the two moves we don't found a pair
                    }
                    nextTurn();
                }

            }
            String win = "";
            if (this.player1Pairs == (this.totalPairs - player1Pairs))
                win = "tie";
            else if (this.player1Pairs > (this.totalPairs - player1Pairs))
                win = "player 1 win";
            else
                win = "player 2 win";
            this.label.setText("game over ! " + win);
            JOptionPane.showMessageDialog(this.frame, "game over ! " + win, "Memory" +
                    "", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void worng()    //if we wrong
    {
        this.label.setText("wrong !");
        try     //delay to seeing the cards
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        if (this.twoMoves[0] != Integer.MAX_VALUE)
            this.buttons[this.twoMoves[0]].setText("");
        if (this.twoMoves[1] != Integer.MAX_VALUE)
            this.buttons[this.twoMoves[1]].setText("");
        this.buttons[twoMoves[0]].setBackground(this.base);
        this.buttons[twoMoves[1]].setBackground(this.base);
        this.moves = 0;
        this.twoMoves[0] = 0;
        this.twoMoves[1] = 0;
        this.index = 0;                 //reset all the parameters
        this.labelPoints.setText("    pairs found " + this.pairs + " of " + this.totalPairs
                + "    player 1 pairs = " + this.player1Pairs + "    player 2 pairs = " + (this.pairs - this.player1Pairs));

    }

    private void match()    //if we found a pair
    {
        this.buttons[twoMoves[0]].setBackground(Color.GREEN);
        this.buttons[twoMoves[1]].setBackground(Color.GREEN);
        this.moves = 0;
        this.twoMoves[0] = Integer.MAX_VALUE;
        this.twoMoves[1] = Integer.MAX_VALUE;
        pairs++;
        if (this.player1Turn)
            player1Pairs++;
        this.labelPoints.setText("    pairs found " + this.pairs + " of " + this.totalPairs
                + "    player 1 pairs = " + this.player1Pairs + "    player 2 pairs = " + (this.pairs - this.player1Pairs));
        this.label.setText("found !");
        nextTurn();//if the player found a pair its his turn again
        this.index = 0;
    }

    public void actionPerformed(ActionEvent e)
    {
        int x = 0;
        int y = 0;  //x and y for the position in the matrix
        for (int i = 0; i < this.buttons.length; i++)
        {
            if (e.getSource() == this.buttons[i] && this.buttons[i].getText().equals("") && this.moves < 2)
            {   //if the card is empty and we under 2 moves
                this.twoMoves[this.index] = i;
                x = i / this.row;
                y = i % this.col;
                this.buttons[i].setText(String.valueOf(this.numbers[x][y]));    //revel the number of the card
                this.moves++;
                this.index++;
                this.labelPoints.setText("    pairs found " + this.pairs + " of " + this.totalPairs
                        + "    player 1 pairs = " + this.player1Pairs + "    player 2 pairs = " + (this.pairs - this.player1Pairs));
                if (this.player1Turn)
                    this.buttons[i].setBackground(Color.LIGHT_GRAY);
                else
                    this.buttons[i].setBackground(Color.CYAN);
            }
        }
        if (e.getSource() == this.btnExit)  //exit the game
        {
            int a = JOptionPane.showConfirmDialog(this.frame, "Are you sure?");
            if (a == JOptionPane.YES_OPTION)
            {
                this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
        if (e.getSource() == this.btnNew)   //start a new game
        {
            newGame();
        }

    }

    private void newGame()
    {
        this.moves = 0; //reset all the parameter
        this.index = 0;
        this.pairs = 0;
        this.player1Pairs = 0;
        turn();
        this.twoMoves[0] = Integer.MAX_VALUE;
        this.twoMoves[1] = Integer.MAX_VALUE;
        this.label.setText("Welcome to Memory");
        this.labelPoints.setText("    pairs found " + this.pairs + " of " + this.totalPairs
                + "    player 1 pairs = " + this.player1Pairs + "    player 2 pairs = " + (this.pairs - this.player1Pairs));
        build();
    }

    private void build()    //function that build a matrix of pair randomly
    {
        for (int i = 0; i < this.buttons.length; i++)
        {
            this.buttons[i].setText("");
            this.buttons[i].setBackground(base);
        }
        for (int i = 0; i < this.numbers.length; i++)   //reset the matrix
        {
            for (int j = 0; j < this.numbers[0].length; j++)
            {
                this.numbers[i][j] = 0;
            }
        }
        Random r = new Random();
        int fill = 0;
        int x1 = 0;
        int y1 = 0;
        int k = 1;
        int x2 = 0;
        int y2 = 0;
        boolean found = false;
        while (fill != (row * col)) //while we not finish filling the matrix
        {
            found = false;
            x1 = r.nextInt(row);
            y1 = r.nextInt(col);
            if (this.numbers[x1][y1] == 0)
            {
                this.numbers[x1][y1] = k;
                fill++;
                while (found == false)
                {
                    x2 = r.nextInt(row);
                    y2 = r.nextInt(col);
                    if (this.numbers[x2][y2] == 0)
                    {
                        this.numbers[x2][y2] = k;
                        found = true;
                        k++;
                        fill++;
                    }
                }
            }
        }
        //print the matrix
        for (int i = 0; i < this.numbers.length; i++)
        { //this equals to the row in our matrix.
            for (int j = 0; j < this.numbers[i].length; j++)
            { //this equals to the column in each row.
                System.out.print(this.numbers[i][j] + " ");
            }
            System.out.println(); //change line on console as row comes to end in the matrix.
        }

    }

    private void init() //function that init all the screen
    {
        this.numbers = new int[row][col];
        this.twoMoves = new int[2];
        this.frame = new JFrame();
        this.panelText = new JPanel();
        this.panelBtn = new JPanel();
        this.label = new JLabel();
        this.buttons = new JButton[row * col];
        this.panelMenu = new JPanel();
        this.btnNew = new JButton();
        this.btnExit = new JButton();
        this.labelPoints = new JLabel();
        this.label.setText("Welcome to Memory");

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        this.frame.setSize((int) (width / 1.5), (int) (height / 1.2));
        this.frame.getContentPane().setBackground(new Color(50, 50, 50));
        this.frame.setTitle("Memory");
        this.frame.setLayout(new BorderLayout());
        this.frame.setVisible(true);

        this.btnNew.addActionListener(this);
        this.btnExit.addActionListener(this);
        this.btnNew.setFont(new Font("Ink Free", Font.BOLD, 20));
        this.btnExit.setFont(new Font("Ink Free", Font.BOLD, 20));
        this.btnNew.setText("New game");
        this.btnExit.setText("Exit");
        this.btnNew.setFocusable(false);
        this.btnExit.setFocusable(false);

        this.labelPoints.setForeground(Color.BLACK);
        this.labelPoints.setFont(new Font("Ink Free", Font.BOLD, 20));
        this.labelPoints.setText("    pairs found " + this.pairs + " of " + this.totalPairs
                + "    player 1 pairs = " + this.player1Pairs + "    player 2 pairs = " + (this.pairs - this.player1Pairs));


        this.labelPoints.setHorizontalAlignment(JLabel.CENTER);
        this.labelPoints.setOpaque(true);

        this.panelMenu.add(this.btnNew);
        this.panelMenu.add(this.btnExit);
        this.panelMenu.add(this.labelPoints);

        this.label.setBackground(new Color(120, 20, 124));
        this.label.setForeground(new Color(25, 255, 0));
        this.label.setFont(new Font("Ink Free", Font.BOLD, 50));
        this.label.setHorizontalAlignment(JLabel.CENTER);
        this.label.setOpaque(true);


        this.panelText.setLayout(new BorderLayout());
        this.panelText.add(this.label);

        this.panelBtn.setLayout(new GridLayout(row, col));
        this.panelBtn.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < this.buttons.length; i++)
        {
            this.buttons[i] = new JButton();
            this.buttons[i].addActionListener(this);
            this.buttons[i].setFont(new Font("Ink Free", Font.BOLD, 50));
            this.buttons[i].setFocusable(false);
            this.panelBtn.add(this.buttons[i]);
        }

        this.base = this.buttons[0].getBackground();
        this.frame.add(this.panelText, BorderLayout.NORTH);
        this.frame.add(this.panelBtn, BorderLayout.CENTER);
        this.frame.add(this.panelMenu, BorderLayout.SOUTH);
        build();
    }
}

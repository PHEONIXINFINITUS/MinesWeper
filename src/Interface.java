import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface extends JFrame{
    public static final JPanel mainPanel = makeMainInterface();
    public static JPanel gamePanel;
    private static int numberOfBombs ;
    public static JPanel rootPane=new JPanel();
    private static int clockTime = 60*10;
    public static JPanel grid ;
    public static Tile gridArray[][];
    public static int dim;
    static boolean intilized = false;
    static Timer timerThread;
    static JFrame jframe;
    static JFrame getInterface()
    {
        if(jframe==null)
        {
            jframe = new Interface();
            Interface.setRootPane(mainPanel);
        }
        return jframe;
    }
    Interface(){
        this.setTitle("Mines Weeper");
        this.setSize(520, 520);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    private static void setRootPane(JPanel panel)
    {
        getInterface().setContentPane(panel);
        getInterface().revalidate();
        getInterface().repaint();
    }
    private static JPanel makeMainInterface()
    {
        JPanel root = new JPanel(new GridLayout(0,1));
        Label welcome = new Label("Welcome to mines weeper game .please choose the size of the grid to play",SwingConstants.CENTER);
        root.add(welcome);
        Label dimentionLabel = new Label("Dimention of the grid : ");
        TextField dimentionOfGrid = new TextField("8");
        Button confirm = new Button("play"); 
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dim = Integer.parseInt(dimentionOfGrid.getText());
                    gamePanel = makeGameInterface(dim);
                    setRootPane(gamePanel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "wrong dimention", "ERROR", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                
            }
        });
        root.add(dimentionLabel);
        root.add(dimentionOfGrid);
        root.add(confirm);
        return root;
    }
    private static JPanel makeGameInterface(int dim)
    {
        intilized=false;
        JPanel root = new JPanel(new BorderLayout());
        JPanel status = new JPanel(new FlowLayout());
        Label clock = new Label();
        Label marked = new Label();
        Label numberOfBombsLabel = new Label();
        status.add(clock);
        status.add(marked);
        status.add(numberOfBombsLabel);
        startTimer(clock);
        root.add(status, BorderLayout.NORTH);
        grid = new JPanel(new GridLayout(dim, dim,5,5));
        numberOfBombs = dim;
        numberOfBombsLabel.setText("number of bombs: "+numberOfBombs);
        gridArray = new Tile[dim][dim];
        makeGrid(grid,dim);
        root.add(grid,BorderLayout.CENTER);
        return root;
    }
    private static void startTimer(Label label){
        int delay = 1000; //milliseconds
        clockTime=0;
  ActionListener taskPerformer = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        label.setText("clock: "+(clockTime));
        clockTime++;
        label.revalidate();
        label.repaint();
    }
  };
    timerThread = new Timer(delay, taskPerformer);
    timerThread.start();
    }
    public static void stopTimer()
    {
        timerThread.stop();
    }
    private static void makeGrid(JPanel grid,int dim){
        System.out.println(dim);
        System.out.println(grid.getLayout());
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                Tile tile = new Tile(i,j);
                grid.add(tile);
                gridArray[i][j]=tile;
                System.out.println(grid.getComponentCount());
                
            }
        }
    }
    public static void intilizeGrid(JPanel grid,Tile gridArray[][]){
        for (int i = 0; i < gridArray.length; i++) {
            for (int j = 0; j < gridArray.length; j++) {
                Tile tile = new Tile(i,j);
                
                if (gridArray[i][j].isMined) {
                    tile.setMined();
                }
                grid.add(tile);
            }
        }
    }
    public static void intilize(Tile gridArray[][],int dim,int stx,int sty)
    {
        for (int i = 0; i < numberOfBombs; i++) {
            int row = getRandomInRange(0, dim-1);
            int col = getRandomInRange(0, dim-1);
            if(!gridArray[row][col].isMined() && stx!=row && sty!=col)
            gridArray[row][col].setMined();
            else
            i--;
        }
        setArrayValues(gridArray, dim);
    }
    private static void setArrayValues(Tile gridArray[][],int dim)
    {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if(!gridArray[i][j].isMined)
                gridArray[i][j].setNumberOfBombs(calcBombs(gridArray, dim, i, j));
            }
        }
    }
    private static int calcBombs(Tile gridArray[][],int dim,int i,int j)
    {
        int num = 0;
        for (int x = -1; x < 2; x++) 
        for (int y = -1; y < 2; y++) 
            if(!(x==0 && y==0))
            if(i+x>=0&&i+x<dim&&j+y>=0&&j+y<dim)
            if(gridArray[i+x][j+y].isMined())
                num++;
        return num;
    }
    private static int getRandomInRange(int min,int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    public static void gameOver(boolean win)
    {
        stopTimer();
        if(!win)
        {
            Tile.playAudio("lose_minesweeper");
        }
        else
        {
            Tile.playAudio("win");
        }
        JDialog dialog = new JDialog(null, (win?"Congratiolations":"Loser"), JDialog.DEFAULT_MODALITY_TYPE);
        Label message = new Label("you "+(win?"win":"lost")+"\n want to play again");
        dialog.add(message);
        JButton button = new JButton("Play Again");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRootPane(mainPanel);
                dialog.dispose();
            }
            
        });
        dialog.add(button);
        dialog.setSize(520,300);
        dialog.setVisible(true);
    }
    public static boolean reveal(int i ,int j){
        if(gridArray[i][j].isMined||gridArray[i][j].isRevealed)
        {
            return false;
        }
        gridArray[i][j].setRevealed(true);
        for (int x = -1; x < 2; x++) 
        for (int y = -1; y < 2; y++) {
            if(!(x==0 && y==0))
            if(i+x>=0&&i+x<dim&&j+y>=0&&j+y<dim)
            {
                System.out.print((i+x)+","+(j+y)+" ");
                if(!gridArray[i+x][j+y].isMined()&&gridArray[i+x][j+y].getNumberOfBombs()==0)
                reveal(i+x, j+y);
                if(gridArray[i][j].getNumberOfBombs()==0)
                revealNonRecursive(i, j);
                // gridArray[i+x][j+y].setRevealed(true);
            }
        }
            return true;
    }
    public static boolean revealNonRecursive(int i ,int j){
        if(gridArray[i][j].isMined)
        {
            return false;
        }
        for (int x = -1; x < 2; x++) 
        for (int y = -1; y < 2; y++) {
            if(!(x==0 && y==0))
            if(i+x>=0&&i+x<dim&&j+y>=0&&j+y<dim)
            {
                if(gridArray[i+x][j+y].numberOfBombs!=0)
                gridArray[i+x][j+y].setRevealed(true);
            }
        }
            return true;
    }
    public static boolean isWin()
    {
        for (int i = 0; i < gridArray.length; i++) {
            for (int j = 0; j < gridArray.length; j++) {
                if(!gridArray[i][j].isMined&&!gridArray[i][j].isRevealed)
                return false;
            }
        }
        return true;
    }
}

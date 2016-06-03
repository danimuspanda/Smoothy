package trabalhoteste;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class TesteGame {

    public static void main(String[] args) {
        new TesteGame();
    }
    private CellPane[][] board = new CellPane[ROWS][COLS];
    private List<CellPane[][]> boardList = new ArrayList<CellPane[][]>();
    private static final int ROWS = 8;
	private static final int COLS = 8;
	GridBagConstraints gbc;
	
    public TesteGame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new GridLayout());
                frame.add(new BoardGame());
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class BoardGame extends JPanel {
    	
	private static final long serialVersionUID = 1L;
    	
    	
        public BoardGame() {
            setLayout(new GridBagLayout());

            gbc = new GridBagConstraints();
            
            
            for (int row = 0; row < ROWS; row++) {
            	for (int col = 0; col < COLS; col++){
                    gbc.gridx = col+1;
                    gbc.gridy = row+1;

                    CellPane cellPane = new CellPane();

                    Border border = null;
                    if (row <= COLS) {
                        if (col <= COLS) {
                            border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                        }
                    } else {
                        if (col < COLS) {
                            border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                        }
                    }
                    cellPane.setBorder(border);
                    add(cellPane, gbc);
                    board[col][row] = cellPane;//add CellPane tile to the board
                    
                    
            	}
                
            }
            boardList.add(board);
            
        }
    
	    
    }
    public class CellPane extends JPanel
    {

        
	private static final long serialVersionUID = 1L;
	public ArrayList<Color> colors = new ArrayList<Color>();
	private Color Background;
		
        public CellPane() 
        {
        	
        	ColorsCreation();
        	
        	drawImage();
        	
        	
            addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
                @Override
                public void mouseClicked(MouseEvent e)
                {
                	List<CellPane> floodedTiles = new ArrayList<CellPane>();
                	//board cell clicked
                	CellPane startingTile = board[e.getComponent().getX()/50][e.getComponent().getY()/50];
                	int x = startingTile.getX()/50;//x current tile
                	int y = startingTile.getY()/50;//y of current tile
                	//need to divide by 50 to get the right values 
                	floodedTiles.add(startingTile);
                	
                	floodedTiles.addAll(BFS(startingTile));
                	
                	Flood(startingTile, floodedTiles);
                	
                	//CheckBoard();
                	
                }	
            });
        }
        public void CheckBoard()
        {
        	for (CellPane[] tile: board){
        		
        	}
        }
        public void Flood(CellPane currentTile, List<CellPane> floodedTiles){
        	
        	Set<CellPane> newFloodedTiles = new HashSet<CellPane>();
        	
        	newFloodedTiles = BFS(currentTile);
        	
        	for (CellPane tile: floodedTiles)
        	{
        		board[tile.getX()/50][tile.getY()/50].setBackground(Color.BLACK);
        	}
        }
        public Set<CellPane> BFS(CellPane startingTile)
        {
        	CellPane currentTile = startingTile;
        	
        	List<CellPane> queue = new ArrayList<CellPane>();
        	queue.add(currentTile);
        	Set<CellPane> floodedTiles = new HashSet<CellPane>();
        	while (queue.size()-1 >= 0)
        	{
        		currentTile = queue.get(queue.size()-1);
            	queue.remove(queue.size()-1);
            	if (currentTile.getBackground().equals(currentTile.getBackground()))
            	{
            		floodedTiles.add(currentTile);        			
            		queue.addAll(getOtherCells(currentTile, floodedTiles));
        		}
        		
        	}
			return floodedTiles;
        	
        }
        /*
         * for neighbor in neighbor in checkNeighbors if neighbor not in floodedTiles
         * method Breadth first search
         */
        public List<CellPane> getOtherCells(CellPane currentTile, Set<CellPane> floodedTiles){
        	List<CellPane> listResult = new ArrayList<CellPane>();
         	Map<CellPane, List<Integer>> otherTiles = new HashMap<CellPane, List<Integer>>();
        	otherTiles = checkNeighbors(currentTile, currentTile.getX()/50, currentTile.getY()/50);//checkNeighbors(tile)
        	
        	for (CellPane ladrilho: otherTiles.keySet())
        	{//for each tile in otherTiles keys
        		if (!floodedTiles.contains(ladrilho))
        		{//add to listResult if not in floodedTiles
        			listResult.add(ladrilho);
        		}
        	}
			return listResult;
        	
        }
        //this method creates a map of neighbors of tile
        //each key is a CellPane Object
        //each value is a list with x and y
		public Map<CellPane, List<Integer>> checkNeighbors(CellPane tile, int x, int y){
			Map<CellPane, List<Integer>> neighbors = new HashMap<CellPane, List<Integer>>();
        	/*
        	 * (0,-1) - SOUTH
        	 * (0,1) - NORTH
        	 * (-1,0) - WEST
        	 * (1,0) - EAST
        	 */
			int[] x_offset = { 0, 0,1, -1};
        	int[] y_offset = {-1, 1,0, 0};
        	for (int xs: x_offset){
        		for (int ys:y_offset){
	        		int tmpX = x + xs;
	        		int tmpY = y + ys;
	        		
	        		List<Integer> cords= new ArrayList<Integer>();//coordenates of each tile.example (tileA, x, y)
	        		//each tile is a key in neighbors
	        		if ((0 <= tmpX && tmpX < ROWS) && (0 <= tmpY && tmpY< COLS)){
	        			CellPane current = board[tmpX][tmpY];
	        			if (board[tmpX][tmpY].getBackground().equals(tile.getBackground())){
	        				cords.add(tmpX);
	    	        		cords.add(tmpY);
	        				neighbors.put(current, cords);
	        			}
	        		}	        		
        		}
        	}
    		return neighbors;
        	
        }
        
		@Override
        public Dimension getPreferredSize() 
		{
            return new Dimension(50, 50);
        }
        public void ColorsCreation() 
        {

    		/*
    		 * colors to remember:
    		 * black = 0xff000000
    		 * red   = 0x00ff0000
    		 * green = 0x0000ff00
    		 * blue  = 0x000000ff
    		 * white = 0xffffffff 
    		 */
    		Color green = new Color(0x0000ff00);
    		Color black = new Color(0xff00ff00);
    		Color blue = new Color(0x004FFF);
    		Color red = new Color(0xff0000);
    		Color yellow = new Color(0xFFFF00);
    		colors.add(green);
    		colors.add(black);
    		colors.add(blue);
    		colors.add(red);
    		colors.add(yellow);
    		
        }
        public void drawImage() 
        {
        	Random r = new Random();
    		if (Background == null) 
    		{
    			//give tiles random colors from colors list
    			Background = colors.get(r.nextInt(colors.size()));
    			setBackground(Background);
    		}else
    		{
    			//black color means no tile
    			
    			Background = Color.black;
    		}
        }
		public Color getbackground(){
			return Background;
		}
		public void setbackground(Color background) {
			this.Background = background;
		}
    }
}

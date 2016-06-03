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
    	/**
		 * 
		 */
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
                    add(cellPane, gbc);//adicionar o ladrilho ao painel do GUI
                    board[col][row] = cellPane;//criar tabuleiro do jogo
                    
                    
            	}
                
            }
            boardList.add(board);
            start();
        }
    
	    public void start()
	    {
	    		
	    }
    }
    public class CellPane extends JPanel
    {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public ArrayList<Color> cores = new ArrayList<Color>();
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
                	int x = startingTile.getX()/50;//x do ladrilho actual
                	int y = startingTile.getY()/50;//y do ladrilho actual
                	
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
        	
        	for (CellPane tile: floodedTiles){
        		//tile.setBackground(Color.black);//flood os ladrilhos designados a null
        		//for (List<Integer> value: floodedTiles.values()){
        			
        			board[tile.getX()/50][tile.getY()/50].setBackground(Color.BLACK);
        		//}
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
         * Para cada vizinho nos vizinhos no checkNeighbors e se vizinho nao estiver nos já inundados, retorna uma List
         * um metodo para ajudar no metodo BFS
         */
        public List<CellPane> getOtherCells(CellPane currentTile, Set<CellPane> floodedTiles){
        	List<CellPane> listResult = new ArrayList<CellPane>();//lista a retornar
         	Map<CellPane, List<Integer>> otherTiles = new HashMap<CellPane, List<Integer>>();//mapa par
        	otherTiles = checkNeighbors(currentTile, currentTile.getX()/50, currentTile.getY()/50);//checkNeighbors(tile)
        	
        	for (CellPane ladrilho: otherTiles.keySet())
        	{//para cada ladrilho nas chaves do mapa otherTiles
        		if (!floodedTiles.contains(ladrilho))
        		{//se ladrilho nao estiver em floodedTiles
        			listResult.add(ladrilho);
        		}
        	}
			return listResult;
        	
        }
        //este metedo cria um mapa com as chaves as celulas e cada chave tem um x e um y
        //das celulas que tem cor igual
        //verifica na horizontal e vertical
		public Map<CellPane, List<Integer>> checkNeighbors(CellPane tile, int x, int y){
			Map<CellPane, List<Integer>> neighbors = new HashMap<CellPane, List<Integer>>();
        	/*
        	 * (0,-1) - Sul
        	 * (0,1) - Norte
        	 * (-1,0) - Oeste
        	 * (1,0) - Este
        	 */
			int[] x_offset = { 0, 0,1, -1};
        	int[] y_offset = {-1, 1,0, 0};
        	for (int xs: x_offset){
        		for (int ys:y_offset){
	        		int tmpX = x + xs;
	        		int tmpY = y + ys;
	        		//System.out.println(tmpX + "   " + tmpY);
	        		List<Integer> cords= new ArrayList<Integer>();//lista para conter as cordenadas dos ladrilhos
	        		//cada ladrilho será uma chave no mapa neighbors
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
    		//graphics2D é como um pincel, ladrilho é como o papel
    		
    		/*
    		 * nota sobre as cores:
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
    		cores.add(green);
    		cores.add(black);
    		cores.add(blue);
    		cores.add(red);
    		cores.add(yellow);
    		
        }
        public void drawImage() 
        {
        	Random r = new Random();
    		if (Background == null) 
    		{
    			//se nao tiver cor, gerar uma cor
    			//por agora será aleatorio
    			//talves faça uma lista de cores que quero especificamente
    			Background = cores.get(r.nextInt(cores.size()));//esta linha modifica as cores que o ladrilho terá
    			setBackground(Background);
    		}else
    		{
    			//cor preta para deixar de existir
    			//ou entao remover completamente
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

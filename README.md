# Smoothy
GUI Java Game

Objective: 
Player have a Gui board of type CellPane, the board is 2 dimension.
Board and CellPane extends JPanel and each CellPane in the array board have a mouse listener
To play, the user clicks on a cell, for example cell named A, and if there are others Cell's with the same color, horizontally and vertically, those cell's change color to black. By clicking in a cell, it runs a breadth first search to get all cell's with the same color. The game stops when there are no more possible plays.

Rules:
To click on a cell, it must have a same color cell as neighbor.
The score is the number of original Cell's - the number of black color cell's.
When a cell is changed to black, all cell's that are above it fall vertically.
When there are no cell's in a whole column, the next columns move to to right.

Problems:
It's not searching for neighbors only in horizontally and vertically, it is also looking in diagonal.


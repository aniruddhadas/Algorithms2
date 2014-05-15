import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {

	private Picture cachedPicture;
	private int currentWidth, currentHeight;
	private Node[][] matrix;

	private static final float MAXENERGY = 195075;

	public SeamCarver(Picture inputImg) {
		// TODO Auto-generated constructor stub
		this.cachedPicture = inputImg;

		this.currentWidth = this.cachedPicture.width();
		this.currentHeight = this.cachedPicture.height();

		// build up the energy matrix
		this.BuildInitialMatrix();
	}

	private void BuildInitialMatrix() {
		matrix = new Node[this.currentHeight][];
		for (int y = 0; y < this.currentHeight; y++) {
			this.matrix[y] = new Node[this.currentWidth];
			for (int x = 0; x < this.currentWidth; x++) {
				this.SetMatrixItem(x, y, new Node(this.computeEnergy(x, y), x,
						y, this.cachedPicture.get(x, y)));
			}
		}
	}

	private void SetMatrixItem(int x, int y, Node node) {
		// TODO Auto-generated method stub
		// int pos = this.GetAbsolutePosition(x, y);
		this.matrix[y][x] = node;
	}

	private Node GetMatrixItem(int x, int y) {
		// TODO Auto-generated method stub
		// int pos = this.GetAbsolutePosition(x, y);
		return this.matrix[y][x];
	}

	// height of current picture
	public int height() {
		// TODO Auto-generated method stub
		return this.currentHeight;
	}

	// width of current picture
	public int width() {
		// TODO Auto-generated method stub
		return this.currentWidth;
	}

	private void DumpMatrix() {
		for (int x = 0; x < this.currentWidth; x++) {
			for (int y = 0; y < this.currentHeight; y++) {
				Node currentNode = this.getNode(x, y);
				String pathToX = currentNode.getPathTo() == null ? "(null)"
						: currentNode.getPathTo().getX() + "";
				String pathToY = currentNode.getPathTo() == null ? "(null)"
						: currentNode.getPathTo().getY() + "";

				// System.out.printf("x:%s y:%s E:%s totalE:%s pathToX:%s pathToY:%s\n",
				// currentNode.myPosition.x, currentNode.myPosition.y,
				// currentNode.energy, currentNode.totalEnergy, pathToX,
				// pathToY);
			}
		}
	}

	public int[] findHorizontalSeam() {
		// everything else's total energy as Double.MAX_VALUE;
		this.ResetTotalEnergies();
		this.ComputeHorizontalStartingEnergies();
		this.SetupHorizontalRip();
		return this.GetHorizontalRipPositions();
	}

	public int[] findVerticalSeam() {
		// everything else's total energy as Double.MAX_VALUE;
		this.ResetTotalEnergies();
		this.ComputeVerticalStartingEnergies();
		this.SetupVerticalRip();
		return this.GetVerticalRipPositions();
	}

	private int[] GetHorizontalRipPositions() {
		int x = this.currentWidth - 1;

		Node minNode = getNode(x, 0);
		for (int y = 1; y < this.currentHeight; y++) {
			Node currNode = getNode(x, y);
			if (minNode.getTotalEnergy() > currNode.getTotalEnergy()) {
				minNode = currNode;
			}
		}

		int[] returnVal = new int[this.currentWidth];
		while (minNode.getPathTo() != null) {
			returnVal[minNode.myPosition().getX()] = minNode.myPosition()
					.getY();
			minNode = getNode(minNode.getPathTo());
		}
		returnVal[minNode.myPosition().getX()] = minNode.myPosition().getY();
		return returnVal;
	}

	private int[] GetVerticalRipPositions() {
		// pick bottom row min and reverse onto a stack
		int y = this.currentHeight - 1;

		Node minNode = getNode(0, y);
		for (int x = 1; x < this.currentWidth; x++) {
			Node currNode = getNode(x, y);
			if (minNode.getTotalEnergy() > currNode.getTotalEnergy()) {
				minNode = currNode;
			}
		}

		int[] returnVal = new int[this.currentHeight];
		while (minNode.getPathTo() != null) {
			returnVal[minNode.myPosition().getY()] = minNode.myPosition()
					.getX();
			minNode = getNode(minNode.getPathTo());
		}
		returnVal[minNode.myPosition().getY()] = minNode.myPosition().getX();
		return returnVal;
	}

	private void ComputeHorizontalStartingEnergies() {
		// everything else's total energy as Double.MAX_VALUE;
		// keep x fixed and walk y down to height resetting total energy to node
		// energy
		int x = 0;

		for (int y = 0; y < this.currentHeight; y++) {
			Node node = this.getNode(x, y);
			node.setTotalEnergy(node.getEnergy());
		}
	}

	private void ComputeVerticalStartingEnergies() {
		for (int x = 0; x < this.currentWidth; x++) {
			Node node = this.getNode(x, 0);
			node.setTotalEnergy(node.getEnergy());
		}
	}

	private void ResetTotalEnergies() {
		for (int x = 0; x < this.currentWidth; x++) {
			for (int y = 0; y < this.currentHeight; y++) {
				Node node = this.getNode(x, y);
				node.setTotalEnergy(Float.MAX_VALUE);
				node.setPathTo(null);
			}
		}
	}

	private Node getNode(int x, int y) {
		// TODO Auto-generated method stub
		return this.GetMatrixItem(x, y);
	}

	private void SetupHorizontalRip() {
		// TODO Auto-generated method stub
		// foreach column and each nodes in column
		// find west to east neighbors
		// visit
		// relax

		for (int x = 0; x < this.currentWidth; x++) {
			for (int y = 0; y < this.currentHeight; y++) {
				Node currentNode = this.getNode(x, y);
				if (x == this.width() - 1) {
					continue;
				}

				if (y >= 1) {
					// northeast
					Node adjNode = this.getNode(x + 1, y - 1);

					// converts to grey node
					float pathEnergy = currentNode.getTotalEnergy()
							+ adjNode.getEnergy();
					if (pathEnergy < adjNode.getTotalEnergy()) {
						adjNode.setTotalEnergy(pathEnergy);
						adjNode.setPathTo(currentNode.myPosition());
					}
					// positions.add(new Position(x + 1, y - 1));
				}

				// east
				Node adjNode = this.getNode(x + 1, y);

				// converts to grey node
				float pathEnergy = currentNode.getTotalEnergy()
						+ adjNode.getEnergy();
				if (pathEnergy < adjNode.getTotalEnergy()) {
					adjNode.setTotalEnergy(pathEnergy);
					adjNode.setPathTo(currentNode.myPosition());
				}
				// positions.add(new Position(x + 1, y));

				if (y < this.height() - 1) {
					// southeast
					adjNode = this.getNode(x + 1, y + 1);

					// converts to grey node
					pathEnergy = currentNode.getTotalEnergy()
							+ adjNode.getEnergy();
					if (pathEnergy < adjNode.getTotalEnergy()) {
						adjNode.setTotalEnergy(pathEnergy);
						adjNode.setPathTo(currentNode.myPosition());
					}
					// positions.add(new Position(x + 1, y + 1));
				}

				// for (Position adjPosition :
				// this.getAdjListWestEast(currentNode.myPosition)) {
				// Node adjNode = this.getNode(adjPosition);
				//
				// // converts to grey node
				// double pathEnergy = currentNode.getTotalEnergy() +
				// adjNode.getEnergy();
				// if (pathEnergy < adjNode.getTotalEnergy()) {
				// adjNode.setTotalEnergy(pathEnergy);
				// adjNode.setPathTo(currentNode.myPosition);
				// }
				// }
			}
		}
	}

	private void SetupVerticalRip() {
		for (int y = 0; y < this.currentHeight; y++) {
			for (int x = 0; x < this.currentWidth; x++) {
				Node currNode = this.getNode(x, y);
				// 1. find all adjecent nodes
				// 2. foreach node

				if (y == this.height() - 1) {
					continue;
				}

				if (x >= 1) {
					// southwest
					// a. conditionally set min energy, and path to
					Node adjNode = this.getNode(x - 1, y + 1);

					// converts to grey node
					float pathEnergy = currNode.getTotalEnergy()
							+ adjNode.getEnergy();
					if (pathEnergy < adjNode.getTotalEnergy()) {
						adjNode.setTotalEnergy(pathEnergy);
						adjNode.setPathTo(currNode.myPosition());
					}
					// positions.add(new Position(x - 1, y + 1));
				}

				// south
				Node adjNode = this.getNode(x, y + 1);

				// converts to grey node
				float pathEnergy = currNode.getTotalEnergy()
						+ adjNode.getEnergy();
				if (pathEnergy < adjNode.getTotalEnergy()) {
					adjNode.setTotalEnergy(pathEnergy);
					adjNode.setPathTo(currNode.myPosition());
				}
				// positions.add(new Position(x, y + 1));

				if (x < this.width() - 1) {
					// southeast
					adjNode = this.getNode(x + 1, y + 1);

					// converts to grey node
					pathEnergy = currNode.getTotalEnergy()
							+ adjNode.getEnergy();
					if (pathEnergy < adjNode.getTotalEnergy()) {
						adjNode.setTotalEnergy(pathEnergy);
						adjNode.setPathTo(currNode.myPosition());
					}
					// positions.add(new Position(x + 1, y + 1));
				}
				// for (Position adjPosition :
				// this.getAdjListNorthSouth(currNode.myPosition)) {
				// // a. conditionally set min energy, and path to
				// Node adjNode = this.getNode(adjPosition);
				//
				// // converts to grey node
				// double pathEnergy = currNode.getTotalEnergy() +
				// adjNode.getEnergy();
				// if (pathEnergy < adjNode.getTotalEnergy()) {
				// adjNode.setTotalEnergy(pathEnergy);
				// adjNode.setPathTo(currNode.myPosition);
				// }
				// }
			}
		}
	}

	private Node getNode(Position position) {
		return this.getNode(position.getX(), position.getY());
	}

	public void removeHorizontalSeam(int[] horizontalSeam) {
		this.GuardForMinDimension(horizontalSeam.length, false);
		this.ResetPicture();
		// he gives us the X indicies that we need to remove

		// when we remove a horizontal seam go col by col and shift all elements
		// after the rip element up by one.
		// repeat for number of cols
		for (int x = 0; x < this.currentWidth; x++) {
			int copyToRow = horizontalSeam[x];

			if ((x != 0 && !(horizontalSeam[x] == horizontalSeam[x - 1]
					|| horizontalSeam[x] == horizontalSeam[x - 1] + 1 || horizontalSeam[x] == horizontalSeam[x - 1] - 1))) {
				throw new IllegalArgumentException();
			}

			while (copyToRow + 1 < this.currentHeight) {
				// shift north (up)
				Node fromNode = this.matrix[copyToRow + 1][x];
				this.matrix[copyToRow][x] = fromNode;
				fromNode.SetPostion(x, copyToRow);
				// fromNode.AssertPosition(x, copyToRow);

				copyToRow++;
			}
		}

		// final step. reduce height before we recompute the energy for the
		// impacted rip items.
		this.currentHeight--;

		for (int x = 0; x < this.currentWidth; x++) {
			// recompute energy for x-1,y and x,y
			int y = horizontalSeam[x];

			// update energy for an item that may have shifted into the current
			// rip slot from bottom up
			SafeSetRipEnergy(x, y);

			// update energy for an item north of rip slot
			SafeSetRipEnergy(x, y - 1);
		}
	}

	public void removeVerticalSeam(int[] verticalSeam) {
		this.GuardForMinDimension(verticalSeam.length, true);

		this.ResetPicture();
		// he gives us the Y indicies that we need to remove

		// when we remove a vertical seam go row by row and shift all elements
		// after the rip element one back.
		// repeat for number of rows
		for (int y = 0; y < this.currentHeight; y++) {
			int copyToPos = verticalSeam[y];

			if ((y != 0 && !(verticalSeam[y] == verticalSeam[y - 1]
					|| verticalSeam[y] == verticalSeam[y - 1] + 1 || verticalSeam[y] == verticalSeam[y - 1] - 1))) {
				throw new IllegalArgumentException();
			}

			while (copyToPos + 1 < this.currentWidth) {
				// shift west (left)
				Node fromPostion = this.matrix[y][copyToPos + 1];
				this.matrix[y][copyToPos] = fromPostion;
				fromPostion.SetPostion(copyToPos, y);
				// update position

				// this.matrix[y][copyToPos].AssertPosition(copyToPos, y);

				copyToPos++;
			}
		}

		// final step. reduce width before we recompute the energy for the
		// impacted rip items.
		this.currentWidth--;

		// todo: recompute energy with verticalSeam x 2 for the items on the
		// seam
		for (int y = 0; y < this.currentHeight; y++) {
			// recompute energy for x-1,y and x,y
			int x = verticalSeam[y];

			// update energy for an item that may have shifted into the current
			// rip slot
			SafeSetRipEnergy(x, y);

			// update energy for an item that may be west (left) of the rip
			SafeSetRipEnergy(x - 1, y);
		}
	}

	private void GuardForMinDimension(int seamLength, boolean isVertical) {
		// TODO Auto-generated method stub
		if (this.currentHeight <= 1 || this.currentWidth <= 1) {
			throw new IllegalArgumentException();
		}
		if (isVertical) {
			if (seamLength != this.currentHeight) {
				throw new IllegalArgumentException();
			}
		} else {
			if (seamLength != this.currentWidth) {
				throw new IllegalArgumentException();
			}
		}
	}

	private void SafeSetRipEnergy(int x, int y) {
		if (x < 0) {
			return;
		}

		if (y < 0) {
			return;
		}

		matrix[y][x].setEnergy(this.computeEnergy(x, y));
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		return (double) this.GetMatrixItem(x, y).getEnergy();
	}

	private float computeEnergy(int x, int y) {
		// TODO: throw IndexOutOfBoundsException
		// TODO Auto-generated method stub
		// this.picture().get(i, j)
		// if it is in the border return 195075
		if (y == 0) {
			return MAXENERGY;
		}
		if (x == 0) {
			return MAXENERGY;
		}

		if ((this.currentWidth - 1) == x) {
			return MAXENERGY;
		}

		if ((this.currentHeight - 1) == y) {
			return MAXENERGY;
		}

		int downR;
		int downG;
		int downB;
		int upR;
		int upG;
		int upB;
		int rightR;
		int rightG;
		int rightB;
		int leftR;
		int leftG;
		int leftB;

		if (this.cachedPicture != null) {
			Color theColor = this.picture().get(x, y + 1);
			downR = theColor.getRed();
			downG = theColor.getGreen();
			downB = theColor.getBlue();

			theColor = this.picture().get(x, y - 1);
			upR = theColor.getRed();
			upG = theColor.getGreen();
			upB = theColor.getBlue();

			theColor = this.picture().get(x + 1, y);
			rightR = theColor.getRed();
			rightG = theColor.getGreen();
			rightB = theColor.getBlue();

			theColor = this.picture().get(x - 1, y);
			leftR = theColor.getRed();
			leftG = theColor.getGreen();
			leftB = theColor.getBlue();
		} else {
			Node node = this.getNode(x, y + 1);

			downR = node.GetR();
			downG = node.GetG();
			downB = node.GetB();

			node = this.getNode(x, y - 1);

			upR = node.GetR();
			upG = node.GetG();
			upB = node.GetB();

			node = this.getNode(x + 1, y);

			rightR = node.GetR();
			rightG = node.GetG();
			rightB = node.GetB();

			node = this.getNode(x - 1, y);
			leftR = node.GetR();
			leftG = node.GetG();
			leftB = node.GetB();
		}

		// Rx(x, y)2 + Gx(x, y)2 + Bx(x, y)2
		float deltaX = (float) (Math.pow(rightR - leftR, 2)
				+ Math.pow(rightB - leftB, 2) + Math.pow(rightG - leftG, 2));
		// Calculating y2
		float deltaY = (float) (Math.pow(upR - downR, 2)
				+ Math.pow(upB - downB, 2) + Math.pow(upG - downG, 2));

		return deltaX + deltaY;
	}

	// current picture
	// TODO: remove throws
	public Picture picture() {
		if (this.cachedPicture != null) {
			return this.cachedPicture;
		}

		// TODO: REMOVE THIS SHIT!

		this.BuildAndCachePicture();

		return this.cachedPicture;
	}

	private void ResetPicture() {
		this.cachedPicture = null;
	}

	private void BuildAndCachePicture() {
		// TODO Auto-generated method stub
		Picture pictureTemp = new Picture(this.currentWidth, this.currentHeight);

		for (int y = 0; y < pictureTemp.height(); y++) {
			for (int x = 0; x < pictureTemp.width(); x++) {
				Node node = this.getNode(x, y);
				pictureTemp.set(x, y, node.getColor());
			}
		}

		this.cachedPicture = pictureTemp;
	}

	// private List<Position> getAdjListWestEast(Position position) {
	// // TODO Auto-generated method stub
	// return this.getAdjListWestEast(position.getX(), position.getY());
	// }

//	private List<Position> getAdjListWestEast(int x, int y) {
//		List<Position> positions = new ArrayList<Position>();
//
//		if (x == this.width() - 1) {
//			return positions;
//		}
//
//		if (y >= 1) {
//			// northeast
//			positions.add(new Position(x + 1, y - 1));
//		}
//
//		// east
//		positions.add(new Position(x + 1, y));
//
//		if (y < this.height() - 1) {
//			// southeast
//			positions.add(new Position(x + 1, y + 1));
//		}
//		return positions;
//	}

	// private List<Position> getAdjListNorthSouth(Position position) {
	// return this.getAdjListNorthSouth(position.getX(), position.getY());
	// }

//	private List<Position> getAdjListNorthSouth(int x, int y) {
//		// TODO Auto-generated method stub
//		List<Position> positions = new ArrayList<Position>();
//		if (y == this.height() - 1) {
//			return positions;
//		}
//
//		if (x >= 1) {
//			// southwest
//			positions.add(new Position(x - 1, y + 1));
//		}
//
//		// south
//		positions.add(new Position(x, y + 1));
//
//		if (x < this.width() - 1) {
//			// southeast
//			positions.add(new Position(x + 1, y + 1));
//		}
//
//		return positions;
//	}
}

//class NullPosition extends Position
//{
//	public NullPosition(int x, int y) {
//		super(x, y);
//		// TODO Auto-generated constructor stub
//	}
//}

class Position {
	private short x;
	private short y;

	public Position(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = (short) x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = (short) y;
	}
}

// possible optimizations to reduce the size of Node
// node does not need to know it's location this can be known at search time
// the path to could be represented with a byte indicating which direction from the nort
// was traveled to get to this node, this can be used when unwiding the shortest path
// Color/RGB is expensive, can this be represented with 3 bytes and then cast back to int's? currently repsenting
// as short seems good.
// current size of node: 8 + 4 + 4 + 2 + 2 + 2 + 2 + 2 + 2 + 2
// also the overhead of Node is not great.  we chould shed 8 bytes by just storing the data in separate matrix...
// but then you'd have to resize them all :(
class Node {
	private static final Position POS = new Position(-1, -1);
	// private static NullPosition NULLPOS = new NullPosition(-1, -1)
	
	// 4
	private float energy;
	// Position myPosition;
	// bfs will set these
	// total energy till this point
	
	// 4
	private float totalEnergy;
	// private Position pathTo;
	
	// 2
	private short R;
	
	// 2
	private short G;
	
	// 2
	private short B;
	
	// 2
	private short x;
	
	// 2
	private short y;
	
	// 2
	private short pathToX;
	
	// 2
	private short pathToY;

	public Node(float energy, int x, int y, Color color) {
		// this.myPosition = new Position(x, y);
		this.x = (short) x;
		this.y = (short) y;
		this.setEnergy(energy);
		this.setTotalEnergy(Float.MAX_VALUE);
		R = (short)color.getRed();
		G = (short)color.getGreen();
		B = (short)color.getBlue();
	}

	public Position myPosition() {
		Node.POS.setX(this.x);
		Node.POS.setY(this.y);
		return Node.POS;
	}

	// make object self aware when moved in underlying data structure
	public void SetPostion(Position newPosition) {
		this.x = (short) newPosition.getX();
		this.y = (short) newPosition.getY();
		// this.myPosition = newPosition;
	}

	public void SetPostion(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
		// myPosition.setX(x);
		// myPosition.setY(y);
	}

	public int GetR() {
		return this.R;
	}

	public int GetG() {
		return this.G;
	}

	public int GetB() {
		return this.B;
	}

	public Color getColor() {
		return new Color(this.R, this.G, this.B);
	}

	// public void AssertPosition(int x, int y){
	// if (x != myPosition.getX() || y != myPosition.getY())
	// {
	// @SuppressWarnings("unused")
	// int temp = 1 / 0;
	// }
	// }

	Position getPathTo() {
		if (this.pathToX == -1 || this.pathToY == -1)
		{
			return null;
		}
		
		Node.POS.setX(this.pathToX);
		Node.POS.setY(this.pathToY);
		return Node.POS;
	}

	void setPathTo(Position pathTo) {
		if (pathTo == null) {
			this.pathToX = -1;
			this.pathToY = -1;
		} else {
			this.pathToX = (short) pathTo.getX();
			this.pathToY = (short) pathTo.getY();
		}
	}

	float getTotalEnergy() {
		return totalEnergy;
	}

	void setTotalEnergy(float totalEnergy) {
		this.totalEnergy = totalEnergy;
	}

	float getEnergy() {
		return energy;
	}

	void setEnergy(float energy) {
		this.energy = energy;
	}
}
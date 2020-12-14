import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Board extends Applet implements KeyListener {
		
	private int[][] digits;
	private final int BOARD_SIZE = 600;
	private int numDigits = 2, points = 0;
	private boolean gameOver = false, validMove = false;
	
	public void init() {
		setSize(BOARD_SIZE, BOARD_SIZE + (BOARD_SIZE / 4));
		digits = new int[4][4];
		int randRow1 = (int) (Math.random() * 4.0), randCol1 = (int) (Math.random() * 4.0);
		int randRow2 = (int) (Math.random() * 4.0), randCol2 = (int) (Math.random() * 4.0);
		while (randRow1 == randRow2)
			randRow2 = (int) (Math.random() * 4.0);
		while (randCol1 == randCol2)
			randCol2 = (int) (Math.random() * 4.0);
		digits[randRow1][randCol1] = 2;
		digits[randRow2][randCol2] = 2;
		addKeyListener(this);
	}
	
	public void paint(Graphics page) {
		int size = BOARD_SIZE / 4;
		page.drawLine(size, 0, size, BOARD_SIZE);
		page.drawLine(size * 2, 0, size * 2, BOARD_SIZE);
		page.drawLine(size * 3, 0, size * 3, BOARD_SIZE);
		page.drawLine(0, size, BOARD_SIZE, size);
		page.drawLine(0, size * 2, BOARD_SIZE, size * 2);
		page.drawLine(0, size * 3, BOARD_SIZE, size * 3);
		page.drawLine(0, BOARD_SIZE, BOARD_SIZE, BOARD_SIZE);
		
		int fontSize = BOARD_SIZE / 10;
		page.setFont(new Font("Helvetica", Font.BOLD, fontSize));
		page.drawString("Points: " + points, 10, BOARD_SIZE + size / 2);
		
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int num = digits[row][col];
				if (num != 0) {
					int shift = 0;
					if (num < 10)
						shift = 10;
					else if (num < 100)
						shift = 20;
					else if (num < 1000)
						shift = 30;
					else
						shift = 42;
					fontSize = BOARD_SIZE / 10 - num / 100;
					page.setFont(new Font("Helvetica", Font.BOLD, fontSize));
					page.drawString("" + num, size / 2 + col * size - shift, size / 2 + row * size + 15);
				}
			}
		}
	}
	
	public void onTurn(int move) {
		
		if (move == 1) {//up
			for (int col = 0; col < 4; col++) {
				for (int row = 1; row < 4; row++) {
					if (digits[row][col] != 0) {
						int currentRow = row;
						while (currentRow > 0 && digits[currentRow - 1][col] == 0) {
							digits[currentRow - 1][col] = digits[currentRow][col];
							digits[currentRow][col] = 0;
							currentRow--;
							validMove = true;
						}
						if (currentRow != 0 && digits[currentRow - 1][col] == digits[currentRow][col]) {
							digits[currentRow - 1][col] *= 2;
							points += digits[currentRow - 1][col];
							digits[currentRow][col] = 0;
							validMove = true;
						}
					}
				}
			}
		}	
		else if (move == 2) {//down
			for (int col = 0; col < 4; col++) {
				for (int row = 2; row >= 0; row--) {
					if (digits[row][col] != 0) {
						int currentRow = row;
						while (currentRow < 3 && digits[currentRow + 1][col] == 0) {
							digits[currentRow + 1][col] = digits[currentRow][col];
							digits[currentRow][col] = 0;
							currentRow++;
							validMove = true;
						}
						if (currentRow != 3 && digits[currentRow + 1][col] == digits[currentRow][col]) {
							digits[currentRow + 1][col] *= 2;
							points += digits[currentRow + 1][col];
							digits[currentRow][col] = 0;
							validMove = true;
						}
					}
				}
			}
		}
		else if (move == 3) {//left
			for (int row = 0; row < 4; row++) {
				for (int col = 1; col < 4; col++) {
					if (digits[row][col] != 0) {
						int currentCol = col;
						while (currentCol > 0 && digits[row][currentCol - 1] == 0) {
							digits[row][currentCol - 1] = digits[row][currentCol];
							digits[row][currentCol] = 0;
							currentCol--;
							validMove = true;
						}
						if (currentCol != 0 && digits[row][currentCol - 1] == digits[row][currentCol]) {
							digits[row][currentCol - 1] *= 2;
							points += digits[row][currentCol - 1];
							digits[row][currentCol] = 0;
							validMove = true;
						}
					}
				}
			}
		}
		else if (move == 4) {//right
			for (int row = 0; row < 4; row++) {
				for (int col = 2; col >= 0; col--) {
					if (digits[row][col] != 0) {
						int currentCol = col;
						while (currentCol < 3 && digits[row][currentCol + 1] == 0) {
							digits[row][currentCol + 1] = digits[row][currentCol];
							digits[row][currentCol] = 0;
							currentCol++;
							validMove = true;
						}
						if (currentCol != 3 && digits[row][currentCol + 1] == digits[row][currentCol]) {
							digits[row][currentCol + 1] *= 2;
							points += digits[row][currentCol + 1];
							digits[row][currentCol] = 0;
							validMove = true;
						}
					}
				}
			}
		}
		
		if (validMove) generateDigit();
		validMove = false;
		repaint();
		
		int totalDigits = 0;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				if (digits[row][col] != 0) {
					totalDigits++;
				}
			}
		}
		
		if (totalDigits == 16) {
			gameOver = true;
			if (digits[0][0] != 0 && digits[0][0] == digits[0][1]) gameOver = false;
			else if (digits[0][0] != 0 && digits[0][0] == digits[1][0]) gameOver = false;
			else if (digits[0][2] != 0 && digits[0][2] == digits[0][3]) gameOver = false;
			else if (digits[0][2] != 0 && digits[0][2] == digits[1][2]) gameOver = false;
			else if (digits[0][2] != 0 && digits[0][2] == digits[0][1]) gameOver = false;
			else if (digits[1][1] != 0 && digits[1][1] == digits[0][1]) gameOver = false;
			else if (digits[1][1] != 0 && digits[1][1] == digits[1][2]) gameOver = false;
			else if (digits[1][1] != 0 && digits[1][1] == digits[2][1]) gameOver = false;
			else if (digits[1][1] != 0 && digits[1][1] == digits[1][0]) gameOver = false;
			else if (digits[1][3] != 0 && digits[1][3] == digits[0][3]) gameOver = false;
			else if (digits[1][3] != 0 && digits[1][3] == digits[2][3]) gameOver = false;
			else if (digits[1][3] != 0 && digits[1][3] == digits[1][2]) gameOver = false;
			else if (digits[2][0] != 0 && digits[2][0] == digits[1][0]) gameOver = false;
			else if (digits[2][0] != 0 && digits[2][0] == digits[2][1]) gameOver = false;
			else if (digits[2][0] != 0 && digits[2][0] == digits[3][0]) gameOver = false;
			else if (digits[2][2] != 0 && digits[2][2] == digits[1][2]) gameOver = false;
			else if (digits[2][2] != 0 && digits[2][2] == digits[2][3]) gameOver = false;
			else if (digits[2][2] != 0 && digits[2][2] == digits[3][2]) gameOver = false;
			else if (digits[2][2] != 0 && digits[2][2] == digits[2][1]) gameOver = false;
			else if (digits[3][1] != 0 && digits[3][1] == digits[2][1]) gameOver = false;
			else if (digits[3][1] != 0 && digits[3][1] == digits[3][2]) gameOver = false;
			else if (digits[3][1] != 0 && digits[3][1] == digits[3][0]) gameOver = false;
			else if (digits[3][3] != 0 && digits[3][3] == digits[2][3]) gameOver = false;
			else if (digits[3][3] != 0 && digits[3][3] == digits[3][2]) gameOver = false;
		}		
		
		if (gameOver) {
			JOptionPane.showMessageDialog(null, "Game over" );
		}
	}
	
	public void generateDigit() {
		boolean cont = true;
		while (cont) {
			int randRow = (int) (Math.random() * 4);
			int randCol = (int) (Math.random() * 4);
			if (digits[randRow][randCol] == 0) {
				digits[randRow][randCol] = 2;
				cont = false;
			}
		}
	}

	public void keyPressed(KeyEvent event) {
		boolean callOnTurn = false;
		if (!gameOver) {
			if (event.getKeyCode() == KeyEvent.VK_UP) {
				for (int row = 1; row < 4; row++) {
					for (int col = 0; col < 4; col++) {
						if (digits[row][col] == digits[row - 1][col] || digits[row - 1][col] == 0)
							callOnTurn = true;
					}
				}
				if (callOnTurn) onTurn(1);
			}
			else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
				for (int row = 0; row < 3; row++) {
					for (int col = 0; col < 4; col++) {
						if (digits[row][col] == digits[row + 1][col] || digits[row + 1][col] == 0)
							callOnTurn = true;
					}
				}
				if (callOnTurn) onTurn(2);
			}
			else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
				for (int row = 0; row < 4; row++) {
					for (int col = 1; col < 4; col++) {
						if (digits[row][col] == digits[row][col - 1] || digits[row][col - 1] == 0)
							callOnTurn = true;
					}
				}
				if (callOnTurn) onTurn(3);
			}
			else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
				for (int row = 0; row < 4; row++) {
					for (int col = 0; col < 3; col++) {
						if (digits[row][col] == digits[row][col + 1] || digits[row][col + 1] == 0)
							callOnTurn = true;
					}
				}
				if (callOnTurn) onTurn(4);
			}
		}
	}

	public void keyReleased(KeyEvent event) {}
	public void keyTyped(KeyEvent event) {}

}

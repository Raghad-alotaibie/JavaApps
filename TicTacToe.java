import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {

    private final JButton[] buttons = new JButton[9];
    private char currentPlayer = 'X';
    private boolean gameEnded = false;
    private final JLabel statusLabel;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        // Bottom status bar
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(230, 230, 230));
        statusLabel.setForeground(new Color(10, 40, 100));
        add(statusLabel, BorderLayout.SOUTH);

        // 3×3 button grid
        JPanel panel = new JPanel(new GridLayout(3, 3, 8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font btnFont = new Font("Segoe UI", Font.BOLD, 60);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(btnFont);
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(new Color(173, 216, 230)); // Light Blue
            buttons[i].setForeground(Color.BLUE.darker());
            buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameEnded) return;

        JButton clicked = (JButton) e.getSource();
        if (!clicked.getText().equals("")) {
            return; // Ignore if already clicked
        }

        clicked.setText(String.valueOf(currentPlayer));

        // Set text color based on current player
        if (currentPlayer == 'X') {
            clicked.setForeground(new Color(10, 40, 100));
        } else {
            clicked.setForeground(new Color(178, 34, 34)); // Firebrick red
        }

        if (checkWin()) {
            statusLabel.setText("Player " + currentPlayer + " wins! 🎉");
            gameEnded = true;
            int option = JOptionPane.showConfirmDialog(this, "Player " + currentPlayer + " won! Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        } else if (checkDraw()) {
            statusLabel.setText("It's a draw!");
            gameEnded = true;
            int option = JOptionPane.showConfirmDialog(this, "It's a draw! Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        } else {
            togglePlayer();
            statusLabel.setText("Player " + currentPlayer + "'s turn");
        }
    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkWin() {
        String p = String.valueOf(currentPlayer);
        // Rows
        if (checkLine(0, 1, 2, p)) return true;
        if (checkLine(3, 4, 5, p)) return true;
        if (checkLine(6, 7, 8, p)) return true;
        // Columns
        if (checkLine(0, 3, 6, p)) return true;
        if (checkLine(1, 4, 7, p)) return true;
        if (checkLine(2, 5, 8, p)) return true;
        // Diagonals
        if (checkLine(0, 4, 8, p)) return true;
        return checkLine(2, 4, 6, p);
    }

    private boolean checkLine(int a, int b, int c, String player) {
        return buttons[a].getText().equals(player) &&
               buttons[b].getText().equals(player) &&
               buttons[c].getText().equals(player);
    }

    private boolean checkDraw() {
        for (JButton btn : buttons) {
            if (btn.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        for (JButton btn : buttons) {
            btn.setText("");
            btn.setForeground(Color.BLUE.darker());
        }
        currentPlayer = 'X';
        gameEnded = false;
        statusLabel.setText("Player X's turn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe game = new TicTacToe();
            game.setVisible(true);
        });
    }
}
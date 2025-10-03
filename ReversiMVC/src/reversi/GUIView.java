package reversi;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;


public class GUIView implements IView
{
	IModel model;
	IController controller;
	BoardSquares[][] whiteBoardSquares;
	BoardSquares[][] blackBoardSquares;
	public GUIView()
	{}
	
	JFrame blackFrame = new JFrame();
	JFrame whiteFrame = new JFrame();
	JLabel fdLblWhite = new JLabel();
	JLabel fdLblBlack = new JLabel();
	JPanel whiteBoard = new JPanel();
	JPanel blackBoard = new JPanel();
	JPanel blackBoardButtonPanel = new JPanel();
	JPanel whiteBoardButtonPanel = new JPanel();
	JButton greedyButtonWhite = new JButton("Greedy AI (play white)");
	JButton restartButtonWhite = new JButton("Restart");
	JButton greedyButtonBlack = new JButton("Greedy AI (play black)");
	JButton restartButtonBlack = new JButton("Restart");

	
	
	
	@Override
	public void initialise(IModel model, IController controller)
	{
		this.model = model;
		this.controller = controller;
		whiteBoardSquares = new BoardSquares[8][8];
		blackBoardSquares = new BoardSquares[8][8];
		
		blackFrame.setLayout(new BorderLayout());
		whiteFrame.setLayout(new BorderLayout());
		whiteBoard.setLayout(new GridLayout(8,8));
		blackBoard.setLayout(new GridLayout(8,8));
		blackBoardButtonPanel.setLayout(new GridLayout(2,1));
		whiteBoardButtonPanel.setLayout(new GridLayout(2,1));
		blackFrame.add(fdLblBlack,BorderLayout.NORTH);
		whiteFrame.add(fdLblWhite,BorderLayout.NORTH);
		blackFrame.add(blackBoard, BorderLayout.CENTER);
		whiteFrame.add(whiteBoard, BorderLayout.CENTER);
		blackBoardButtonPanel.add(greedyButtonBlack);
		blackBoardButtonPanel.add(restartButtonBlack);
		whiteBoardButtonPanel.add(greedyButtonWhite);
		whiteBoardButtonPanel.add(restartButtonWhite);
		blackFrame.add(blackBoardButtonPanel,BorderLayout.SOUTH);
		whiteFrame.add(whiteBoardButtonPanel,BorderLayout.SOUTH);
		whiteFrame.setTitle("Reversi - White Player");
		blackFrame.setTitle("Reversi - Black Player");

		restartButtonBlack.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.startup();
			}
		});
		restartButtonWhite.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.startup();
			}
		});
		greedyButtonBlack.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.doAutomatedMove(2);
			}
		});
		greedyButtonWhite.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.doAutomatedMove(1);
				
			}
		});
		for (int y  = 0 ; y <8 ; y++)
		{
			for (int x = 0 ; x < 8;x++)
			{
				BoardSquares button = new BoardSquares(x, y,1, model, controller);
				whiteBoard.add(button);
				whiteBoardSquares[x][y] = button;
			}
		}
		
		for (int y  = 7 ; y >= 0 ; y--)
		{
			for (int x = 7 ; x >= 0 ;x--)
			{
				BoardSquares button = new BoardSquares(x, y,2, model, controller);
				blackBoard.add(button);
				blackBoardSquares[x][y] = button;
			}
		}
		
		blackFrame.pack();
		whiteFrame.pack();
		blackFrame.setVisible(true);
		whiteFrame.setVisible(true); 
	}

	@Override
	public void refreshView()
	{
		for(int y = 0 ; y < 8 ; y++)
		{
			for(int x = 0 ; x < 8;x++)
			{
				blackBoardSquares[x][y].repaint();
				whiteBoardSquares[x][y].repaint();
			}
		}
	}

	@Override
	public void feedbackToUser(int player, String message)
	{
		if (player == 1)
		{
			fdLblWhite.setText(message);
		}
		else if(player == 2)
		{
			fdLblBlack.setText(message);
		}
	}

}

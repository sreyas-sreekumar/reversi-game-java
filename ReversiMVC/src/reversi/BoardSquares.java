package reversi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BoardSquares extends JButton
{
	IModel model;
	IController controller;
	int x,y;
	int player;
	
	public BoardSquares(int x,int y, int player,IModel model,IController controller)
	{
		this.model = model;
		this.controller = controller;
		this.x = x;
		this.y = y;
		this.player = player;
		addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//int playerNum = model.getPlayer();
				controller.squareSelected(player, x, y);
				System.out.println("X :"+ x + "Y : " + y);
			}
		});
		
	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		int pieceValue = model.getBoardContents(x, y);
		
		if (pieceValue == 1)
		{
			g.setColor(Color.WHITE);
			g.fillOval(10, 10, getWidth()-20, getHeight()-20);
			g.setColor(Color.BLACK);
			g.drawOval(10, 10, getWidth()-20, getHeight()-20);

		}
		if (pieceValue == 2)
		{
			g.setColor(Color.BLACK);
			g.fillOval(10, 10, getWidth()-20, getHeight()-20);
			g.setColor(Color.WHITE);
			g.drawOval(10, 10, getWidth()-20, getHeight()-20);

		}
	}
	
}

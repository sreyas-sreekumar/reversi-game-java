package reversi;

import java.awt.Point;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

public class ReversiController implements IController
{
	IModel model;
	IView view;
	
	@Override
	public void initialise(IModel model, IView view)
	{
		this.model = model;
		this.view = view;
		
	}

	@Override
	public void startup()
	{
		model.clear(0);
		model.setPlayer(1);
		model.setBoardContents(3, 3, 1);
		model.setBoardContents(4, 4, 1);
		model.setBoardContents(3, 4, 2);
		model.setBoardContents(4, 3, 2);
		view.feedbackToUser(1, "White player – choose where to put your piece");
		view.feedbackToUser(2, "Black player - not your turn");
		view.refreshView();
		
	}

	@Override
	public void update() {
	    int current   = model.getPlayer();
	    int otherPlayer = 3 - current;
	
	    boolean curCan   = canPlayerMakeMove(current);
	    boolean otherCan = canPlayerMakeMove(otherPlayer);
	
	    if (!curCan && otherCan) {
	        model.setPlayer(otherPlayer);
	        
	        model.setFinished(false);
	        view.feedbackToUser(otherPlayer,(otherPlayer==1 ? "White" : "Black") + " player – choose where to put your piece");
	        view.feedbackToUser(current,( current==1   ? "White" : "Black") + " player – not your turn");
	        view.refreshView();
	        return;
	    }
	
	    if (!curCan && !otherCan) 
	    {
	        model.setFinished(true);
	        int whiteCount = 0;
	        int blackCount = 0;
	        for (int x = 0; x < 8; x++)
	        {
	          for (int y = 0; y < 8; y++) 
	          {
	            int value = model.getBoardContents(x,y);
	            
	            if(value==1) 
	            {
	            	whiteCount++;
	            }
	            else if (value==2) 
	            {
	            	blackCount++;
	            }
	          }
	        }
	        if(whiteCount > blackCount) 
	        {
	          String msg = String.format("White won. White %d to Black %d. Reset game to replay.",whiteCount, blackCount);
	          view.feedbackToUser(1, msg);
	          view.feedbackToUser(2, msg);
	        }
	        else if (blackCount > whiteCount) 
	        {
	          String msg = String.format("Black won. Black %d to White %d. Reset game to replay.",blackCount, whiteCount);
	          view.feedbackToUser(1, msg);
	          view.feedbackToUser(2, msg);
	        }
	        else 
	        {
	          String msg = String.format("Draw. Both players ended with %d pieces. Reset game to replay.",whiteCount);
	          view.feedbackToUser(1, msg);
	          view.feedbackToUser(2, msg);
	        }
	        view.refreshView();
	        return;
	    }
	
	    model.setFinished(false);
	    view.feedbackToUser(current,(current   ==1 ? "White" : "Black") + " player – choose where to put your piece");
	    view.feedbackToUser(otherPlayer, (otherPlayer==1 ? "White" : "Black") + " player – not your turn");
	    view.refreshView();
	}

	public List<Point> takeFlipInDirection(int player,int x,int y,int xDir,int yDir)
	{
		List<Point> tempListOpponents = new ArrayList<Point>();
		int tempX = x + xDir;
		int tempY = y + yDir;
		while (tempX >= 0 && tempX < 8 && tempY >= 0 && tempY < 8)
		{
			int currentValue = model.getBoardContents(tempX,tempY);
			if (currentValue == 0)
			{
				return new ArrayList<Point>();

			}
			else if(currentValue == player)
			{
				return tempListOpponents;
			}
			else 
			{
				Point newOpponentPoint =new Point (tempX,tempY);
				tempListOpponents.add(newOpponentPoint);
			}
			
			 tempX += xDir;
			 tempY += yDir;
			
		}
		
		return new ArrayList<Point>();
	}
	
	public boolean canPlayerMakeMove(int player)
	{
		int[] xDelta = {+1,-1,0,0,+1,-1,+1,-1};
		int[] yDelta = {0,0,+1,-1,+1,-1,-1,+1};
		for(int y = 0; y< 8;y++)
		{
			for(int x = 0 ;x <8 ;x++)
			{
				if (model.getBoardContents(x, y) == 0)
				{
					for(int i = 0;i<8;i++)
					{
						List<Point>tempList = takeFlipInDirection(player, x, y, xDelta[i],yDelta[i]);
						if(tempList.isEmpty()==false)
						{
							return true;
						}

					}
					
				}
			}
		}
		return false;
	}

	@Override
	public void squareSelected(int player, int x, int y)
	{
		int playerNext;
		int[] xDelta = {+1,-1,0,0,+1,-1,+1,-1};
		int[] yDelta = {0,0,+1,-1,+1,-1,-1,+1};
		int blackScore =0;
		int whiteScore=0;
		List<Point> masterListFlippable = new ArrayList<Point>();
		
		if (model.getPlayer() != player)
		{
			view.feedbackToUser(player, "It is not your turn!");
			return;
		}
		if(model.getBoardContents(x, y) != 0)
		{
			view.feedbackToUser(player, "Invalid location to play a piece.");
			return;
		}
		
		for(int i = 0;i<8;i++)
		{
			List<Point>flipList = takeFlipInDirection(player, x, y, xDelta[i],yDelta[i]);
			masterListFlippable.addAll(flipList);
		}
		if(masterListFlippable.isEmpty())
		{
			view.feedbackToUser(player, "Invalid location to play a piece.");
			return;
		}
		model.setBoardContents(x, y, player);
		
		for (Point point : masterListFlippable)
		{
			model.setBoardContents(point.x, point.y, player);
		}
		
		if(player == 1)
		{
			 playerNext = 2;
		}
		else
		{
			playerNext = 1;
		}
		model.setPlayer(playerNext);
		update();
	}

	@Override
	public void doAutomatedMove(int player)
	{
		int aiX = 0;
		int aiY = 0 ;
		int currentMaxFlippable = 0;
		int[] xDelta = {+1,-1,0,0,+1,-1,+1,-1};
		int[] yDelta = {0,0,+1,-1,+1,-1,-1,+1};

		for(int y = 0;y<8;y++)
		{
			for(int x = 0;x<8;x++)
			{
				if(model.getBoardContents(x, y) == 0)
				{
					List<Point> totalFlips = new ArrayList<>();
					for(int i = 0;i<8;i++)
					{
						List<Point>tempPointList = takeFlipInDirection(player, x, y, xDelta[i],yDelta[i]);
						totalFlips.addAll(tempPointList);
					}
					if( totalFlips.size() > currentMaxFlippable)
					{
						aiX = x;
						aiY = y;
						currentMaxFlippable = totalFlips.size();
						
					}
				}
			}
		}
		
		if(currentMaxFlippable > 0)
		{
			squareSelected(player, aiX, aiY);
			update();
		}
		else
		{
			model.setPlayer(3-player);
			update();
		}
	}

}

package controller;
import java.util.ArrayList;

public class BidController {
	
	private ArrayList<Bid> bids;
	
	public BidController() {
		bids = new ArrayList<>();
	}
	
	public boolean newBid(String player, int bidValue) { 
         if (bidValue <= 0) return false;
		 Bid bid =  createBid(player, bidValue);
		 boolean validBid = checkBidValidity(bid);
		 if(validBid) {
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	
	//createBid creates a bid object and returns it
	private Bid createBid(String player, int bidValue) {
		Bid newBid = new Bid(player, bidValue);
		return newBid;
	}
	
	//checkValidity checks if the bid passed to it is a valid bid. If it is a valid bit then it deletes 
	//the old bid made by the the player and adds the new bid and returns true. Otherwise it does nothing and 
	//returns false.
	private Boolean checkBidValidity(Bid bid) {
		String player = bid.getPlayer();
		int bidValue = bid.getValue();
		int index = 0;
		while(index < bids.size()) {
			Bid searchBid = bids.get(index);
			String searchPlayer = searchBid.getPlayer();
			int searchValue = searchBid.getValue();
			if(searchPlayer.equals(player)) {
				if(searchValue > bidValue) {
					bids.remove(index);
					bids.add(bid);
					return true;
				}
				else {
					return false;
				}
			}
			else {
				index++;
			}
		}
		bids.add(bid);
		return true;
	}
	
	//print list is just for testing 
	public void printList() {
		for(Bid bid : bids) {
			bid.printBid();
		}
	}
	
	//Comparing moves with bid
	public boolean compareMoves(String player2, int moves) {
		int index = 0;
		while(index < bids.size()) {
			Bid searchBid = bids.get(index);
			String searchPlayer = searchBid.getPlayer();
			int searchValue = searchBid.getValue();
			if(searchPlayer.equals(player2)) {
				if(moves >= searchValue) {
					return true;
				}else {
					return false;
				}
			}
			else {
				index++;
			}
		}
		return false;
	}
}

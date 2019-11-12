package com.sovereignty.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sovereignty.model.Card;
import com.sovereignty.model.Page;

public class CardDAO {
	java.sql.Connection conn;
	PageDAO pageDAO = new PageDAO();
	public CardDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
	}
	
	// Return null if card does not exist
	public Card getCardByID(String cardID) throws Exception{
		try {
			Card card = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Cards WHERE cardID = ?;");
			ps.setString(1,  cardID);

            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                card = generateDeepCard(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return card;
		} catch (Exception e) {
			throw new Exception("Could not get card with ID"+cardID);
		}
	}
	
	// Return null if card does not exist
	public Card getCardByRecipientAndEventType(String recipient, String eventType) throws Exception{
		try {
			Card card = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Cards WHERE recipient = ? AND eventType = ?;");
			ps.setString(1,  recipient);
			ps.setString(2,  eventType);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                card = generateDeepCard(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return card;
		} catch (Exception e) {
			throw new Exception("Could not get card");
		}
	}
	
	public boolean addCard(Card card) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Cards "
					+ "(cardID, recipient, eventType, orientation, frontPage, leftPage, rightPage, backPage) "
					+ "values(?,?,?,?,?,?,?,?);");
            ps.setString(1, card.getCardID());
            ps.setString(2, card.getRecipient());
            ps.setString(3, card.getEventType());
            ps.setString(4, card.getOrientation());
            ps.setString(5, card.getFrontPage().getPageID());
            ps.setString(6, card.getLeftPage().getPageID());
            ps.setString(7, card.getRightPage().getPageID());
            ps.setString(8, card.getBackPage().getPageID());
            ps.execute();
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert card: " + e.getMessage());
        }
		
	}
	
    private Card generateCard(ResultSet res) throws Exception {
    	String cardID = res.getString("cardID");
        String recipient  = res.getString("recipient");
        String eventType = res.getString("eventType");
        String orientation = res.getString("orientation");
        Page frontPage = pageDAO.getPageByID(res.getString("frontPage"));
        Page leftPage = pageDAO.getPageByID(res.getString("leftPage"));
        Page rightPage = pageDAO.getPageByID(res.getString("rightPage"));
        Page backPage = pageDAO.getPageByID(res.getString("backPage"));

        return new Card (cardID, recipient, eventType, orientation, frontPage, leftPage, rightPage, backPage);
    }
    
    private Card generateDeepCard(ResultSet res) throws Exception {
    	String cardID = res.getString("cardID");
        String recipient  = res.getString("recipient");
        String eventType = res.getString("eventType");
        String orientation = res.getString("orientation");
        
        String frontPageID = res.getString("frontPage");
        String leftPageID = res.getString("leftPage");
        String rightPageID = res.getString("rightPage");
        String backPageID = res.getString("backPage");
        
        Card genCard = new Card (cardID, recipient, eventType, orientation);
        genCard.setFrontPage(new Page(frontPageID));
        genCard.setLeftPage(new Page(leftPageID));
        genCard.setRightPage(new Page(rightPageID));
        genCard.setBackPage(new Page(backPageID, 0));
        
        return genCard;
    }
    
    public List<Card> getAllCards() throws Exception{
    	List<Card> allCards = new ArrayList<>();
    	try {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM Cards";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	Card c = generateCard(resultSet);
            	allCards.add(c);
            }
            resultSet.close();
            statement.close();
            return allCards;
    	}catch (Exception e) {
    		throw new Exception("Failed getting all cards: "+ e.getMessage());
    	}    	
    }
    
    public boolean deleteCard(String cardID) throws Exception{
    	try {
    		PreparedStatement ps = conn.prepareStatement("DELETE FROM Cards WHERE cardID = ?;");
    		ps.setString(1, cardID);
    		int numAffected = ps.executeUpdate();
    		ps.close();
    		return (numAffected == 1);
    	}catch (Exception e) {
			throw new Exception("Failed to delete Card: "+e.getMessage());
		}
    }

}

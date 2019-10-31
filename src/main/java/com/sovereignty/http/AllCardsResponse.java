package com.sovereignty.http;

import java.util.List;
import com.sovereignty.model.Card;

public class AllCardsResponse {
	public List<Card> cards;
	public int code;
	String error;
	
	public AllCardsResponse(int code, String errorMessage) {
		this.code = code;
		this.error = errorMessage;
	}
	
	public AllCardsResponse(int code, String errorMessage, List<Card> cards) {
		this.code = code;
		this.error = errorMessage;
		this.cards = cards;
	}
}
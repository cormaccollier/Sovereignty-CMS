package com.sovereignty.http;

import java.util.List;
import com.sovereignty.model.Card;

public class AllCardsResponse {
	List<Card> cards;
	int code;
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

	public List<Card> getCards() {
		return cards;
	}

	public int getCode() {
		return code;
	}

	public String getError() {
		return error;
	}
}

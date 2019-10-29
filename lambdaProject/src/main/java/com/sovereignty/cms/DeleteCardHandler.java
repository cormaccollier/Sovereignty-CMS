package com.sovereignty.cms;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sovereignty.cms.db.CardDAO;
import com.sovereignty.cms.http.DeleteCardRequest;
import com.sovereignty.cms.http.DeleteCardResponse;

public class DeleteCardHandler implements RequestHandler<DeleteCardRequest, DeleteCardResponse> {
	CardDAO cardDao = new CardDAO();
	
	public String validateDeleteCardRequest(DeleteCardRequest input) {
		if (input.getCardID() == null || input.getCardID().isEmpty()) {
			return "cardID required";
		}
		return null;
	}
	
	
    @Override
    public DeleteCardResponse handleRequest(DeleteCardRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        
        try {
        	String validationError = this.validateDeleteCardRequest(input);
        	if (validationError != null) {
        		return new DeleteCardResponse(400, validationError);
        	}
        	
        	boolean cardDeleted = cardDao.deleteCard(input.getCardID());
        	if (! cardDeleted) {
        		return new DeleteCardResponse(500, "failed deleting Card "+input.getCardID());
        	};
        	
        	return new DeleteCardResponse(200, "successfully deleted Card "+input.getCardID());
        }catch (Exception e) {
			return new DeleteCardResponse(500, e.getMessage());
		}
		
    }

}
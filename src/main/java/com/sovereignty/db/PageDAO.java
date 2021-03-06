package com.sovereignty.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import com.sovereignty.model.Page;
import com.sovereignty.model.VisualElement;

public class PageDAO {
	java.sql.Connection conn;
	
	public PageDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
	}
	
	// Return null if page does not exist
	public Page getPageByID(String pageID) throws Exception{
		try {
			Page page = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Pages WHERE pageID = ?;");
			ps.setString(1,  pageID);

            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                page = generatePage(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return page;
		} catch (Exception e) {
			throw new Exception("Could not get page");
		}
	}
	
	public String createEmptyPage() throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Pages "
					+ "(pageID, isModifiable) "
					+ "values(?,?);");
			
	    	String pageID = UUID.randomUUID().toString().substring(0, 20); // no more than 20 because of DB restrictions...
			int isModifiable = 1;
	    	
            ps.setString(1, pageID);
            ps.setInt(2, isModifiable);
            ps.execute();
            
            return pageID;
        } catch (Exception e) {
            throw new Exception("Failed to insert page: " + e.getMessage());
        }
		
	}
	
	public boolean addPage(Page page) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Pages "
					+ "(pageID, isModifiable) "
					+ "values(?,?);");
            ps.setString(1, page.getPageID());
            ps.setInt(2, page.getIsModifiable());
            ps.execute();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to insert page: " + e.getMessage());
        }
		
	}
	
	public boolean savePage(Page page) throws Exception {
		try {
			boolean addedPage = addPage(page);
			
			for(VisualElement ve : page.getListVisualElements()) {
				ve.setUpdated(VisualElementDAO.CHANGED);
			}
			
			boolean updatedPage =  updatePage(page);
			
            return addedPage && updatedPage;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert page: " + e.getMessage());
        }
		
	}
	
    private Page generatePage(ResultSet res) throws Exception {
    	String pageID = res.getString("pageID");
        int isModifiable  = res.getInt("isModifiable");
        List<VisualElement> listVE = new VisualElementDAO().getVisualElementByPageID(pageID);
        
        return new Page (pageID, isModifiable, listVE);
    }
    
    public boolean updatePage(Page page) throws Exception{
    	try {
    		VisualElementDAO vd = new VisualElementDAO();
    		
    		for(VisualElement ve : page.getListVisualElements()) {
    			vd.update(ve);
    		}
    		
    		return (true);
    	}catch (Exception e) {
			throw new Exception("Failed to update Page: "+e.getMessage());
		}
    }
    
    public boolean deletePage(String pageID) throws Exception{
    	try {
    		Page p = getPageByID(pageID);

    		// Delete all the elements associated with the Page
    		VisualElementDAO vd = new VisualElementDAO();
    		for(VisualElement ve : p.getListVisualElements()) {
    			ve.setUpdated(VisualElementDAO.DELETE);
    			vd.update(ve);
    		}
    		
    		// Delete the Actual Page second
    		// Makes sure Foreign Key constraint in Elements table is not lost
    		// after deleting the Page
    		PreparedStatement ps = conn.prepareStatement("DELETE FROM Pages WHERE pageID = ?;");
    		ps.setString(1, pageID);
    		int numAffected = ps.executeUpdate();
    		ps.close();
    		
    		return (numAffected == 1);
    		
    	}catch (Exception e) {
			throw new Exception("Failed to delete Page: "+e.getMessage());
		}
    }
		
}


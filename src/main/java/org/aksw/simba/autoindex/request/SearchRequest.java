package org.aksw.simba.autoindex.request;

public class SearchRequest{
	String query;
	
	public enum Type {
		LABEL ,
		URI  ,
		NONE
	};
	
	public enum Category{
		CLASS,
		ENTITY,
		PROPERTY,
		ALL,
		NONE
	};
	
	Type type;
	Category category;
	
	public SearchRequest(){
		this.query = "";
		this.type = Type.NONE;
		this.category = Category.NONE;
	}
	
	public SearchRequest(String query){
		this.query = query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void setType(String type) {
		if(type.equals("uri") || type.equals("URI"))
			this.type = Type.URI;
		else if (type.equals("label") || type.equals("LABEL"))
			this.type = Type.LABEL;
		else
			this.type = Type.NONE;
	}
	
	public void setCategory(String category) {
		if(category.equals("CLASS") || category.equals("class"))
			this.category = Category.CLASS;
		else if (category.equals("PROPERTY") || category.equals("property"))
			this.category = Category.PROPERTY;
		else if (category.equals("ENTITY") || category.equals("entity"))
			this.category = Category.ENTITY;
		else
			this.category = Category.ALL;
	}
	
	public Category getCategory(){
		return this.category;
	}
	
	public Type getType() {
		return this.type;
	}
	
}
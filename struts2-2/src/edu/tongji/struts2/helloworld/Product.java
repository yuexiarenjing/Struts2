package edu.tongji.struts2.helloworld;

public class Product {

	private Integer prodectId;
	private String productName;
	private String productDesc;

	private double productPrice;

	public Integer getProdectId() {
		return prodectId;
	}

	public void setProdectId(Integer prodectId) {
		this.prodectId = prodectId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "Product [prodectId=" + prodectId + ", productName=" + productName + ", productDesc=" + productDesc
				+ ", productPrice=" + productPrice + "]";
	}
	
	public String save(){
		System.out.println("save " + this);
		return "details";
	}

}

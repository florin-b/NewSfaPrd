package my.logon.screen.beans;

import java.util.List;

public class ComandaMathaus {

	private String sellingPlant;
	private List<DateArticolMathaus> deliveryEntryDataList;

	public String getSellingPlant() {
		return sellingPlant;
	}

	public void setSellingPlant(String sellingPlant) {
		this.sellingPlant = sellingPlant;
	}

	public List<DateArticolMathaus> getDeliveryEntryDataList() {
		return deliveryEntryDataList;
	}

	public void setDeliveryEntryDataList(List<DateArticolMathaus> deliveryEntryDataList) {
		this.deliveryEntryDataList = deliveryEntryDataList;
	}

}

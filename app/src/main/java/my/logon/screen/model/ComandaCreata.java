package my.logon.screen.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.beans.BeanComandaCreata;

public class ComandaCreata {

	List<BeanComandaCreata> listComenzi = new ArrayList<BeanComandaCreata>();

	private static ComandaCreata instance = new ComandaCreata();
	

	private ComandaCreata() {

	}

	public static ComandaCreata getInstance() {
		return instance;
	}

	public double getTotalComenzi(List<BeanComandaCreata> listComenzi) {
		
		Iterator<BeanComandaCreata> iterator = listComenzi.iterator();
		double totalComenzi = 0;
		
		BeanComandaCreata comanda;
		while(iterator.hasNext())
		{
			comanda = iterator.next();
			totalComenzi += Double.valueOf(comanda.getSuma());
			
		}
		
		return totalComenzi;
	}

	public double getTotalComenziTva(List<BeanComandaCreata> listComenzi) {
		
		Iterator<BeanComandaCreata> iterator = listComenzi.iterator();
		double totalComenziTva = 0;
		
		BeanComandaCreata comanda;
		while(iterator.hasNext())
		{
			comanda = iterator.next();
			totalComenziTva += Double.valueOf(comanda.getSumaTva());
			
		}
		
		return totalComenziTva;
	}

	public void clearListComenzi() {
		listComenzi.clear();
	}

}

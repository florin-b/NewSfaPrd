package my.logon.screen.patterns;

import java.util.Comparator;

import my.logon.screen.beans.BeanComandaCreata;

public class ComandaAprobareComparator implements Comparator<BeanComandaCreata> {

	public int compare(BeanComandaCreata cmd1, BeanComandaCreata cmd2) {

		Boolean clRaft1 = !cmd1.isClientRaft();
		Boolean clRaft2 = !cmd2.isClientRaft();

		int comp1 = clRaft1.compareTo(clRaft2);

		if (comp1 != 0) {
			return comp1;
		} else {
			String idCmd1 = cmd1.getId();
			String idCmd2 = cmd2.getId();

			return idCmd1.compareTo(idCmd2);
		}

	}

}

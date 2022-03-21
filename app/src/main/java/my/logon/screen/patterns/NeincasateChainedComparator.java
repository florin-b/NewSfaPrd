package my.logon.screen.patterns;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class NeincasateChainedComparator<FacturaNeincasataLite> implements Comparator<FacturaNeincasataLite> {

	private List<Comparator<FacturaNeincasataLite>> listComparators;

	
	public NeincasateChainedComparator(Comparator<FacturaNeincasataLite>... comparators) {
		this.listComparators = Arrays.asList(comparators);
	}

	@Override
	public int compare(FacturaNeincasataLite fact1, FacturaNeincasataLite fact2) {
		for (Comparator<FacturaNeincasataLite> comparator : listComparators) {
			int result = comparator.compare(fact1, fact2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

}

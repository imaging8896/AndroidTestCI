package hcfs.test.utils.db.info;

import java.util.Comparator;

public class UidInfoAdapterComparator implements Comparator<UidInfoAdapter> {
    @Override
    public int compare(UidInfoAdapter uidInfoAdapter, UidInfoAdapter t1) {
        return uidInfoAdapter.getId() - t1.getId();
    }
}

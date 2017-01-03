package hcfs.test.utils.db.info;

import com.hopebaytech.hcfsmgmt.info.UidInfo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Use this instead of UidInfo directly to prevent modification from RD.
 */

public class UidInfoAdapter extends UidInfo {

    public UidInfoAdapter() {
        super();
        setExternalDir(new ArrayList<String>());
    }

    @Override
    public boolean equals(Object object) {
        UidInfoAdapter compareOne = (UidInfoAdapter) object;
        if(externalDir.size() != compareOne.externalDir.size())
            return false;
        Collections.sort(new ArrayList<>(externalDir));
        Collections.sort(new ArrayList<>(compareOne.externalDir));

        return id == compareOne.id &&
                isPinned == compareOne.isPinned &&
                isSystemApp == compareOne.isSystemApp &&
                isEnabled == compareOne.isEnabled &&
                uid == compareOne.uid &&
                packageName.equals(compareOne.packageName) &&
                externalDir.equals(compareOne.externalDir) &&
                boostStatus == compareOne.boostStatus;
    }
}

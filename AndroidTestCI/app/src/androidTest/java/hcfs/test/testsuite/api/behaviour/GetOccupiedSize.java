package hcfs.test.testsuite.api.behaviour;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import hcfs.test.spec.HCFSStat;
import hcfs.test.spec.JsonFields;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetOccupiedSize {

	private static final String THIS_CLASS = GetOccupiedSize.class.getSimpleName();

	/**	
	 * void HCFS_get_occupied_size(char ** json_res)
	 * To fetch the value of occupied size (Unpin-but-dirty size + Pin size).
	 * Return data dict in json_res -
	 * data: {
	 *     occupied: Bytes,
	 *     }
	 * Return code -	
	 *    True 	0
	 *    False 	Linux errors.
	 * 
	 * 	json_res		result string in json format.
	 */

	private Context mContext;

	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();
		setWIFI(false);
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");
		setWIFI(true);
	}
	
	@Test
	public void occupiedSizeEqualsCacheDirtyPlusPinTotalCase() throws Exception {
		Logs.i(THIS_CLASS, "occupiedSizeEqualsCacheDirtyPlusPinTotalCase", "");
		HCFSAPI.Result result = HCFSAPI.getOccupiedSize();
		assertTrue(result.isSuccess);
		long occupiedSize = toLong(result.data.get(JsonFields.OCCUPIED_SIZE));

		result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		long cacheDirty = toLong(result.data.get(HCFSStat.CACHE_STORAGE_DIRTY));
		long pinTotal = toLong(result.data.get(HCFSStat.PIN_SPACE_TOTAL_USED));

		//cacheDirty = Pin but dirty + unpin but dirty
		//occupiedSize = unpin but dirty + pinTotal
		assertTrue(occupiedSize <= cacheDirty + pinTotal);
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private long toLong(Object obj) {
		if(obj instanceof Integer)
			return ((Integer) obj).longValue();
		else if(obj instanceof Long)
			return (Long) obj;
		throw new RuntimeException("Should be either Integer or Long.");
	}
}

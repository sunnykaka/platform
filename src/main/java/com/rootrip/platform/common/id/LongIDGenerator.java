package com.rootrip.platform.common.id;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author liubin
 *
 */
public class LongIDGenerator implements IDGenerator {

	private static final AtomicInteger intSequence;

	static {
		intSequence = new AtomicInteger(0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.common.util.IDGenerator#generate()
	 */
	public Serializable generate(Object obj) {
		long key = System.currentTimeMillis();
	    key = key * 10000;
	    key = key + intSequence.getAndIncrement();
	    intSequence.compareAndSet(9900, 0);
	    return key;
	}

}

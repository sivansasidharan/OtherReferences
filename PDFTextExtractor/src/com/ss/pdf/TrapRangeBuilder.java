package com.ss.pdf;

/**
 * Copyright (C) 2015, GIAYBAC
 *
 * Released under the MIT license
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;

/**
 *
 * @author THOQ LUONG Mar 19, 2015 10:43:22 PM
 */
public class TrapRangeBuilder {

	// --------------------------------------------------------------------------
	// Members
	private final Logger logger = LoggerFactory
			.getLogger(TrapRangeBuilder.class);
	private final List<Range<Integer>> ranges = new ArrayList<>();

	// --------------------------------------------------------------------------
	// Initialization and releasation
	// --------------------------------------------------------------------------
	// Getter N Setter
	// --------------------------------------------------------------------------
	// Method binding
	public TrapRangeBuilder addRange(Range<Integer> range) {
		ranges.add(range);
		return this;
	}

	/**
	 * The result will be ordered by lowerEndpoint ASC
	 *
	 * @return
	 */
	public List<Range<Integer>> build() {
		List<Range<Integer>> retVal = new ArrayList<>();
		// order range by lower Bound
		Collections.sort(ranges, new Comparator<Range>() {
			@Override
			public int compare(Range o1, Range o2) {
				return o1.lowerEndpoint().compareTo(o2.lowerEndpoint());
			}
		});

		for (Range<Integer> range : ranges) {
			if (retVal.isEmpty()) {
				retVal.add(range);
			} else {

				Range<Integer> lastRange = retVal.get(retVal.size() - 1);
				if (lastRange.isConnected(range)
				// || lastRange.lowerEndpoint() == 133
				) {
					try {
						Range newLastRange = lastRange.span(range);
						retVal.set(retVal.size() - 1, newLastRange);
						// logger.info("trap- isConnected====== " +
						// newLastRange);

					} catch (Exception e) {

						logger.info(e.getMessage());
					}
				} else {

					// logger.info("trap- not isConnected====== " + range);
					retVal.add(range);
				}
			}
		}
		// debug
		logger.info("trap-range====== " + retVal);

		// return
		return retVal;
	}
	// --------------------------------------------------------------------------
	// Implement N Override
	// --------------------------------------------------------------------------
	// Utils
	// --------------------------------------------------------------------------
	// Inner class
}

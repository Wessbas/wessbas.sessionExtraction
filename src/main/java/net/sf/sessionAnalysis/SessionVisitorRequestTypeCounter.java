/***************************************************************************
 * Copyright (c) 2016 the WESSBAS project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/


package net.sf.sessionAnalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Andre van Hoorn
 *
 */
public class SessionVisitorRequestTypeCounter implements ISessionDatVisitor {
	//	private final int resolutionValueOriginal;
	//	private final TimeUnit resolutionTimeUnitOriginal;
	//	private final long resolutionValueNanos;
	
	/** Stores the list of actions (login, etc.) that are used as keys for the counts table */
	//private final HashSet<String> actions = new HashSet<String>();
	/** Future work: Counts per request type per time bin */
	// private final HashMap<String, TreeMap<Long, AtomicInteger>> counts = new HashMap<String, TreeMap<Long,AtomicInteger>>();
	
	private final HashMap<String, AtomicInteger> countsPerAction = new HashMap<String, AtomicInteger>();
	
//	public SessionVisitorRequestTypeCounter(int resolutionValueOriginal, TimeUnit resolutionTimeUnitOriginal, long resolutionValueNanos) {
		// this.resolutionValueOriginal = resolutionValueOriginal;
		// this.resolutionTimeUnitOriginal = resolutionTimeUnitOriginal;
		// this.resolutionValueNanos = resolutionValueNanos;
//	}

	
	public void handleSession(Session session) {
		for (UserAction action : session.getUserActions()) {
			AtomicInteger curCount = this.countsPerAction.get(action.getActionName());
			if (curCount == null) {
				curCount = new AtomicInteger(0);
				this.countsPerAction.put(action.getActionName(), curCount);
			}
			curCount.incrementAndGet();			
		}
	}

	public void writeCallFrequencies(final String outputDir) throws IOException  {
		FileWriter fw = new FileWriter(outputDir + "/" + this.getClass().getSimpleName()+"-totalRequestsCountsPerAction.csv");
        BufferedWriter writer = new BufferedWriter(fw);
        
        writer.write("action;numRequests");
        writer.newLine();
        for (Entry<String, AtomicInteger> event: this.countsPerAction.entrySet()) {
        	writer.write(event.getKey() + ";" + event.getValue());
        	writer.newLine();
        }
        
        writer.close();
        fw.close();
	}
	
	public void handleEOF() {
		// Nothing to do
	}
}

package com.ljy.misc;

import com.lmax.disruptor.EventFactory;

public class LogicEventFactory implements EventFactory<LogicEvent>{

	@Override
	public LogicEvent newInstance() {

		return new LogicEvent();
		
	}

}

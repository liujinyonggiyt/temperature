package com.ljy.misc;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;

import java.util.concurrent.locks.LockSupport;

/**
 * 类似于disruptor库里自带的SleepingWaitStrategy类
 */
public class SleepingWaitExtendStrategy implements WaitStrategy {
	
	private volatile boolean needSleep=true;
	
	private final EventConsumer<?> eventConsumer;

	private static final int DEFAULT_RETRIES = 200;

	private final int retries;

	public SleepingWaitExtendStrategy(EventConsumer<?> eventConsumer) {

		this.retries = DEFAULT_RETRIES;

		this.eventConsumer = eventConsumer;

	}

	@Override
	public long waitFor(final long sequence, Sequence cursor,
			final Sequence dependentSequence, final SequenceBarrier barrier)
			throws AlertException, InterruptedException {
		long availableSequence;
		int counter = retries;

		while ((availableSequence = dependentSequence.get()) < sequence) {


				eventConsumer.loopTemplate();


			counter = applyWaitMethod(barrier, counter);

		}

		return availableSequence;
	}

	@Override
	public void signalAllWhenBlocking() {
	}

	private int applyWaitMethod(final SequenceBarrier barrier, int counter)
			throws AlertException {
		barrier.checkAlert();
		if (counter > 100) {
			--counter;
		} else if (counter > 0) {
			--counter;
			Thread.yield();
		} else {
			if(needSleep){
				LockSupport.parkNanos(10000L);
			}
		}
		return counter;
	}

	public void setNeedSleep(boolean needSleep) {
		this.needSleep = needSleep;
	}
}

package org.apache.log4j.extras;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * An EventEvaluator that returns true only when a certain number of events
 * have occurred.
 *
 * @author Christopher Schultz
 */
public class DelayedTriggeringEventEvaluator
    implements TriggeringEventEvaluator
{
    /**
     * Buffer size of the parent appender is not accessible, so we
     * have to have our own triggering event count.
     */
    private int _triggeringEventCount = 512;

    /**
     * The number of events seen since the last triggering event.
     */
    private int _eventCount = 0;

    /**
     * Sets the number of events that must occur before this evaluator
     * indicates that the event is a "triggering event".
     *
     * @param events The number of events required to cause this evaluator to trigger.
     */
    public synchronized void setEventCount(int events)
    {
        _triggeringEventCount = events;
    }

    @Override
    public synchronized boolean isTriggeringEvent(LoggingEvent event)
    {
        if(_eventCount++ < _triggeringEventCount)
        {
            return false;
        }
        else
        {
            _eventCount = 1;
            return true;
        }
    }
}

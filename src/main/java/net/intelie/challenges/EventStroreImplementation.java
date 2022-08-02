package net.intelie.challenges;

import net.intelie.challenges.interfaces.EventIterator;
import net.intelie.challenges.models.Event;

import java.util.Collection;

//main class implementation
public class EventStoreImplementation implements EventIterator {
    private Collection<Event> events;
    private Boolean nextMethodCalledOnce = false;
    private Boolean nextLastResult = null;
    private int currentPosition = -1;
    private final String illegalStateMessage = "method next has never been called or its false";

    //event sorting class implementation
    public EventStoreImplementation(Collection<Event> events) {this.events = events;}

    @Override
    public boolean nextMove() { //nextMove Method creation
        nextMethodCalledOnce = true;
        boolean hasReachEnd = currentPosition == events.size()-1;
        
        if (hasReachEnd) { //check if you got to the last element
            nextLastResult = false;
            return false;
        }
      
        nextLastResult = true;
        currentPosition++;
        return true;
    }

    @Override
    public Event current() {
        if (!nextMethodCalledOnce || (nextLastResult == false)) {
            throw new IllegalStateException(illegalStateMessage);
        }

        return (Event) events.toArray()[currentPosition];
    }

    @Override
    public void remove() {
        if (!nextMethodCalledOnce || (nextLastResult == false)) {
            throw new IllegalStateException(illegalStateMessage);
        }

        Event currentEvent = current();
        events.remove(currentEvent);
        (new EventDAO()).remove(currentEvent);
        currentPosition--;
    }

    @Override
    public void close() {
        nextMethodCalledOnce = false;
        nextLastResult = null;
        currentPosition = -1;
        events = null;
    }
}


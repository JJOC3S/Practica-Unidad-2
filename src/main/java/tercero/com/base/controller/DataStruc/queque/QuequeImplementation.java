package tercero.com.base.controller.DataStruc.queque;

import tercero.com.base.controller.DataStruc.List.LinkedList;

public class QuequeImplementation<E> extends LinkedList<E> {
    private Integer top;

    //getter 
    public Integer getTop() {
        return top;
    }
    
    public QuequeImplementation(Integer top) {
       
        this.top = top;
    }

    protected boolean isFullQueue() {
        return this.top >= getLength();
    }

    protected void queue(E info) throws Exception {
        if (!isFullQueue()) {
            add( info, 0);
        } else {
            throw new ArrayIndexOutOfBoundsException("Queque is full");
        }
    }

    protected E dequeue()throws Exception {
        return deleteFirst(); 
    }

    
}

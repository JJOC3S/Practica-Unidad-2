package tercero.com.base.controller.DataStruc.stack;

public class Stack<E> {
    private StackImplementation<E> stack;

    public Stack(Integer top){
        stack = new StackImplementation<>(top);

    }

    public Boolean push(E data){
        try {
            stack.push(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public E pop() {
        try {
            return stack.pop();
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean isFullStack(){
        return stack.isFull();
    }

    public Integer top(){
        return stack.getTop();
    }

    public Integer size(){
        return stack.getLength();
    }
}

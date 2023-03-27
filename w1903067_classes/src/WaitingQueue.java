public class WaitingQueue<E> {

    private int front;
    private int end;
    private E[] customersInQueue;
    private int queueLimit;
    private int currentLength;

    public WaitingQueue(int qLimit) {
        this.queueLimit = qLimit;
        customersInQueue = (E[]) new Object[this.queueLimit];
        currentLength = 0;
        front = -1;
        end = -1;
    }

    //to check if waiting list is empty
    public boolean isWQueueEmpty() {
        return (currentLength == 0);
    }

    //to check if waiting list is full
    public boolean isWQueueFull() {
        return (currentLength == customersInQueue.length);
    }


    //add customers to end of the waiting list
    public void addQueue(E item) {

        try {
            if (!(isWQueueFull())) {
                end = (end + 1) % customersInQueue.length;
                customersInQueue[end] = item;
                currentLength++;

                if (front == -1) {
                    front = end;
                }
            }
        } catch (QueueFullException e) {
            System.out.println(("Waiting list is full."));
        }
    }

    //take customers from the front of the waiting list
    public E takeQueue() throws QueueEmptyException {
        E removeFromWaitingQ = null;
        try {
            if (!(isWQueueEmpty())) {
                removeFromWaitingQ = customersInQueue[front];
                customersInQueue[front] = null;
                front = (front + 1) % customersInQueue.length;
                currentLength--;
            }

        } catch (QueueEmptyException e) {
            System.out.println("Waiting list is empty.");
        }
        return removeFromWaitingQ;
    }
}

class QueueEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueueEmptyException() {
        super();
    }

    public QueueEmptyException(String message) {
        super(message);
    }

}

class QueueFullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueueFullException() {
        super();
    }

    public QueueFullException(String message) {
        super(message);
    }

}


//reference: https://www.javainuse.com/java/circular_java


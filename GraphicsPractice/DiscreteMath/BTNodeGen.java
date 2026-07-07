
public class BTNodeGen<T extends Comparable<T>> {

    private BTNodeGen<T> left;
    private BTNodeGen<T> right;
    private T data;

    public BTNodeGen<T> getLeft() {
        return left;
    }

    public BTNodeGen<T> getRight() {
        return right;
    }

    public T getData() {
        return data;
    }

    public void setLeft(BTNodeGen<T> l) {
        left = l;
    }

    public void setRight(BTNodeGen<T> r) {
        right = r;
    }

    public void setData(T d) {
        data = d;
    }

    public int countLeftOrRightOnly() {
        // write your code here 
        int count = 0;
        if (left != null && right == null || right != null && left == null) {
            count++;

        }
        if (left != null) {
            count += left.countLeftOrRightOnly();
        }
        if (right != null) {
            count += right.countLeftOrRightOnly();
        }
        return count;
    }

    public T peekLast(){
        int size = this.length();
        T last = null;
        for(int i = 0; i < size; i++){
            if(i == size){
                last = this.remove();
            }
        } return last;
    }



}

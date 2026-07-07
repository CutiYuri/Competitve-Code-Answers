 


    package CSCI1933P3;

public class ArrayList<T extends Comparable<T>> implements List<T> {
    private T[] array;
    private int size;
    private boolean isSorted;

    public ArrayList() {
        array = (T[]) new Comparable[2];
        size = 0;
        isSorted = true;
    }

    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        if (size == array.length) {
            resizeArray();
        }
        array[size] = element;
        if (size > 0 && array[size-1].compareTo(element) > 0) {
            isSorted = false;
        }
        size++;
        return true;
    }

    public boolean add(int index, T element) {
        if (element == null || index < 0 || index >= size) {
            return false;
        }
        if (size == array.length) {
            resizeArray();
        }
        for (int i = size; i > index; i--) {
            array[i] = array[i-1];
        }
        array[index] = element;
        size++;
        // Check if added element breaks sorted order
        if (isSorted) {
            if ((index > 0 && array[index-1].compareTo(element) > 0) ||
                (index < size-1 && element.compareTo(array[index+1]) > 0)) {
                isSorted = false;
            }
        }
        return true;
    }

    private void resizeArray() {
        T[] newArray = (T[]) new Comparable[array.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void clear() {
        array = (T[]) new Comparable[2];
        size = 0;
        isSorted = true;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[index];
    }

    public int indexOf(T element) {
        if (element == null) {
            return -1;
        }
        if (isSorted) {
            // Binary search for optimized case
            int low = 0, high = size - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                int cmp = element.compareTo(array[mid]);
                if (cmp < 0) {
                    high = mid - 1;
                } else if (cmp > 0) {
                    low = mid + 1;
                } else {
                    // Find first occurrence
                    while (mid > 0 && array[mid-1].equals(element)) {
                        mid--;
                    }
                    return mid;
                }
            }
            return -1;
        } else {
            // Linear search
            for (int i = 0; i < size; i++) {
                if (array[i].equals(element)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void sort() {
        if (isSorted) {
            return;
        }
        // Insertion sort
        for (int i = 1; i < size; i++) {
            T key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j+1] = array[j];
                j--;
            }
            array[j+1] = key;
        }
        isSorted = true;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        T removed = array[index];
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i+1];
        }
        array[size-1] = null;
        size--;
        // Check if still sorted
        if (isSorted && size > 1) {
            for (int i = 1; i < size; i++) {
                if (array[i-1].compareTo(array[i]) > 0) {
                    isSorted = false;
                    break;
                }
            }
        }
        return removed;
    }

    public void reverse() {
        for (int i = 0; i < size / 2; i++) {
            T temp = array[i];
            array[i] = array[size - 1 - i];
            array[size - 1 - i] = temp;
        }
        if (size > 1) {
            isSorted = false;
        }
    }

    public void removeDuplicates() {
        if (size <= 1) {
            return;
        }
        if (isSorted) {
            // More efficient removal for sorted lists
            int writeIndex = 1;
            for (int readIndex = 1; readIndex < size; readIndex++) {
                if (!array[readIndex].equals(array[writeIndex-1])) {
                    array[writeIndex++] = array[readIndex];
                }
            }
            // Null out remaining elements
            for (int i = writeIndex; i < size; i++) {
                array[i] = null;
            }
            size = writeIndex;
        } else {
            // Brute force for unsorted lists
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size;) {
                    if (array[i].equals(array[j])) {
                        remove(j);
                    } else {
                        j++;
                    }
                }
            }
        }
    }

    public void intersect(List<T> otherList) {
        if (otherList == null) {
            return;
        }
        ArrayList<T> other = (ArrayList<T>) otherList;
        sort();
        other.sort();
        
        int writeIndex = 0;
        int i = 0, j = 0;
        while (i < size && j < other.size) {
            int cmp = array[i].compareTo(other.array[j]);
            if (cmp < 0) {
                i++;
            } else if (cmp > 0) {
                j++;
            } else {
                // Add only if not duplicate
                if (writeIndex == 0 || !array[i].equals(array[writeIndex-1])) {
                    array[writeIndex++] = array[i];
                }
                i++;
                j++;
            }
        }
        // Null out remaining elements
        for (int k = writeIndex; k < size; k++) {
            array[k] = null;
        }
        size = writeIndex;
        isSorted = true;
    }

    public void merge(List<T> list) {
        if (list == null) {
            return;
        }
        ArrayList<T> other = (ArrayList<T>) list;
        sort();
        other.sort();
        
        T[] newArray = (T[]) new Comparable[size + other.size];
        int i = 0, j = 0, k = 0;
        while (i < size && j < other.size) {
            if (array[i].compareTo(other.array[j]) <= 0) {
                newArray[k++] = array[i++];
            } else {
                newArray[k++] = other.array[j++];
            }
        }
        while (i < size) {
            newArray[k++] = array[i++];
        }
        while (j < other.size) {
            newArray[k++] = other.array[j++];
        }
        array = newArray;
        size = k;
        isSorted = true;
    }

    public T getMin() {
        if (isEmpty()) {
            return null;
        }
        if (isSorted) {
            return array[0];
        }
        T min = array[0];
        for (int i = 1; i < size; i++) {
            if (array[i].compareTo(min) < 0) {
                min = array[i];
            }
        }
        return min;
    }

    public T getMax() {
        if (isEmpty()) {
            return null;
        }
        if (isSorted) {
            return array[size-1];
        }
        T max = array[0];
        for (int i = 1; i < size; i++) {
            if (array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }
        return max;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(array[i].toString());
            if (i < size - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public boolean isSorted() {
        return isSorted;
    }
}






package CSCI1933P3;

public class LinkedList<T extends Comparable<T>> implements List<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private boolean isSorted;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
        isSorted = true;
    }

    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        Node<T> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
            if (isSorted && tail.getData().compareTo(tail.getNext().getData()) < 0) {
                isSorted = false;
            }
        }
        size++;
        return true;
    }

    public boolean add(int index, T element) {
        if (element == null || index < 0 || index >= size) {
            return false;
        }
        Node<T> newNode = new Node<>(element);
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            if (newNode.getNext() == null) {
                tail = newNode;
            }
        }
        // Check if added element breaks sorted order
        if (isSorted) {
            Node<T> prev = getNodeAtIndex(index - 1);
            Node<T> next = getNodeAtIndex(index + 1);
            if ((prev != null && prev.getData().compareTo(element) > 0) ||
                (next != null && element.compareTo(next.getData()) > 0)) {
                isSorted = false;
            }
        }
        size++;
        return true;
    }

    private Node<T> getNodeAtIndex(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
        isSorted = true;
    }

    public T get(int index) {
        Node<T> node = getNodeAtIndex(index);
        return node == null ? null : node.getData();
    }

    public int indexOf(T element) {
        if (element == null) {
            return -1;
        }
        if (isSorted) {
            // Binary search for optimized case
            int low = 0, high = size - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                T midData = get(mid);
                int cmp = element.compareTo(midData);
                if (cmp < 0) {
                    high = mid - 1;
                } else if (cmp > 0) {
                    low = mid + 1;
                } else {
                    // Find first occurrence
                    while (mid > 0 && get(mid-1).equals(element)) {
                        mid--;
                    }
                    return mid;
                }
            }
            return -1;
        } else {
            // Linear search
            Node<T> current = head;
            int index = 0;
            while (current != null) {
                if (current.getData().equals(element)) {
                    return index;
                }
                current = current.getNext();
                index++;
            }
            return -1;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void sort() {
        if (isSorted || size <= 1) {
            return;
        }
        // Insertion sort
        Node<T> sorted = null;
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.getNext();
            if (sorted == null || sorted.getData().compareTo(current.getData()) >= 0) {
                current.setNext(sorted);
                sorted = current;
            } else {
                Node<T> temp = sorted;
                while (temp.getNext() != null && 
                       temp.getNext().getData().compareTo(current.getData()) < 0) {
                    temp = temp.getNext();
                }
                current.setNext(temp.getNext());
                temp.setNext(current);
            }
            current = next;
        }
        head = sorted;
        // Update tail
        tail = head;
        while (tail != null && tail.getNext() != null) {
            tail = tail.getNext();
        }
        isSorted = true;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        T removed;
        if (index == 0) {
            removed = head.getData();
            head = head.getNext();
            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> prev = getNodeAtIndex(index - 1);
            removed = prev.getNext().getData();
            prev.setNext(prev.getNext().getNext());
            if (prev.getNext() == null) {
                tail = prev;
            }
        }
        size--;
        // Check if still sorted
        if (isSorted && size > 1) {
            Node<T> current = head;
            while (current.getNext() != null) {
                if (current.getData().compareTo(current.getNext().getData()) > 0) {
                    isSorted = false;
                    break;
                }
                current = current.getNext();
            }
        }
        return removed;
    }

    public void reverse() {
        Node<T> prev = null;
        Node<T> current = head;
        tail = head;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setNext(prev);
            prev = current;
            current = next;
        }
        head = prev;
        if (size > 1) {
            isSorted = false;
        }
    }

    public void removeDuplicates() {
        if (size <= 1) {
            return;
        }
        if (isSorted) {
            // More efficient removal for sorted lists
            Node<T> current = head;
            while (current != null && current.getNext() != null) {
                if (current.getData().equals(current.getNext().getData())) {
                    current.setNext(current.getNext().getNext());
                    size--;
                } else {
                    current = current.getNext();
                }
            }
            tail = current;
        } else {
            // Brute force for unsorted lists
            Node<T> current = head;
            while (current != null && current.getNext() != null) {
                Node<T> runner = current;
                while (runner.getNext() != null) {
                    if (current.getData().equals(runner.getNext().getData())) {
                        runner.setNext(runner.getNext().getNext());
                        size--;
                    } else {
                        runner = runner.getNext();
                    }
                }
                current = current.getNext();
            }
            tail = current;
        }
    }

    public void intersect(List<T> otherList) {
        if (otherList == null) {
            return;
        }
        LinkedList<T> other = (LinkedList<T>) otherList;
        sort();
        other.sort();
        
        Node<T> dummy = new Node<>(null);
        Node<T> tail = dummy;
        Node<T> a = head;
        Node<T> b = other.head;
        
        while (a != null && b != null) {
            int cmp = a.getData().compareTo(b.getData());
            if (cmp < 0) {
                a = a.getNext();
            } else if (cmp > 0) {
                b = b.getNext();
            } else {
                // Add only if not duplicate
                if (tail.getData() == null || !a.getData().equals(tail.getData())) {
                    tail.setNext(new Node<>(a.getData()));
                    tail = tail.getNext();
                }
                a = a.getNext();
                b = b.getNext();
            }
        }
        head = dummy.getNext();
        this.tail = tail;
        size = countNodes(head);
        isSorted = true;
    }

    private int countNodes(Node<T> node) {
        int count = 0;
        while (node != null) {
            count++;
            node = node.getNext();
        }
        return count;
    }

    public void merge(List<T> list) {
        if (list == null) {
            return;
        }
        LinkedList<T> other = (LinkedList<T>) list;
        sort();
        other.sort();
        
        Node<T> dummy = new Node<>(null);
        Node<T> tail = dummy;
        Node<T> a = head;
        Node<T> b = other.head;
        
        while (a != null && b != null) {
            if (a.getData().compareTo(b.getData()) <= 0) {
                tail.setNext(a);
                a = a.getNext();
            } else {
                tail.setNext(b);
                b = b.getNext();
            }
            tail = tail.getNext();
        }
        tail.setNext(a != null ? a : b);
        head = dummy.getNext();
        // Update tail
        this.tail = head;
        while (this.tail != null && this.tail.getNext() != null) {
            this.tail = this.tail.getNext();
        }
        size = countNodes(head);
        isSorted = true;
    }

    public T getMin() {
        if (isEmpty()) {
            return null;
        }
        if (isSorted) {
            return head.getData();
        }
        T min = head.getData();
        Node<T> current = head.getNext();
        while (current != null) {
            if (current.getData().compareTo(min) < 0) {
                min = current.getData();
            }
            current = current.getNext();
        }
        return min;
    }

    public T getMax() {
        if (isEmpty()) {
            return null;
        }
        if (isSorted) {
            return tail.getData();
        }
        T max = head.getData();
        Node<T> current = head.getNext();
        while (current != null) {
            if (current.getData().compareTo(max) > 0) {
                max = current.getData();
            }
            current = current.getNext();
        }
        return max;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.getData().toString());
            if (current.getNext() != null) {
                sb.append("\n");
            }
            current = current.getNext();
        }
        return sb.toString();
    }

    public boolean isSorted() {
        return isSorted;
    }
}
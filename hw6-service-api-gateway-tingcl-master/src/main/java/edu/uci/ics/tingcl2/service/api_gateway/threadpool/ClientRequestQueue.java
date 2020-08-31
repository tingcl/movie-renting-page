package edu.uci.ics.tingcl2.service.api_gateway.threadpool;

import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        head = tail = null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
        ListNode temp = new ListNode(clientRequest);
        if(tail == null){
            head = tail = temp;
        }
        else{
           tail.setNext(temp);
           tail = temp;
        }
        notify();
    }
    public synchronized ClientRequest dequeue() {
        while(isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                ServiceLogger.LOGGER.warning("While thread was waiting something failed.");
            }
        }
        ListNode temp = head;
        head = head.getNext();
        if(head == null){
            tail = null;
        }
        return temp.getClientRequest();
    }

    boolean isEmpty() {
        return head == null;
    }

    boolean isFull() {
        return false;
    }
}


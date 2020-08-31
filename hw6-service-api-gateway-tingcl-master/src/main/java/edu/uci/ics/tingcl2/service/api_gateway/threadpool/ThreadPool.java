package edu.uci.ics.tingcl2.service.api_gateway.threadpool;

import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;

public class ThreadPool {
    private int numWorkers;
    private Worker[] workers;
    private ClientRequestQueue queue;

    public ThreadPool(int numWorkers) {
        // Initialize max number of threads
        this.numWorkers = numWorkers;
        // Initialize array of worker threads
        this.workers = new Worker[this.numWorkers];
        // Initialize client request queue
        this.queue = new ClientRequestQueue();
        for(int i = 0; i < numWorkers; i++){
            Worker object = Worker.CreateWorker(i, this);
            ServiceLogger.LOGGER.info("Created worker thread: " + i);
            workers[i] = object;
            workers[i].start();
        }
    }
    public void add(ClientRequest clientRequest) {
        queue.enqueue(clientRequest);
    }
    public ClientRequest remove() {
        return queue.dequeue();
    }
    public ClientRequestQueue getQueue() {
        return queue;
    }
}

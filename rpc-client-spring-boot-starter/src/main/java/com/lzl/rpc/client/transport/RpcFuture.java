package com.lzl.rpc.client.transport;

import java.util.concurrent.*;

public class RpcFuture<T> implements Future<T> {


    private T response;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (countDownLatch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    public void setResponse(T response) {
        this.response = response;
        countDownLatch.countDown();
    }
}

package in.kashyapp.horde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.exceptions.JedisException;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static com.google.common.collect.Queues.newLinkedBlockingQueue;

/**
 * User: kashyapp
 * Time: 10/07/13 12:01 PM
 */
public class RedisPipeline extends AbstractLifeCycle implements Runnable {
    private final Logger log = LoggerFactory.getLogger(RedisPipeline.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final BlockingQueue<Request> queue = newLinkedBlockingQueue();
    private final JedisFactory jedisFactory;
    private final Thread worker;
    private volatile boolean shutdown = false;

    public RedisPipeline(JedisFactory jedisFactory) {
        this.jedisFactory = jedisFactory;
        worker = new Thread(this, RedisPipeline.class.getSimpleName());
    }

    public void request(String userId, Continuation continuation) {
        queue.add(new Request(userId, continuation));
    }

    @Override
    protected void doStart() throws Exception {
        log.info("Started");
        worker.start();
    }

    @Override
    protected void doStop() throws Exception {
        log.info("Stopping");
        shutdown = true;
        worker.interrupt();
        worker.join();
        log.info("Stopped");
    }

    @Override
    public void run() {
        final Jedis jedis = jedisFactory.get();

        while (!shutdown) {
            final ArrayList<Request> requests = Lists.newArrayList();
            try {
                requests.add(queue.take());
                queue.drainTo(requests);

                completeWithResponses(
                        requests,
                        getPipelinedResponses(jedis, requests));

            } catch (InterruptedException e) {
                // TODO
                return;
            }
        }
    }

    private void completeWithResponses(ArrayList<Request> requests, List<Object> responses) {
        for (int i = 0; i < requests.size(); ++i) {
            Request request = requests.get(i);
            Object response = responses.get(i);
            if (response instanceof Map) {
                try {
                    mapper.writeValue(
                            request.continuation.getServletResponse().getWriter(),
                            response
                    );
                } catch (IOException e) {
                    // TODO
                }
            }
            request.continuation.complete();
        }
    }

    private List<Object> getPipelinedResponses(Jedis jedis, ArrayList<Request> requests) {
        log.debug("Count: {}", requests.size());

        final Pipeline pipeline = jedis.pipelined();

        for (Request request : requests) {
            pipeline.hgetAll(request.userId);
        }

        return pipeline.syncAndReturnAll();
    }

    private class Request {
        private final String userId;
        private final Continuation continuation;

        public Request(String userId, Continuation continuation) {
            this.userId = userId;
            this.continuation = continuation;
        }
    }
}

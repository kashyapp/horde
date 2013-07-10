package in.kashyapp.horde;

import redis.clients.jedis.Jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: kashyapp
 * Time: 08/07/13 3:51 PM
 */
public class ThreadLocalJedisFactory implements JedisFactory {
    private static final Logger log = LoggerFactory.getLogger(ThreadLocalJedisFactory.class);

    private final ThreadLocal<Jedis> jedisHolder = new ThreadLocal<Jedis>();

    public ThreadLocalJedisFactory() {
    }

    @Override
    public Jedis get() {
        Jedis jedis = jedisHolder.get();
        if (jedis == null) {
            log.info("new Jedis instance for {}", Thread.currentThread());
            jedis = new Jedis("localhost", 28330);
            jedisHolder.set(jedis);
        }
        return jedis;
    }
}

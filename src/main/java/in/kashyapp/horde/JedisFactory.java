package in.kashyapp.horde;

import redis.clients.jedis.Jedis;

import com.google.common.base.Supplier;

/**
 * User: kashyapp
 * Time: 08/07/13 3:49 PM
 */
public interface JedisFactory extends Supplier<Jedis> {
}

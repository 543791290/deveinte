package com.haothink.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author wangh
 * 另一种配置方式可以参考
 * spring整合redis
 * http://www.codeceo.com/article/redis-spring-cache.html
 * 
 */
public final class RedisUtil {
	
	 private static JedisPool pool = null;
	    private static ThreadLocal<JedisPool> poolThreadLocal = new ThreadLocal<JedisPool>();
	 
	    /**
	     * 构建redis连接池
	     * 
	     * @param ip
	     * @param port
	     * @return JedisPool
	     */
	    public static JedisPool getPool() {
	        if (pool == null) {
	            JedisPoolConfig config = new JedisPoolConfig();
	            config.setMaxTotal(Integer.valueOf(PropUtils.getRedisValue("redis.pool.maxActive")));
	            config.setMaxIdle(Integer.valueOf(PropUtils.getRedisValue("redis.pool.maxIdle")));
	            config.setMaxWaitMillis(Long.valueOf(PropUtils.getRedisValue("redis.pool.maxWait")));
	            config.setTestOnBorrow(Boolean.valueOf(PropUtils.getRedisValue("redis.pool.testOnBorrow")));
	            config.setTestOnReturn(Boolean.valueOf(PropUtils.getRedisValue("redis.pool.testOnReturn")));
	            // 测试环境
	            // pool = new JedisPool(config, bundle.getString("redis.ip"),
	            // Integer.valueOf(bundle.getString("redis.port")));
	            // 线上环境
	            pool = new JedisPool(config, PropUtils.getRedisValue("redis.ip"), Integer.valueOf(PropUtils.getRedisValue("redis.port")),
	                    100000, PropUtils.getRedisValue("redis.pool.password"));
	        }
	        return pool;
	    }
	 
	    public static JedisPool getConnection() {
	        // ②如果poolThreadLocal没有本线程对应的JedisPool创建一个新的JedisPool，将其保存到线程本地变量中。
	        if (poolThreadLocal.get() == null) {
	            pool = RedisUtil.getPool();
	            poolThreadLocal.set(pool);
	            return pool;
	        } else {
	            return poolThreadLocal.get();// ③直接返回线程本地变量
	        }
	    }
	 
	    /**
	     * 返还到连接池
	     * 
	     * @param pool
	     * @param redis
	     */
	    public static void returnResource(JedisPool pool, Jedis redis) {
	        if (redis != null) {
	            pool.close();
	        }
	    }
	 
	    /**
	     * 获取数据
	     * 
	     * @param key
	     * @return
	     */
	    public static String get(String key) {
	        String value = null;
	 
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            value = jedis.get(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            // 返还到连接池
	        	pool.close();
	        }
	 
	        return value;
	    }
	 
	    /**
	     * 获取数据
	     * 
	     * @param key
	     * @return
	     */
	    public static byte[] get(byte[] key) {
	        byte[] value = null;
	 
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            value = jedis.get(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            // 返还到连接池
	        	pool.close();
	        }
	 
	        return value;
	    }
	 
	    /**
	     * 删除数据
	     * 
	     * @param key
	     * @return
	     */
	    public static Long del(String key) {
	        Long value = null;
	 
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            value = jedis.del(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            // 返还到连接池
	        	pool.close();
	        }
	 
	        return value;
	    }
	 
	    /**
	     * 删除数据
	     * 
	     * @param key
	     * @return
	     */
	    public static Long del(byte[] key) {
	        Long value = null;
	 
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            value = jedis.del(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            // 返还到连接池
	        	 pool.close();
	        }
	 
	        return value;
	    }
	 
	    /**
	     * 判断是否存在
	     * 
	     * @param key
	     * @return
	     */
	    public static Boolean exists(String key) {
	        Boolean value = null;
	 
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            value = jedis.exists(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            // 返还到连接池
	        	 pool.close();
	        }
	 
	        return value;
	    }
	 
	    /**
	     * 赋值数据
	     * 
	     * @param key
	     * @param value
	     * @param expireSeconds(过期时间，秒)
	     * @return value
	     */
	    public static Long set(String key, String value, int expireSeconds) {
	        Long result = null;
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            jedis.set(key, value);
	            result = jedis.expire(key, expireSeconds);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            pool.close();
	        }
	 
	        return result;
	    }
	 
	    /**
	     * 设置过期时间
	     * 
	     * @param key
	     * @param expireSeconds(过期时间，秒)
	     * @return value
	     */
	    public static Long expire(String key, int expireSeconds) {
	        Long result = null;
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            result = jedis.expire(key, expireSeconds);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            pool.close();
	        }
	 
	        return result;
	    }
	 
	    /**
	     * 赋值数据
	     * 
	     * @param key
	     * @return
	     */
	    public static String set(String key, String value) {
	        String result = null;
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            result = jedis.set(key, value);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            pool.close();
	        }
	 
	        return result;
	    }
	 
	    /**
	     * 赋值数据
	     * 
	     * @param key
	     * @return
	     */
	    public static Long sadd(String key, String value) {
	        Long result = null;
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            result = jedis.sadd(key, value);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            pool.close();
	        }
	 
	        return result;
	    }
	 
	    /**
	     * 判断set中是否有值
	     * 
	     * @param key
	     * @return
	     */
	    public static Boolean sismember(String key, String member) {
	        Boolean result = null;
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            result = jedis.sismember(key, member);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 释放redis对象
	            // 返还到连接池
	        	 pool.close();
	        }
	 
	        return result;
	    }
}

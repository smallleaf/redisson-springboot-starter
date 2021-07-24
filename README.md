### redisson封装使用

使用方法
1. 直接使用RedissonClient

```

@Autowire
private RedissonClient redisson;
```

2. 使用分布式锁
```

@DistributeLock(name="testLock")
public void test(){

}
```

3. 或者使用
```

@Autowire
private RedissonDistributeLock lock;

```

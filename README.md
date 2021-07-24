### redisson封装使用

使用方法
1. 直接使用RedissonClient

```aidl

@Autowire
private RedissonClient redisson;
```

2. 使用分布式锁
```aidl

@DistributeLock(name="testLock")
public void test(){

}
```

3. 或者使用
```aidl

@Autowire
private RedissonDistributeLock lock;

```

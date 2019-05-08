###使用排他锁
```
User: id,name,age,version
```

操作放在同一个事务中
```
//获取对象
1.User user = userService.queryUserByLock(id); 
  (select * from user where id=#{id} for update)
  
//获取age并进行操作
2.age = user.getAge()+1;


//更新数据库
3.userService.updateAge(id,age);
  (update user set age=#{age} where id=#{id})

```


 ###使用共享锁
``` 
//获取对象
1.User user = userService.queryUser(id); 
	(select * from user where id=#{id})

//获取age并进行操作
2.age = user.getAge()+1;

//更新数据库
3.userService.updateAgeByVersion(id,age);
  (update user set age=#{age},version = version + 1 where id=#{id} and version=#{version})
```

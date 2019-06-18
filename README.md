# SQL Pie
A tool for creating SQL statement dynamically

# Usage
```JAVA
    String sql = SQL.pie(Select.class)
            .from("user", "u").page(1, 20).order(Bys.pie().asc("x").desc("y"))
            .query(Columns.pie().get("x", "y", "z"))
            .shout();
    System.out.println(sql);
``` 
OUTPUT: SELECT x AS `x`, y AS `y`, z AS `z` FROM user u ORDER BY x ASC, y DESC


```JAVA
    String sql = SQL.pie(Select.class)
            .from("user", "u")
            .shout();
    System.out.println(sql);
``` 
OUTPUT: SELECT * FROM user u

```JAVA
sql = SQL.pie(Update.class)
        .update("user")
        .filter(Conditions.pie().andEq("x", 1).andEq("y", 2))
        .set(Sets.pie().set("x", 2).set("y", 1))
        .shout();
System.out.println(sql);
``` 
OUTPUT: UPDATE user SET x= #{u1}, y= #{u2} WHERE (x = #{w1} AND y = #{w2})

```JAVA
sql = SQL.pie(Update.class)
        .update("user", "u")
        .filter(Conditions.pie().andEq("u.x", 1).andEq("u.y", 2).andEq("a.l", 3))
        .set(Sets.pie().set("u.x", 2).set("u.y", 1))
        .join(SQL.pie(LeftJoin.class).table("address", "a"))
        .on(Conditions.pie().andEq("u.id", "${a.id}"))
        .shout();
System.out.println(sql);
``` 
OUTPUT: UPDATE FROM user u LEFT JOIN address a ON u.id = a.id SET u.x= #{u1}, u.y= #{u2} WHERE (u.x = #{w1} AND u.y = #{w2} AND a.l = #{w3})
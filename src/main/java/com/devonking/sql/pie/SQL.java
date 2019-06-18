package com.devonking.sql.pie;

import com.devonking.sql.filter.Filter;
import com.devonking.sql.filter.FilterGroup;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.simple.SimpleStatementFactory;
import com.devonking.sql.pie.statement.LeftJoin;
import com.devonking.sql.pie.statement.Select;
import com.devonking.sql.pie.statement.Update;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SQL {


    private SQL() {}

    public static <T extends Statement> T pie(Class<T> clazz) {

        return pie(clazz, new SimpleStatementFactory());
    }

    public static <T extends Statement> T pie(Class<T> clazz, StatementFactory statementFactory) {
        clear();

        return statementFactory.getStatement(clazz);
    }

    private static void clear() {
        Filter.clear();
        FilterGroup.clear();
    }

    public static void main(String[] args) {
        String sql = SQL.pie(Select.class)
                .from("user", "u").page(1, 20).order(Bys.pie().asc("x").desc("y"))
                .query(Columns.pie().get("x", "y", "z"))
                .shout();
        System.out.println(sql);

        sql = SQL.pie(Select.class)
                .from("user", "u")
                .shout();
        System.out.println(sql);

//        sql = SQL.pie(Select.class)
//                .table("user", "u")
//                .filter(Conditions.pie().andEq("u.x", 1).orEq("y", 2).orRoll(Conditions.pie().andEq("y", 3).orEq("x", 5)))
//                .select(Columns.pie().get("u.x", "u.y", "u.z"))
//                .join(SQL.pie(Join.class).table("address", "a").select(Columns.pie().get("l", "m", "n")))
//                .on(Conditions.pie().andEq("x", 1).andRoll(Conditions.pie().andEq("y", 2)))
//                .join(SQL.pie(Join.class).table("car", "c"))
//                .on(Conditions.pie().andEq("c.x", "u.x"))
//                .shout();
//        SQL.clear();
//        System.out.println(sql);


        sql = SQL.pie(Update.class)
                .update("user")
                .filter(Conditions.pie().andEq("x", 1).andEq("y", 2))
                .set(Sets.pie().set("x", 2).set("y", 1))
                .shout();
        System.out.println(sql);


        sql = SQL.pie(Update.class)
                .update("user", "u")
                .filter(Conditions.pie().andEq("u.x", 1).andEq("u.y", 2).andEq("a.l", 3))
                .set(Sets.pie().set("u.x", 2).set("u.y", 1))
                .join(SQL.pie(LeftJoin.class).table("address", "a"))
                .on(Conditions.pie().andEq("u.id", "${a.id}"))
                .shout();
        System.out.println(sql);

    }
}

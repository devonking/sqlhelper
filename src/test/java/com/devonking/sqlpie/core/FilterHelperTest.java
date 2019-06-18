package com.devonking.sqlpie.core;

import com.devonking.sql.pie.SQL;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.statement.*;
import com.devonking.sql.mybatis.SqlMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterHelperTest {
    @Autowired
    private SqlMapper sqlMapper;

    @Test
    public void test05() {
        List<Map<String, Object>> resultList = SQL.pie(Select.class)
                .from("md_dict").fire(sqlMapper);

        resultList.forEach(r -> System.out.println(r.toString()));
    }

    @Test
    public void test06() {
        List<Map<String, Object>> resultList = SQL.pie(Native.class)
                .load("select * from md_dict where gmt_modified > #{now}").sets(Sets.pie().set("now", new Date())).fire(sqlMapper);

        resultList.forEach(r -> System.out.println(r.toString()));
    }

    @Test
    public void test07() {
        SQL.pie(Rename.class).from("sc_region_company").to("zzz@" + System.currentTimeMillis() + "$sc_region_company").fire(sqlMapper);
    }

    @Test
    public void test08() {
        System.out.println(
                SQL.pie(AlterColumn.class).table("rl_community_room").modify(sqlMapper, "mdm_devon", ColumnDefinition.builder().name("code2").newName("code").comment("好啊").build()).shout()
        );

//        System.out.println(
//                SQL.pie(AlterColumn.class).table("rl_community_room").modifyAll(ColumnDefinition.builder().name("code").newName("code2").comment("好啊").build()).shout()
//        );
    }

    @Test
    public void test09() {
//        List<MdHistoryShort> resultList = SQL.pie(Select.class)
//                .from("company_history")
//                .query(Columns.pie(MdHistoryShort.class))
//                .filter(Conditions.pie().andEq(BusinessConstant.DEFAULT_COLUMN_MD_CODING, "GSXX0001"))
//                .order(Bys.pie().desc(BusinessConstant.DEFAULT_COLUMN_HISTORY_MD_AUTO_ID))
//                .fire(sqlMapper, MdHistoryShort.class);
//
//        resultList.stream().forEach(System.out::println);
    }
}
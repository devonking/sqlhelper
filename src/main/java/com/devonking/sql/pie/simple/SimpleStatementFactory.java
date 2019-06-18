package com.devonking.sql.pie.simple;

import com.devonking.sql.pie.core.Statement;
import com.devonking.sql.pie.core.StatementFactory;
import com.devonking.sql.pie.statement.*;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleStatementFactory implements StatementFactory {

    @Override
    public <T extends Statement> T getStatement(Class<T> clazz) {
        if(clazz == Select.class) {
            return (T) new SimpleSelect();
        }
        if(clazz == Join.class) {
            return (T) new SimpleJoin();
        }
        if(clazz == LeftJoin.class) {
            return (T) new SimpleLeftJoin();
        }
        if(clazz == RightJoin.class) {
            return (T) new SimpleRightJoin();
        }
        if(clazz == Insert.class) {
            return (T) new SimpleInsert();
        }
        if(clazz == Update.class) {
            return (T) new SimpleUpdate();
        }
        if(clazz == Delete.class) {
            return (T) new SimpleDelete();
        }
        if(clazz == Native.class) {
            return (T) new SimpleNative();
        }
        if(clazz == Drop.class) {
            return (T) new SimpleDrop();
        }
        if(clazz == Rename.class) {
            return (T) new SimpleRename();
        }
        if(clazz == InsertSelect.class) {
            return (T) new SimpleInsertSelect();
        }
        if(clazz == AlterColumn.class) {
            return (T) new SimpleAlterColumn();
        }

        throw new RuntimeException("#SQL statement not supported");
    }
}

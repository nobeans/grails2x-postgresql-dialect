package hibernate

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.PostgreSQL9Dialect
import org.hibernate.id.SequenceGenerator
import org.hibernate.type.Type

import java.sql.Types

class MyPostgreSQLDialect extends PostgreSQL9Dialect {
    // 9.x用の場合。8.1以前はPostgreSQL81Dialect、8.2以降の8.xはPostgreSQL82Dialectを継承すること。

    MyPostgreSQLDialect() {
        // Stringに対するデフォルトカラム長である255になっている場合に、強制的にTEXTマッピングする。
        // それ以外はvarchar($l)で長さ指定のVARCHARにマッピングする。
        registerColumnType(Types.VARCHAR, 254, 'varchar($l)')   // 254以下はVARCHAR(デフォルトのまま)
        registerColumnType(Types.VARCHAR, 255, 'text')          // 255はTEXT
        registerColumnType(Types.VARCHAR, 'varchar($l)')        // 256以上はVARCHAR(デフォルトのまま)
    }

    @Override
    Class<?> getNativeIdentifierGeneratorClass() {
        // デフォルトの実装では全テーブル共通で一つのhibernate_sequenceを使用してid採番が行われる。
        // これをテーブル個別にシーケンスが生成されるように独自のジェネレータを指定する。
        TableNameSequenceGenerator
    }

    static class TableNameSequenceGenerator extends SequenceGenerator {
        @Override
        void configure(Type type, Properties params, Dialect dialect) {
            if (!params.getProperty(SEQUENCE)) {
                String tableName = params.getProperty(TABLE)
                if (tableName) {
                    params.setProperty(SEQUENCE, "${tableName}_id_seq")
                }
            }
            super.configure(type, params, dialect)
        }
    }
}


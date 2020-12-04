package de.immomio.config.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomPostgreSqlDialect extends PostgreSQL10Dialect {

    public CustomPostgreSqlDialect() {
        super();
        // registering this functions is only a workaround due to a bug in hibernate 5.x remove after upgrading to 6.x
        this.registerFunction("jsonb_extract_path_textDouble", new StandardSQLFunction("jsonb_extract_path_text", StandardBasicTypes.DOUBLE));
        this.registerFunction("jsonb_extract_path_textInteger", new StandardSQLFunction("jsonb_extract_path_text", StandardBasicTypes.INTEGER));

    }

}

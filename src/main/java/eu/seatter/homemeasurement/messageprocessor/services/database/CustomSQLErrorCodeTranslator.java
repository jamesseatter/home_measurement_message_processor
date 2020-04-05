//package eu.seatter.homemeasurement.messageprocessor.services.database;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
//
//import java.sql.SQLException;
//
///**
// * Created by IntelliJ IDEA.
// * User: jas
// * Date: 06/03/2020
// * Time: 14:04
// */
//public class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {
//    @Override
//    protected DataAccessException customTranslate(final String task, final String sql, final SQLException sqlException) {
//        if (sqlException.getErrorCode() == 23505) {
//            return new DuplicateKeyException("Custome Exception translator - Integrity contraint voilation.", sqlException);
//        }
//        return null;
//    }
//}

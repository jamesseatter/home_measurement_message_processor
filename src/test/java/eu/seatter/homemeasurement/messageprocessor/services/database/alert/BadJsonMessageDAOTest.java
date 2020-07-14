package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 08/04/2020
 * Time: 07:57
 */
@ExtendWith(MockitoExtension.class)
class BadJsonMessageDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private BadJsonMessageDAO badJsonMessageDAO;

    @BeforeEach
    public void initUseCase() {
        badJsonMessageDAO = new BadJsonMessageDAO(jdbcTemplate);
        ReflectionTestUtils.setField(badJsonMessageDAO, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(badJsonMessageDAO, "dbTable", "badjsonmessages");
    }

    @Test
    void whenInsertRecord_thenReturnNumberRowsAffected() {
        //given
        String title = "Test";
        String message = "Test Message";

        final String sql = "INSERT INTO badjsonmessages (title, message) VALUES (?,?)";

        //when

        when(jdbcTemplate.update(sql, title, message)).thenReturn(1);

        //then
        assertEquals(1, badJsonMessageDAO.insertRecord(title, message));
    }
}
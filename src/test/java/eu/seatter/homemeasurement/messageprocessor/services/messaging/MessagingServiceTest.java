//package eu.seatter.homemeasurement.messageprocessor.services.messaging;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.env.Environment;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
///**
// * Created by IntelliJ IDEA.
// * User: jas
// * Date: 03/04/2020
// * Time: 11:07
// */
//@ExtendWith(MockitoExtension.class)
//class MessagingServiceTest {
//    @Mock
//    MessageProcessorMeasurement measurementMessageProcessor;
//    @Mock
//    MessageProcessorMeasurementAlert alertMessageProcessor;
//    @Mock
//    MessageProcessorSystemAlert systemAlertMessageProcessor;
//
//    MessagingService messagingService;
//
//    Environment env;
//
//    @BeforeEach
//    void setUp() {
//        messagingService = new MessagingService("MQ_HOST",1111,"MQ_VHOST","E_TEST","uname","password",measurementMessageProcessor,alertMessageProcessor,systemAlertMessageProcessor, env);
//        ReflectionTestUtils.setField(messagingService, "hostname", "MQ_HOST");
//        ReflectionTestUtils.setField(messagingService, "portnumber", 1111);
//        ReflectionTestUtils.setField(messagingService, "vhost", "vhost");
//        ReflectionTestUtils.setField(messagingService, "exchangename", "Exchange");
//        ReflectionTestUtils.setField(messagingService, "username", "username");
//        ReflectionTestUtils.setField(messagingService, "password", "password");
//    }
//
//    @Test
//    void whenProcessMessage_givenGoodJson_thenReturn1() {
//        //given
//
//
//        //then
//
//
//        //when
//        verify(messagingService, times(1)).run();
//
//    }
//}
package eu.seatter.homemeasurement.messageprocessor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 14/04/2020
 * Time: 07:09
 */
@Service
@Slf4j
public class Security {

    public SSLContext getJKSKey() throws Exception {
        char[] keyPassphrase = "bunnies".toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");

        try {
            Resource keyFile = new ClassPathResource("certificates/client_key.p12");
            ks.load(new FileInputStream(keyFile.getFile()), keyPassphrase);
        } catch (Exception ex){
            log.error("Unable to load Security Key from keystore :" + ex.getMessage());
            return null;
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyPassphrase);

        char[] trustPassphrase = "bunnies".toCharArray();
        KeyStore tks = KeyStore.getInstance("JKS");
        try {
            Resource keyFile = new ClassPathResource("certificates/rabbitstore");
            tks.load(new FileInputStream(keyFile.getFile()), trustPassphrase);
        } catch (Exception ex){
            log.error("Unable to load Security Key from keystore :" + ex.getMessage());
            return null;
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);

        SSLContext c = SSLContext.getInstance("TLSv1.2");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return c;
    }
}

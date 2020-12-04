package de.immomio.schufa.util;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Component;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.naming.ConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Base64;

/**
 * @author Niklas Lindemann
 */
@Component
public class KeyStoreHandler {

    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    private static final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    private static final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";
    private static final String RSA = "RSA";
    private static final String JKS = "JKS";
    private static final String CLIENT_CERT = "client-cert";
    private static final String CLIENT_KEY = "client-key";
    private static final String X_509 = "X509";

    private Certificate loadCertificate(String certificatePem) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X_509);
        final byte[] content = readPemContent(certificatePem);

        return certificateFactory.generateCertificate(new ByteArrayInputStream(content));
    }

    private byte[] readPemContent(String pem) throws IOException {
        final byte[] content;
        try (PemReader pemReader = new PemReader(new StringReader(pem))) {
            final PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();
        }
        return content;
    }

    public KeyStore getKeyStore(
            String unformattedClientCertificate,
            String unformattedPrivateKey,
            String keyPassword,
            String keyStorePassword
    ) throws Exception {
        try {
            Certificate clientCertificate = loadCertificate(unformattedClientCertificate);
            KeyStore keyStore = KeyStore.getInstance(JKS);
            keyStore.load(null, keyStorePassword.toCharArray());
            keyStore.setCertificateEntry(CLIENT_CERT, clientCertificate);
            keyStore.setKeyEntry(CLIENT_KEY,
                    loadPrivateKey(unformattedPrivateKey),
                    keyPassword.toCharArray(),
                    new Certificate[]{clientCertificate}
            );

            return keyStore;
        } catch (GeneralSecurityException | IOException e) {
            throw new ConfigurationException(e.getMessage());
        }
    }

    private PrivateKey loadPrivateKey(String privateKeyPem) throws IOException, GeneralSecurityException {
        return pemLoadPrivateKeyPkcs1OrPkcs8Encoded(privateKeyPem);
    }

    private  PrivateKey pemLoadPrivateKeyPkcs1OrPkcs8Encoded(String privateKeyPem) throws GeneralSecurityException, IOException {
        // PKCS#8 format
        final String pemPrivateStart = BEGIN_PRIVATE_KEY;
        // PKCS#1 format
        final String pemRsaPrivateStart = BEGIN_RSA_PRIVATE_KEY;
        if (isPkcs8Key(privateKeyPem)) { // PKCS#8 format
            return generatePkcs8(privateKeyPem, pemPrivateStart);
        } else if (isPkcs1Key(privateKeyPem)) {  // PKCS#1 format
            return generatePkcs1(privateKeyPem, pemRsaPrivateStart);
        }
        throw new GeneralSecurityException("Not supported format of a private key");
    }

    private PrivateKey generatePkcs8(String privateKeyPem, String pemPrivateStart) throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKeyPem = privateKeyPem
                .replace(pemPrivateStart, "")
                .replace(END_PRIVATE_KEY, "");
        privateKeyPem = privateKeyPem.replaceAll("\\s", "");
        byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);
        KeyFactory factory = KeyFactory.getInstance(RSA);
        return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));
    }

    private PrivateKey generatePkcs1(String privateKeyPem, String pemRsaPrivateStart) throws IOException, GeneralSecurityException {
        privateKeyPem = privateKeyPem
                .replace(pemRsaPrivateStart, "")
                .replace(END_RSA_PRIVATE_KEY, "");
        privateKeyPem = privateKeyPem.replaceAll("\\s", "");
        DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));
        DerValue[] seq = derReader.getSequence(0);
        if (seq.length < 9) {
            throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
        }
        // skip version seq[0];
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2,
                exp1, exp2, crtCoef);
        KeyFactory factory = KeyFactory.getInstance(RSA);

        return factory.generatePrivate(keySpec);
    }

    private boolean isPkcs8Key(String privateKeyPem) {
        return privateKeyPem.contains(BEGIN_PRIVATE_KEY);
    }

    private boolean isPkcs1Key(String privateKeyPem) {
        return privateKeyPem.contains(BEGIN_RSA_PRIVATE_KEY);
    }
}

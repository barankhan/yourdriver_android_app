package live.yourdriver.driver.Services;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    public static Retrofit getApiClient(String baseUrl) {

       HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


//        X509TrustManager trustManager;
//        SSLSocketFactory sslSocketFactory;
//        try {
//            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{trustManager}, null);
//            sslSocketFactory = sslContext.getSocketFactory();
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }

        OkHttpClient client = new OkHttpClient.Builder()
        //        .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
//                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();





        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        Log.e("RetrofitClient", baseUrl);

        return retrofit;

    }



    public static OkHttpClient okClient(){
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


//        X509TrustManager trustManager;
//        SSLSocketFactory sslSocketFactory;
//        try {
//            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{trustManager}, null);
//            sslSocketFactory = sslContext.getSocketFactory();
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }

        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .sslSocketFactory(sslSocketFactory, trustManager)

                .build();
        return client;

    }


//    private static X509TrustManager trustManagerForCertificates(InputStream in)
//            throws GeneralSecurityException {
//        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
//        if (certificates.isEmpty()) {
//            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
//        }
//
//        // Put the certificates a key store.
//        char[] password = "password".toCharArray(); // Any password will work.
//        KeyStore keyStore = newEmptyKeyStore(password);
//        int index = 0;
//        for (Certificate certificate : certificates) {
//            String certificateAlias = Integer.toString(index++);
//            keyStore.setCertificateEntry(certificateAlias, certificate);
//        }
//
//        // Use it to build an X509 trust manager.
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
//                KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, password);
//        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm());
//        trustManagerFactory.init(keyStore);
//        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//            throw new IllegalStateException("Unexpected default trust managers:"
//                    + Arrays.toString(trustManagers));
//        }
//        return (X509TrustManager) trustManagers[0];
//    }
//
//    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
//        try {
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            InputStream in = null; // By convention, 'null' creates an empty key store.
//            keyStore.load(in, password);
//            return keyStore;
//        } catch (IOException e) {
//            throw new AssertionError(e);
//        }
//    }
//
//
//    private static InputStream trustedCertificatesInputStream() {
//        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
//        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
//        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
//        // Typically developers will need to get a PEM file from their organization's TLS administrator.
//        String comodoRsaCertificationAuthority = "-----BEGIN CERTIFICATE-----\n" +
//                "MIIGHDCCBASgAwIBAgIJAL9YjcpQAkTLMA0GCSqGSIb3DQEBCwUAMIGiMQswCQYD\n" +
//                "VQQGEwJQSzEPMA0GA1UECAwGUHVuamFiMQ8wDQYDVQQHDAZNdWx0YW4xGzAZBgNV\n" +
//                "BAoMEllvdXIgRHJpdmVyIE9ubGluZTEMMAoGA1UECwwDQXBwMSAwHgYDVQQDDBdo\n" +
//                "dHRwczovL3lvdXJkcml2ZXIubGl2ZTEkMCIGCSqGSIb3DQEJARYVYWRtaW5AeW91\n" +
//                "cmRyaXZlci5saXZlMB4XDTE5MTIwOTA5NDAxN1oXDTI4MDIyNTA5NDAxN1owgaIx\n" +
//                "CzAJBgNVBAYTAlBLMQ8wDQYDVQQIDAZQdW5qYWIxDzANBgNVBAcMBk11bHRhbjEb\n" +
//                "MBkGA1UECgwSWW91ciBEcml2ZXIgT25saW5lMQwwCgYDVQQLDANBcHAxIDAeBgNV\n" +
//                "BAMMF2h0dHBzOi8veW91cmRyaXZlci5saXZlMSQwIgYJKoZIhvcNAQkBFhVhZG1p\n" +
//                "bkB5b3VyZHJpdmVyLmxpdmUwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoIC\n" +
//                "AQCxe7P0l//MVLatnWc2srPsNjmKoCLg7pM965R73iV9rDoj40EigjUa+Zq5PuUK\n" +
//                "MNSHhmy1hjt3GHN4ZGQcYbvdhrrvK3TpCoIWFB3K5ExCQc5wyQPnoPhqQJdoH34F\n" +
//                "t2TA+swLRj6o2QG8ftsVRegnXFdZUJLGka88/JrsZvlQJ4+/JiiLgmo4UeJCmOSK\n" +
//                "PD/lirLMRJmo0aesgq2jnD54EymRT/TgBdMgWuZ5RWfefICzFKFBzKiWvbtvDYPW\n" +
//                "t/n/CObJ9tURCeqfh4W+7b5iocOX8+UzeNETtoFtuf/ZCaU2k/zdVwqYa5deWSfb\n" +
//                "vtSHZO1AfqgvH7Dc/4NEw9dPJp3udoDtl2NM6S3OJHvsKbgV/YhYE/098ok0It4H\n" +
//                "YDlOz66X0voEnc7ZsgDbSHiLw7VqtsrHERYBbPoqi0rNSO8NOrXwgiaDt/ReqiUX\n" +
//                "FgtSZ25rhAP5Gdevnm3iZn1G5GkMG7AAf7HmJbSx3TbMbzbtU80iYb8jvpmmmO42\n" +
//                "E9G4Kxx7Rg+tnelzApcF+QP7svNr3icp6eTmf5w6uGSrL51TbSDqtX6LQiFGkD9A\n" +
//                "tIk4fICdbX9Ur2GoadaR1d74fCYToHjlP8OPDTSO8nIMKLgyc3WpyENOFKljmOmH\n" +
//                "qsLZfXF/3FOGA+apKESXITF+EBQzQSg3ChlGgNNvW3MrkQIDAQABo1MwUTAdBgNV\n" +
//                "HQ4EFgQUkW18A5L6b1NTlBRjw7ahdOG7KqIwHwYDVR0jBBgwFoAUkW18A5L6b1NT\n" +
//                "lBRjw7ahdOG7KqIwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAgEA\n" +
//                "GEmOKHr5MuOLs9usNQq35YXl2CtkWBPXBv3ftbUOPQnL0trujvaYI7HUv38hWgkv\n" +
//                "j2pjtSNqRIxmXE1ZtOj4f/RxUMoYUSg3AKvG0zRq1WgpKPgqkSi4i3AUI7bXoX7B\n" +
//                "ayGxYS6Dofx+LfQFugB3HVmn+7lpY/wb1Dp3z/wmi0xT+8BZL/qvI4ISS4mAPJ9d\n" +
//                "77JwSrQ40qnuWXkoJwC8ubWPP9bQWxolzW2rFn2yRyz9Po/tOYfpV/hhDNbuumxW\n" +
//                "MHVM3Z1pwTUM1xz8RlFE9uJOGVgMX/iikqZ1EMZ6QwIvXZ7Lcpd0Ov09xeeyIx0x\n" +
//                "Gj/t9/7PdrDTkTUG3qZ/UJNqcD2dKBY5E7bZn5QhLSJBTva9vCC/N2i4+i77K/Cy\n" +
//                "/or+aeEAb5oKR8X86UVgNpVrSYyJ9TN/qboGaTf6Hxx9Gm7IUMynpI76pEE2tHHW\n" +
//                "l//9tZMwKzbHyJOfkMPFlW4x1hTsHmAqh4+M0Jo0P6b+NnoaHe2n7SHERKqcJd1q\n" +
//                "9xeY1E+kI5Gv13oXZQDuHZfeAm+aaul0qxkwE/zTmaEKkQbh0TaEliur63vzdrLb\n" +
//                "mdvImvKLciXAzoTRsLFL964ja52ao5vjJ+fPVj/hlI4KkkGkL9QH4PBgV0Dgn7JW\n" +
//                "X/uaAUlYfUhBD4uet2C88syqtkW0+Vb0MuTO4sO1YSE=\n" +
//                "-----END CERTIFICATE-----\n";
//
//        return new Buffer()
//                .writeUtf8(comodoRsaCertificationAuthority)
//                .inputStream();
//    }
//
//
//
////    KeyStore readKeyStore(Context context) throws Exception{
////        KeyStore ks;
////        ks = KeyStore.getInstance(KeyStore.getDefaultType());
////
////
////
////        // get user password and file input stream
////        char[] password = getPassword();
////
////        java.io.FileInputStream fis = context.getResources().openRawResource(R.raw.your_keystore_filename);;
////        try {
////            fis = new java.io.FileInputStream("keyStoreName");
////            ks.load(fis, password);
////        }catch (Exception e){}
////        finally {
////            if (fis != null) {
////                fis.close();
////            }
////        }
////        return ks;
////    }
}
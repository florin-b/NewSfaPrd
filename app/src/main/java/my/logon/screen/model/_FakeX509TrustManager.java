package my.logon.screen.model;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public  class _FakeX509TrustManager implements
        javax.net.ssl.X509TrustManager {
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};



    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return (true);
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return (true);
    }


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }
}

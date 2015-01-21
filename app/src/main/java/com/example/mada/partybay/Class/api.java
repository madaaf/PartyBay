package com.example.mada.partybay.Class;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mada on 21/01/15.
 */
public class api {

    private InputStream KEYSTORE;
    private String TRUSTSTORE_PASSWORD;
    private String URL;
    private String METHODE;
    private HttpEntity DATA;
    private List<NameValuePair> HEADER = new ArrayList<NameValuePair>();
    private HttpClient httpClient;


}

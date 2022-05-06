package com.keane9301.myapp001;

import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslateApi extends AsyncTask<String, String, String> {
    private OnTranslationCompleteListener listener;
/*
    @Override
    protected String doInBackground(String... strings) {
        String[] strArr = (String[]) strings;
        String str = "";
        Log.d("DEBUG_TAG", "doInBackground: before try");
        try {
            String encode = URLEncoder.encode(strArr[0], "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(strArr[1]);
            sb.append("&tl=");
            sb.append(strArr[2]);
            sb.append("&dt=t&q=");
            sb.append(encode);
            HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(sb.toString()));
            StatusLine statusLine = execute.getStatusLine();
            Log.d("DEBUG_TAG", "doInBackground: before if");
//            Log.d("DEBUG_TAG", "doInBackground: " + execute2.getResponseCode());
            if(statusLine.getStatusCode() == 200) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();;
                Log.d("DEBUG_TAG", "doInBackground: middle of if");
                execute.getEntity().writeTo(byteArrayOutputStream);
                String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                byteArrayOutputStream.close();
                JSONArray jsonArray = new JSONArray(byteArrayOutputStream2).getJSONArray(0);
                String str2 = str;
                Log.d("DEBUG_TAG", "doInBackground: before for loop");
                    Log.d("DEBUG_TAG", "doInBackground: in for loop");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(jsonArray2.get(i).toString());
                    str2 = sb2.toString();
                }
                return str2;
            }
            execute.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        } catch (Exception e) {
            Log.d("DEBUG_TAG", "doInBackground: " + e.getMessage());
            Log.e("translate.api", e.getMessage());
            listener.onError(e);
            return str;
        }
//        return str;
    }

 */



///*
    @Override
    protected String doInBackground(String... strings) {
        String[] strArr = (String[]) strings;
        String str = "";
        Log.d("DEBUG_TAG", "doInBackground: before try");
        try {
            String encode = URLEncoder.encode(strArr[0], "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(strArr[1]);
            sb.append("&tl=");
            sb.append(strArr[2]);
            sb.append("&dt=t&q=");
            sb.append(encode);
            URL url = new URL(sb.toString());
            HttpURLConnection execute2 = (HttpURLConnection) url.openConnection();
            execute2.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            execute2.setRequestMethod("POST");
            execute2.setDoInput(true);
            execute2.connect();
            Log.d("DEBUG_TAG", "doInBackground: before if");
            Log.d("DEBUG_TAG", "doInBackground: " + execute2.getResponseCode());
            if(execute2.getResponseCode() == 200) {
                InputStream is = new BufferedInputStream(execute2.getInputStream());
                String isString = getResponse2(is);
                is.close();
                Log.d("DEBUG_TAG", "doInBackground: middle of if");
                JSONArray json = new JSONArray(isString).getJSONArray(0);
                String str2 = str;
                Log.d("DEBUG_TAG", "doInBackground: before for loop");
                for(int i = 0; i < json.length(); i++) {
                    Log.d("DEBUG_TAG", "doInBackground: in for loop");
                    JSONArray json2 = json.getJSONArray(i);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(json2.get(i).toString());
                    str2 = sb2.toString();
                }
                return str2;
            }
            execute2.disconnect();
        } catch (Exception e) {
            Log.d("DEBUG_TAG", "doInBackground: " + e.getMessage());
            Log.e("translate.api", e.getMessage());
            listener.onError(e);
            return str;
        }
        return str;
    }


 //*/


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStartTranslation();
    }


    @Override
    protected void onPostExecute(String text) {
        listener.onCompleted(text);
    }


    public interface OnTranslationCompleteListener {
        void onStartTranslation();
        void onCompleted(String text);
        void onError(Exception e);
    }


    public void setOnTranslationCompleteListener(OnTranslationCompleteListener listener) {
        this.listener = listener;
    }




    private String getResponse2(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }


/*
    private String getResponse(OutputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        OutputStreamWriter isr = new OutputStreamWriter(is, "UTF-8");
        BufferedWriter reader = new BufferedWriter(isr);

        String line;

        while ((line = reader.toString()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

 */


}








/**
 * Source: https://medium.com/swlh/free-use-google-translate-api-in-android-with-no-limit-70977726d7cf
 *
 * Deprecated Fix: https://stackoverflow.com/questions/32156842/android-statusline-now-deprecated-what-is-the-alternative
 *
 All Language ISO Code list

 Abkhazian --> ab

 Afar --> aa

 Afrikaans --> af

 Akan --> ak

 Albanian --> sq

 Amharic --> am

 Arabic --> ar

 Aragonese --> an

 Armenian --> hy

 Assamese --> as

 Avaric --> av

 Avestan --> ae

 Aymara --> ay

 Azerbaijani --> az

 Bambara --> bm

 Bashkir --> ba

 Basque --> eu

 Belarusian --> be

 Bengali (Bangla) --> bn

 Bihari --> bh

 Bislama --> bi

 Bosnian --> bs

 Breton --> br

 Bulgarian --> bg

 Burmese --> my

 Catalan --> ca

 Chamorro --> ch

 Chechen --> ce

 Chichewa, Chewa, Nyanja --> ny

 Chinese --> zh

 Chinese (Simplified) --> zh-Hans

 Chinese (Traditional) --> zh-Hant

 Chuvash --> cv

 Cornish --> kw

 Corsican --> co

 Cree --> cr

 Croatian --> hr

 Czech --> cs

 Danish --> da

 Divehi, Dhivehi, Maldivian --> dv

 Dutch --> nl

 Dzongkha --> dz

 English --> en

 Esperanto --> eo

 Estonian --> et

 Ewe --> ee

 Faroese --> fo

 Fijian --> fj

 Finnish --> fi

 French --> fr

 Fula, Fulah, Pulaar, Pular --> ff

 Galician --> gl

 Gaelic (Scottish) --> gd

 Gaelic (Manx) --> gv

 Georgian --> ka

 German --> de

 Greek --> el

 Greenlandic --> kl

 Guarani --> gn

 Gujarati --> gu

 Haitian Creole --> ht

 Hausa --> ha

 Hebrew --> he

 Herero --> hz

 Hindi --> hi

 Hiri Motu --> ho

 Hungarian --> hu

 Icelandic --> is

 Ido --> io

 Igbo --> ig

 Indonesian --> id, in

 Interlingua --> ia

 Interlingue --> ie

 Inuktitut --> iu

 Inupiak --> ik

 Irish --> ga

 Italian --> it

 Japanese --> ja

 Javanese --> jv

 Kalaallisut, Greenlandic --> kl

 Kannada --> kn

 Kanuri --> kr

 Kashmiri --> ks

 Kazakh --> kk

 Khmer --> km

 Kikuyu --> ki

 Kinyarwanda (Rwanda) --> rw

 Kirundi --> rn

 Kyrgyz --> ky

 Komi --> kv

 Kongo --> kg

 Korean --> ko

 Kurdish --> ku

 Kwanyama --> kj

 Lao --> lo

 Latin --> la

 Latvian (Lettish) --> lv

 Limburgish ( Limburger) --> li

 Lingala --> ln

 Lithuanian --> lt

 Luga-Katanga --> lu

 Luganda, Ganda --> lg

 Luxembourgish --> lb

 Manx --> gv

 Macedonian --> mk

 Malagasy --> mg

 Malay --> ms

 Malayalam --> ml

 Maltese --> mt

 Maori --> mi

 Marathi --> mr

 Marshallese --> mh

 Moldavian --> mo

 Mongolian --> mn

 Nauru --> na

 Navajo --> nv

 Ndonga --> ng

 Northern Ndebele --> nd

 Nepali --> ne

 Norwegian --> no

 Norwegian bokmål --> nb

 Norwegian nynorsk --> nn

 Nuosu --> ii

 Occitan --> oc

 Ojibwe --> oj

 Old Church Slavonic, Old Bulgarian --> cu

 Oriya --> or

 Oromo (Afaan Oromo) --> om

 Ossetian --> os

 Pāli --> pi

 Pashto, Pushto --> ps

 Persian (Farsi) --> fa

 Polish --> pl

 Portuguese --> pt

 Punjabi (Eastern) --> pa

 Quechua --> qu

 Romansh --> rm

 Romanian --> ro

 Russian --> ru

 Sami --> se

 Samoan --> sm

 Sango --> sg

 Sanskrit --> sa

 Serbian --> sr

 Serbo-Croatian --> sh

 Sesotho --> st

 Setswana --> tn

 Shona --> sn

 Sichuan Yi --> ii

 Sindhi --> sd

 Sinhalese --> si

 Siswati --> ss

 Slovak --> sk

 Slovenian --> sl

 Somali --> so

 Southern Ndebele --> nr

 Spanish --> es

 Sundanese --> su

 Swahili (Kiswahili) --> sw

 Swati --> ss

 Swedish --> sv

 Tagalog --> tl

 Tahitian --> ty

 Tajik --> tg

 Tamil --> ta

 Tatar --> tt

 Telugu --> te

 Thai --> th

 Tibetan --> bo

 Tigrinya --> ti

 Tonga --> to

 Tsonga --> ts

 Turkish --> tr

 Turkmen --> tk

 Twi --> tw

 Uyghur --> ug

 Ukrainian --> uk

 Urdu --> ur

 Uzbek --> uz

 Venda --> ve

 Vietnamese --> vi

 Volapük --> vo

 Wallon --> wa

 Welsh --> cy

 Wolof --> wo

 Western Frisian --> fy

 Xhosa --> xh

 Yiddish --> yi, ji

 Yoruba --> yo

 Zhuang, Chuang --> za

 Zulu --> zu

 **/

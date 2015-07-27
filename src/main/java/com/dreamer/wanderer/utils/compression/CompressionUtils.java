package com.dreamer.wanderer.utils.compression;

import com.dreamer.wanderer.exceptions.SpringTransactionalException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by rjain236 on 26/7/15.
 */

@Service("CompressionUtils")
public class CompressionUtils {

    public byte[] compress(String str){
        if (str == null || str.length() == 0) {
            return new byte[]{};
        }
        System.out.println("String length : " + str.length());
        ByteArrayOutputStream obj=new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(obj);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
        } catch (IOException e) {
            throw new SpringTransactionalException(e.getMessage());
        }
        return obj.toByteArray();
    }

    public String decompress(byte[] data){
        if (data == null || data.length == 0) {
            return "";
        }
        System.out.println("Input String length : " + data.length);
        GZIPInputStream gis;
        String outStr = "";
        try {
            gis = new GZIPInputStream(new ByteArrayInputStream(data));
            BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
            String line;
            while ((line=bf.readLine())!=null) {
                outStr += line;
            }
        } catch (IOException e) {
            throw new SpringTransactionalException(e.getMessage());
        }
        return outStr;
    }
}

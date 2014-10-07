package com.sindrave.caelum.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Yanik on 07/10/2014.
 */
public class Cache {
    private final File cacheDir;

    public Cache(Context context) {
        cacheDir = context.getCacheDir();
    }

    public void save(String name, Object object) throws IOException {
        File cacheFile = new File(cacheDir, name);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(cacheFile));
        byte[] forecastBytes = toBytes(object);

        os.write(forecastBytes);
        os.close();
    }

    private byte[] toBytes(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);

        out.writeObject(object);
        out.close();

        return bos.toByteArray();
    }

    public Object get(String name) throws IOException, ClassNotFoundException {
        File cacheFile = new File(cacheDir, name);
        byte[] fileData = new byte[(int) cacheFile.length()];
        DataInputStream is = new DataInputStream(new FileInputStream(cacheFile));

        is.readFully(fileData);
        is.close();

        return fromBytes(fileData);
    }

    private Object fromBytes(byte[] forecastBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(forecastBytes);
        ObjectInputStream is = new ObjectInputStream(in);

        Object readObject = is.readObject();
        is.close();

        return readObject;
    }
}

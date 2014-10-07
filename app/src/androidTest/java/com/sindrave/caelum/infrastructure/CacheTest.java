package com.sindrave.caelum.infrastructure;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.io.IOException;

/**
 * Created by Yanik on 07/10/2014.
 */
public class CacheTest extends AndroidTestCase {
    private String fileName = "testfilename";
    private Cache cache;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        cache = new Cache(getContext());
    }

    public void testSaveStringReadsCorrectString() throws IOException, ClassNotFoundException {
        String toSave = "stringtosave";
        cache.save(fileName, toSave);
        String readString = (String) cache.get(fileName);
        assertEquals(toSave, readString);
    }

    public void testSaveStringUnknownFileNameThrowsException() {
        try {
            String toSave = "stringtosave";
            cache.save(fileName, toSave);
            cache.get("unknown file name");
            Assert.fail();
        } catch (Exception e) {
            // success
        }
    }
}

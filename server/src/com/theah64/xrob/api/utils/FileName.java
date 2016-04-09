package com.theah64.xrob.api.utils;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import javax.servlet.http.Part;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by theapache64 on 9/4/16.
 */
public class FileName {

    private static final String KEY_CONTENT_DISPOSITION = "content-disposition";
    private static final String CONTENT_TYPE_IMAGE_JPEG = "image/jpeg";
    private static final String FILE_EXTENSION_JPG = ".jpg";
    private static final String FILE_EXTENSION_UNKNOWN = ".unk";
    private static final int FILE_NAME_LENGTH = 10;
    private Matcher fileNameMatcher;

    private static final Pattern FILE_NAME_MATCHER = Pattern.compile("filename=\"(.+\\.(.+))\"");

    public FileName(Part filePart) {
        final String conDisHeader = filePart.getHeader(KEY_CONTENT_DISPOSITION);
        if (conDisHeader != null) {
            fileNameMatcher = FILE_NAME_MATCHER.matcher(conDisHeader);
            if (!fileNameMatcher.find()) {
                fileNameMatcher = null;
            }
        }
    }

    public final String getRealFileName() {
        if (fileNameMatcher != null) {
            return fileNameMatcher.group(1);
        }
        return null;
    }

    private String getFileExtension() {
        if (fileNameMatcher != null) {
            return fileNameMatcher.group(2);
        }
        return null;
    }

    public String getRandomFileName(final String contentType) {
        String fileExtension = getFileExtension();
        if (fileExtension == null) {
            fileExtension = getFileExtension(contentType);
        }
        return RandomString.getRandomFilename(FILE_NAME_LENGTH, fileExtension);
    }


    /**
     * @param contentType MIME Type
     * @return file extension for the given mime type
     */
    private static String getFileExtension(String contentType) {
        switch (contentType) {
            case CONTENT_TYPE_IMAGE_JPEG:
                return FILE_EXTENSION_JPG;
            default:
                return FILE_EXTENSION_UNKNOWN;
        }
    }
}

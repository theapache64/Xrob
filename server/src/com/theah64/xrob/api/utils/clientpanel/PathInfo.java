package com.theah64.xrob.api.utils.clientpanel;

/**
 * Created by theapache64 on 12/9/16.
 */
public class PathInfo {

    public static final int UNLIMITED = -1;
    private final String pathInfo;
    private final int minNumberOfParams, maxNumberOfParams;
    private String[] pathParts;

    public PathInfo(String pathInfo, int minNumberOfParams, int maxNumberOfParams) throws PathInfoException {

        this.pathInfo = pathInfo;
        this.minNumberOfParams = minNumberOfParams;
        this.maxNumberOfParams = maxNumberOfParams;

        if (pathInfo != null) {

            this.pathParts = pathInfo.split("/");
            final int totalNumOfParams = this.pathParts.length - 1; // -first slash
            System.out.println("t" + totalNumOfParams);
            if (maxNumberOfParams != UNLIMITED && totalNumOfParams > maxNumberOfParams) {
                throw new PathInfoException("Maximum number of param is exceeded (" + totalNumOfParams + "/" + maxNumberOfParams + ")");
            } else if (minNumberOfParams != UNLIMITED && totalNumOfParams < minNumberOfParams) {
                throw new PathInfoException("Minimum number of param is not satisfied (" + totalNumOfParams + "/" + maxNumberOfParams + ")");
            }

        } else if (minNumberOfParams > 0) {
            throw new PathInfoException("Path info is null");
        }
    }

    public String getPart(final int index) {
        return getPart(index, null);
    }

    public String getPart(int index, String defValue) {
        if (index < pathParts.length) {
            return pathParts[index];
        }
        return defValue;
    }

    public String getLastPart(String defValue) {
        return getPart(pathParts.length - 1);
    }

    public static final class PathInfoException extends Exception {
        public PathInfoException(String message) {
            super(message);
        }
    }

}

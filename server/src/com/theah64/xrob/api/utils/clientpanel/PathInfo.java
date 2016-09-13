package com.theah64.xrob.api.utils.clientpanel;

/**
 * Created by theapache64 on 12/9/16.
 */
public class PathInfo {

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
            if (totalNumOfParams > maxNumberOfParams) {
                throw new PathInfoException("Maximum number of param is exceeded");
            } else if (totalNumOfParams < maxNumberOfParams) {
                throw new PathInfoException("Minimum number of param is not satisfied");
            }

        } else if (minNumberOfParams > 0) {
            throw new PathInfoException("Path info is null");
        }
    }

    public String getPart(final int index) {
        return this.pathParts[index];
    }

    public static final class PathInfoException extends Exception {
        public PathInfoException(String message) {
            super(message);
        }
    }

}

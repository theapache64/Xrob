package com.theah64.xrob.utils.gpix;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by theapache64 on 21/10/16.
 */

public interface Callback {
    void onResult(@NotNull List<Image> imageList);

    void onError(final String reason);
}

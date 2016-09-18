package com.theah64.xrob.asynctasks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Patterns;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.theah64.xrob.R;
import com.theah64.xrob.commandcenter.commands.BaseCommand;
import com.theah64.xrob.commandcenter.commands.NotificationCommand;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by theapache64 on 14/9/16.
 */
public class NotificationPopper extends AsyncTask<Void, Void, Bitmap> {

    private final Context context;
    private final NotificationCommand notificationCommand;
    private final BaseCommand.Callback callback;

    public NotificationPopper(Context context, NotificationCommand notificationCommand, BaseCommand.Callback callback) {
        this.context = context;
        this.notificationCommand = notificationCommand;
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        //Load image here
        if (notificationCommand.getImageUrl() != null) {
            return ImageLoader.getInstance().loadImageSync(notificationCommand.getImageUrl());

            //REPLACED WITH UIL
            /*

           try {
                final URL imageUrl = new URL(notificationCommand.getImageUrl());
                final HttpURLConnection urlConnection = (HttpURLConnection) imageUrl.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();

                final InputStream is = urlConnection.getInputStream();
                final Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(notificationCommand.getTitle())
                .setAutoCancel(true)
                .setTicker(notificationCommand.getTicker())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_notification);

        final String contentUrl = notificationCommand.getContentUrl();

        if (contentUrl != null && Patterns.WEB_URL.matcher(contentUrl).matches()) {
            final Intent contentIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentUrl));
            final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);
            builder.setContentIntent(pendingIntent);
        }

        if (bitmap != null) {
            builder.setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        } else {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationCommand.getContent()));
        }

        builder.setContentText(notificationCommand.getContent());

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build());

        if (bitmap == null) {
            callback.onSuccess("Notification shown with out image");
        } else {
            callback.onSuccess("Notification shown with image");
        }
    }
}

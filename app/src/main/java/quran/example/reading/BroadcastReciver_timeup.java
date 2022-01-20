package quran.example.reading;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;



import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class BroadcastReciver_timeup extends BroadcastReceiver {
    private static final String CHANNEL_ID = "Zikr Reminder" ;
    private static final String[] zikr = {"سُبْحَانَ الله","اَلۡحَمۡدُ لِلّٰه","اللهُ أَكْبَر","أَسْتَغْفِرُ اللَّه","سُبْحـانَ اللهِ وَبِحَمْـدِه","اللَّهُمَّ صَلِّ وَ سَلِّمْ عَلَى نَبِيِّنَا مُحَمَّد","أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْه"};

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"in Receiver",Toast.LENGTH_LONG).show();
        PendingIntent pi = PendingIntent.getBroadcast(context, 234, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        boolean isTimerOffIntent = intent.getBooleanExtra("ReminderOff",false);
        int reminder_hour = intent.getIntExtra(context.getString(R.string.reminder_setting),2);

        if(isTimerOffIntent || reminder_hour == 0)
        {

            Toast.makeText(context,"Zikr notifications turned off successfully",Toast.LENGTH_SHORT).show();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(5432);
            am.cancel(pi);

            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(context.getString(R.string.reminder_hour),0);
            editor.commit();
        }
        else
        {
            showNotification(context);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (reminder_hour *AlarmManager.INTERVAL_HOUR), pi);
          //  am.set(AlarmManager.RTC_WAKEUP, 10000, pi);
        }
    }

    private void showNotification(Context context) {

        Random r = new Random();
        int random_index = r.nextInt(zikr.length);
        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                  .setSmallIcon(R.drawable.notification)
                .setContentTitle("Let's do some spiritual topup")
                .setContentText("Say "+zikr[random_index]+" few times")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context, BroadcastReciver_timeup.class);
        intent.putExtra("ReminderOff",true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 567, intent, 0);
        builder.addAction(R.drawable.notification_cancel,"Turn Off reminders",
                pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(5432, builder.build());


    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

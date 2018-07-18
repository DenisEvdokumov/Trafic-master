package com.professor.traficinspiration.model.tasks;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.professor.traficinspiration.model.Order;
import com.professor.traficinspiration.utils.TimeHelper;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ReopenTask extends Task {

    private int daysDelay;

    public ReopenTask(int daysDelay) {
        super("Reopen");
        this.daysDelay = daysDelay;
        String daysWord;

        switch (daysDelay%10) {
            case 1:
                daysWord = "день";
                break;
            case 2:
            case 3:
            case 4:
                daysWord = "дня";
                break;
            default:
                daysWord = "дней";
        }

        buttonString = "Открыть приложение повторно";
        description = "Откройте приложение повторно через " + daysDelay + " " + daysWord + " после последнего открытия.";
        titleString = "Открыть приложение повторно";
    }

    @Override
    public boolean executeTask(Activity activity) {
        Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage(order.getPackageName());
        if (launchIntent != null) {
            String timeString = null;

            try {
                timeString = new TimeHelper().execute(this).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Date currentDate;
            // проверка получена ли дата...
            if (timeString == null) {
                currentDate = new Date(System.currentTimeMillis());
            } else {
                currentDate = new Date(Long.parseLong(timeString));
            }

            Date openDate = order.getOpenDate();
            if (openDate.equals(new Date(0))) {
                Toast.makeText(activity, "Для первого открытия используйте кнопку \"Открыть приложение\"", Toast.LENGTH_SHORT).show();
                return false;
            }
            Date neededDate = new Date(openDate.getTime() + daysDelay * 24 * 60 * 60 * 1000);

            boolean completed = currentDate.after(neededDate);
            if (completed) {
                Toast.makeText(activity, "Выполнено", Toast.LENGTH_SHORT).show();
                order.setOpenDate(currentDate);
                // отметить задачу как выполненную
                complete();

            } else {
                Toast.makeText(activity, "Указанное для повторного открытия время еще не пришло", Toast.LENGTH_SHORT).show();
            }
            activity.startActivity(launchIntent);
            return completed;

        } else {
            Toast.makeText(activity, "Приложение не установлено", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}

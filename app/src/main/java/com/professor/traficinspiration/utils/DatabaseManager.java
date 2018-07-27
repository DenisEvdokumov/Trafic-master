package com.professor.traficinspiration.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.professor.traficinspiration.ApplicationContext;
import com.professor.traficinspiration.MyAlertDialogFragment;
import com.professor.traficinspiration.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // подключаемся к БД
    DBHelper dbHelper = new DBHelper(ApplicationContext.getContext());
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    public void writeOrdersToDB() {
        // подключаемся к БД
//        db = dbHelper.getWritableDatabase();

//        db.beginTransaction();
        writeListToDB(ApplicationContext.getActiveOrderList());
        writeListToDB(ApplicationContext.getHistoryOrderList());
//        db.setTransactionSuccessful();
//        db.endTransaction();

        // для ускорения также можно использовать SQLiteStatement

        // закрываем подключение к БД
//        db.close();
//        dbHelper.close();
    }

    public void writeListToDB(List<Order> orderList) {
        for (Order order : orderList) {
            writeOrderToDB(order);
        }
    }

    public void writeOrderToDB(Order order) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("_id", order.getId());
        cv.put("name", order.getName());
        cv.put("package_name", order.getPackageName());
        cv.put("payment", order.getPayment());
        cv.put("finished", String.valueOf(order.isFinished()));
        cv.put("payed", String.valueOf(order.isPayed()));
        cv.put("open_date", order.getOpenDate().getTime());
        cv.put("open_count", order.getOpenCount());
        cv.put("open_interval", order.getOpenInterval());
        cv.put("comment", String.valueOf(order.isComment()));
        cv.put("tasks_status", order.getTasksStatus());
        cv.put("img_url", order.getImageUrl());
        cv.put("key_words", order.getKeywords());


        // вставляем или обновляем запись
        int updateCount = db.update("orders", cv, "_id=?", new String[]{String.valueOf(order.getId())});
        if (updateCount == 0) {
            db.insert("orders", null, cv);
        }
    }


    public List<Order> readOrdersFromDB() {
        List<Order> activeOrderList = new ArrayList<>();
        List<Order> historyOrderList = new ArrayList<>();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor resultCursor = db.query("orders", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (!resultCursor.moveToFirst()) {
//            Toast.makeText(ApplicationContext.getContext(), "no rows", Toast.LENGTH_LONG).show();
            resultCursor.close();
            return activeOrderList;
        }

        // определяем номера столбцов по имени в выборке
        int idColIndex = resultCursor.getColumnIndex("_id");
        int nameColIndex = resultCursor.getColumnIndex("name");
        int packageNameColIndex = resultCursor.getColumnIndex("package_name");
        int paymentColIndex = resultCursor.getColumnIndex("payment");
        int finishedColIndex = resultCursor.getColumnIndex("finished");
        int payedColIndex = resultCursor.getColumnIndex("payed");
        int openDateColIndex = resultCursor.getColumnIndex("open_date");
        int openCountColIndex = resultCursor.getColumnIndex("open_count");
        int openIntervalColIndex = resultCursor.getColumnIndex("open_interval");
        int commentColIndex = resultCursor.getColumnIndex("comment");
        int tasksStatusColIndex = resultCursor.getColumnIndex("tasks_status");
        int imageUrlColIndex = resultCursor.getColumnIndex("img_url");
        int keywordsColIndex = resultCursor.getColumnIndex("key_words");




        do {
            Order order = new Order(
                    resultCursor.getLong(idColIndex),
                    resultCursor.getString(nameColIndex),
                    resultCursor.getString(packageNameColIndex),
                    resultCursor.getDouble(paymentColIndex),
                    resultCursor.getString(finishedColIndex),
                    resultCursor.getString(payedColIndex),
                    resultCursor.getLong(openDateColIndex),
                    resultCursor.getInt(openCountColIndex),
                    resultCursor.getInt(openIntervalColIndex),
                    resultCursor.getString(commentColIndex),
                    resultCursor.getString(tasksStatusColIndex),
                    resultCursor.getString(imageUrlColIndex),
                    resultCursor.getString(keywordsColIndex)
            );

            // разделить на активные и завершенные (оплаченные)
            if (order.isPayed()) {
                historyOrderList.add(order);
            } else {
                activeOrderList.add(order);
            }

            // переход на следующую строку
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
        } while (resultCursor.moveToNext());

//        Toast.makeText(ApplicationContext.getContext(), "local data is read", Toast.LENGTH_LONG).show();

        ApplicationContext.setActiveOrderList(activeOrderList);
        ApplicationContext.setHistoryOrderList(historyOrderList);

        resultCursor.close();

        return activeOrderList;
    }

    public void closeDB() {
        db.close();
        dbHelper.close();
    }
}

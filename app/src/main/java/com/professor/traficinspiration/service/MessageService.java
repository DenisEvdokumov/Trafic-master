package com.professor.traficinspiration.service;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.professor.traficinspiration.ApplicationContext;
import com.professor.traficinspiration.MyAlertDialogFragment;
import com.professor.traficinspiration.model.Order;
import com.professor.traficinspiration.model.User;
import com.professor.traficinspiration.model.WithdrawHistoryEntry;
import com.professor.traficinspiration.model.messages.CompleteOrderRequestMessage;
import com.professor.traficinspiration.model.messages.CompleteOrderResponseMessage;
import com.professor.traficinspiration.model.messages.EncryptionRequestMessage;
import com.professor.traficinspiration.model.messages.EncryptionRequestMessage2;
import com.professor.traficinspiration.model.messages.EncryptionResponseMessage;
import com.professor.traficinspiration.model.messages.EncryptionResponseMessage2;
import com.professor.traficinspiration.model.messages.OrderResponse;
import com.professor.traficinspiration.model.messages.OrdersRequestMessage;
import com.professor.traficinspiration.model.messages.OrdersResponseMessage;
import com.professor.traficinspiration.model.messages.RequestMessage;
import com.professor.traficinspiration.model.messages.ResponseMessage;
import com.professor.traficinspiration.model.messages.SupportRequestMessage;
import com.professor.traficinspiration.model.messages.SupportResponseMessage;
import com.professor.traficinspiration.model.messages.UserRequestMessage;
import com.professor.traficinspiration.model.messages.UserResponseMessage;
import com.professor.traficinspiration.model.messages.WithdrawHistoryResponseMessage;
import com.professor.traficinspiration.model.messages.WithdrawRequestMessage;
import com.professor.traficinspiration.model.messages.WithdrawResponseMessage;
import com.professor.traficinspiration.service.handler.OrdersHandler;
import com.professor.traficinspiration.service.handler.UserHandler;
import com.professor.traficinspiration.utils.FirstStep;
import com.professor.traficinspiration.utils.FirstStep2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.professor.traficinspiration.ApplicationContext.getContext;

public class MessageService {


    static Retrofit retrofit;

    public MessageService() {
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("http://tapmoney.testmy.tk/rest/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();
    }


    public void executeEnterSequence(final String email, final String password, final String action, final Long idReferrer) {

        if(connectToServer()) {


            if (UserHandler.handle(getOrCreateUser(email, password, action, idReferrer))) {
                OrdersHandler.handle(getOrders( false));

                ApplicationContext.notificator.init();
            }
        }

    }

    private Boolean connectToServer() {



        EncryptionResponseMessage encryptionResponseMessage = SendFistEncryptKey();

        EncryptionResponseMessage2 encryptionResponseMessage2 = RequestEncryptKey(encryptionResponseMessage);


        if(chekcMAC_MAC(encryptionResponseMessage2.getKeyMAC_MAC(),encryptionResponseMessage2.getKeyMAC())){

            String KeyMAC_real = FirstStep2.decrypt(encryptionResponseMessage2.getKeyMAC(), ApplicationContext.getKeyAES());
            ApplicationContext.setKeyMAC(KeyMAC_real);
            return true;
        }


        return false;
    }

    private EncryptionResponseMessage2 RequestEncryptKey(EncryptionResponseMessage encryptionResponseMessage) {
        EncryptionRequestMessage2 encryptionRequestMessage2 = FirstStep2.
                genetateEncryptionRequestMessage(encryptionResponseMessage);

        FistConnectToServerAPI fistConnectToServerAPI = retrofit
                .create(FistConnectToServerAPI.class);

        Call<EncryptionResponseMessage2> call2 = fistConnectToServerAPI
                .getFirstKey2(encryptionRequestMessage2);

        Response<EncryptionResponseMessage2> response2 = sendResponse(call2);

        if (!isResponseSuccessful(response2)) {
           return null;
        }
//        Log.i("1","-----------------------------------second succses");
        ApplicationContext.sequensePlus();
        return response2.body();

    }

    private EncryptionResponseMessage SendFistEncryptKey() {

        final EncryptionRequestMessage encryptionRequestMessage = FirstStep.genetateEncryptionRequestMessage();
        FistConnectToServerAPI fistConnectToServerAPI = retrofit.create(FistConnectToServerAPI.class);
        final Call<EncryptionResponseMessage> call = fistConnectToServerAPI.getFirstKey(encryptionRequestMessage);

        Response<EncryptionResponseMessage> response = sendResponse(call);
       // Log.i("1","----------------------------------first succses");
        if (!isResponseSuccessful(response)) {
            return null;
        }
        ApplicationContext.sequensePlus();
        return response.body();
    }

    private Boolean chekcMAC_MAC(String keyMAC_MAC,String keyMAC) {
        String keyMACold = ApplicationContext.getKeyMAC();

        String KeyMAC_from_MAC = FirstStep2.decrypt(keyMAC_MAC, keyMACold);
        //Проверка равен ли KeyMAC к KeyMAC_MACdecoded
        if(!keyMAC.equals(KeyMAC_from_MAC)) {
            return false;
        }
        return true;
    }

    public User getOrCreateUser(final String email, final String password, final String action, final Long idReferrer) {

        // !!! encrypt password...

        UserRequestMessage userRequestMessage = new UserRequestMessage();
        userRequestMessage = (UserRequestMessage) generateRequestMassage(userRequestMessage);

        String emailAES = encryptAES(email);
        userRequestMessage.setEmail(emailAES);
        userRequestMessage.setEmailMAC(encrypt(emailAES));

        String passwordAES = encryptAES(password);
        userRequestMessage.setPassword(passwordAES);
        userRequestMessage.setPasswordMAC(encrypt(passwordAES));

        userRequestMessage.setAction(action);


        UserService userService = retrofit.create(UserService.class);
        Call<UserResponseMessage> call = userService.getOrCreateUser(userRequestMessage);

        Response<UserResponseMessage> response = sendResponse(call);
        UserResponseMessage userResponseMessage = response.body();

        User user = getUserForResponse(userResponseMessage);
//        ApplicationContext.setUser(user);

        if(chekcMAC_MAC(userResponseMessage.getKeyMACMAC(),userResponseMessage.getKeyMAC())){

            String KeyMAC_real = FirstStep2.decrypt(userResponseMessage.getKeyMAC(), ApplicationContext.getKeyAES());
            ApplicationContext.setKeyMAC(KeyMAC_real);

            ApplicationContext.sequensePlus();
           // Log.i("1","-----------------------------------user succses");
            return user;

        }

        return null;
    }

    private User getUserForResponse(UserResponseMessage userResponseMessage) {
        User user = new User();
        user.setId(Long.parseLong(decryptAES(userResponseMessage.getId())));
        user.setBalance(Long.parseLong(decryptAES(userResponseMessage.getBalance())));
        user.setToken(decryptAES(userResponseMessage.getToken()));
        user.setOrdersCompleted(Long.parseLong(decryptAES(userResponseMessage.getOrdersCompleted())));
        user.setReferralsCount(Long.parseLong(decryptAES(userResponseMessage.getReferralsCount())));
        return user;
    }



    public void sendCompleteOrder(final Order order) {

        User user = ApplicationContext.getUser();

        CompleteOrderRequestMessage completeOrderRequestMessage = new CompleteOrderRequestMessage();
        completeOrderRequestMessage = (CompleteOrderRequestMessage)
                generateRequestMassage(completeOrderRequestMessage);


        int rev = order.getReview();
        if(rev>0) rev=1;
        order.setReview(rev);
        String review = String.valueOf(order.getReview());
        completeOrderRequestMessage.setReview(review);
        completeOrderRequestMessage.setReviewMAC(encrypt(review));


        OrderService orderService = retrofit.create(OrderService.class);
        Call<CompleteOrderResponseMessage> call = orderService.completeOrder(completeOrderRequestMessage);


        Response<OrdersResponseMessage> response = sendResponse(call);
        if (!isResponseSuccessful(response)) {

            if (order.isComment()) {
                Toast.makeText(getContext(), "Оплата за выполнение будет перечислена после проверки модератором", Toast.LENGTH_LONG).show();
            } else {
                double payment = order.getPayment();
                user.setBalance(user.getBalance() + payment);
                order.setPayed(true);
            }

            // переместить выполненную задачу в архив
            order.setPayed(true);
            ApplicationContext.getIdToActiveOrderMap().remove(order.getId());
            ApplicationContext.getIdToHistoryOrderMap().put(order.getId(), order);


            ApplicationContext.getDatabaseManager().writeOrderToDB(order);
//        ApplicationContext.sequensePlus();
        }
    }

    public void withdraw(int amount, String withdrawType, String accountNumber, String notice) {
        WithdrawRequestMessage withdrawRequestMessage = new WithdrawRequestMessage();
        MoneyService moneyService = retrofit.create(MoneyService.class);
        Call<WithdrawResponseMessage> call = moneyService.withdraw(withdrawRequestMessage);

        call.enqueue(new Callback<WithdrawResponseMessage>() {
            @Override
            public void onResponse(Call<WithdrawResponseMessage> call, Response<WithdrawResponseMessage> response) {

                if (!isResponseSuccessful(response)) {
                    return;
                }

                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<WithdrawResponseMessage> call, Throwable t) {
                MyAlertDialogFragment.createAndShowErrorDialog("Возникла ошибка в процессе попытки вывода средств");
            }
        });
    }

    public List<WithdrawHistoryEntry> getWithdrawHistory() {

        MoneyService moneyService = retrofit.create(MoneyService.class);

        User user = ApplicationContext.getUser();

        Call<WithdrawHistoryResponseMessage> call = moneyService.getWithdrawHistory(user.getId(), ApplicationContext.getIdSession());

        Response<WithdrawHistoryResponseMessage> response = null;

        RequestExecutor requestExecutor = new RequestExecutor();

        try {
            response = requestExecutor.execute(call).get();

//            Toast.makeText(ApplicationContext.getContext(), "" + response.toString(), Toast.LENGTH_LONG).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        if (!isResponseSuccessful(response)) {
            return null;
        }

        WithdrawHistoryResponseMessage withdrawHistoryResponseMessage = response.body();

        return withdrawHistoryResponseMessage.getWithdrawList();

    }

    public void sendSupportRequest(String message) {
        SupportRequestMessage supportRequestMessage = new SupportRequestMessage();

        SupportService supportService = retrofit.create(SupportService.class);
        Call<SupportResponseMessage> call = supportService.sendSupportRequest(supportRequestMessage);

        call.enqueue(new Callback<SupportResponseMessage>() {
            @Override
            public void onResponse(Call<SupportResponseMessage> call, Response<SupportResponseMessage> response) {

                if (!isResponseSuccessful(response)) {
                    return;
                }

                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<SupportResponseMessage> call, Throwable t) {

            }
        });

    }

    private boolean isResponseSuccessful(Response<? extends ResponseMessage> response) {

        if (response == null || !response.isSuccessful()) {
            if (getContext() != null) {
                Log.i("1","--------------------------------------------------------------------");
                try {
                    Log.i("1",response.errorBody().string().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            MyAlertDialogFragment.createAndShowErrorDialog("Сервер не отвечает. Проверьте соединение с интернетом");
            return false;
        }

        if (response.body().getErrors() != null) {
            if (getContext() != null) {
                Toast.makeText(getContext(), Arrays.deepToString(response.body().getErrors().values().toArray()), Toast.LENGTH_LONG).show();
            }
            return false;
        }

        return true;
    }


    private RequestMessage generateRequestMassage(RequestMessage requestMessage) {
        User user = ApplicationContext.getUser();

//        Log.i("1", "------------------------------------------------------------------");
        String idSession = ApplicationContext.getIdSession();
//        Log.i("1", "idSession " + idSession);
//        Log.i("1","encrypt(idSession) " + encrypt(idSession));

        String userIdAES = encryptAES(String.valueOf(user.getId()));
//        Log.i("1","userIdAES " + userIdAES);
//        Log.i("1","encrypt(userIdAES) " + encrypt(userIdAES));

        String tokenAES = encryptAES(String.valueOf(user.getToken()));
//        Log.i("1", "tokenAES " + tokenAES);
//        Log.i("1", "encrypt(tokenAES) " +encrypt(tokenAES));

        String sequenceAES = encryptAES(ApplicationContext.getSequence());
//        Log.i("1","sequenceAES " + sequenceAES);
//        Log.i("1", "encrypt(sequenceAES) " + encrypt(sequenceAES));



        requestMessage.setIdSession(idSession);
        requestMessage.setIdSessionMAC(encrypt(idSession));

        requestMessage.setId(userIdAES);
        requestMessage.setIdMAC(encrypt(userIdAES));

        requestMessage.setToken(tokenAES);
        requestMessage.setTokenMAC(encrypt(tokenAES));

        requestMessage.setSequence(sequenceAES);
        requestMessage.setSequenceMAC(encrypt(sequenceAES));


        return requestMessage;
    }


    private String decryptAES(String string) {
        String encryptString = FirstStep2.decrypt(string,ApplicationContext.getKeyAES());
        return encryptString;
    }

    private String encryptAES(String string) {
        String encryptString = FirstStep2.encrypt(string,ApplicationContext.getKeyAES());
        return encryptString;

    }

    private String encrypt(String string) {
        String encryptString = FirstStep2.encrypt(string,ApplicationContext.getKeyMAC());
        return encryptString;
    }

    public List<OrderResponse> getOrders(boolean history) {
        return getOrders(ApplicationContext.getUser().getId(), ApplicationContext.getUser().getToken(), history);
    }
    public List<OrderResponse> getOrders(long userId, String sessionToken, boolean history) {
        OrdersRequestMessage ordersRequestMessage = new OrdersRequestMessage();
        ordersRequestMessage = (OrdersRequestMessage) generateRequestMassage(ordersRequestMessage);
        OrderService orderService = retrofit.create(OrderService.class);



        Call<OrdersResponseMessage> call;
        String action;
        if (history) {
            action = "get-list";
            ordersRequestMessage.setAction(action);
            ordersRequestMessage.setActionMAC(encrypt(action));
            call = orderService.getOrdersHistory(ordersRequestMessage);
        } else {
            action = "list-orders";
            ordersRequestMessage.setAction(action);
            ordersRequestMessage.setActionMAC(encrypt(action));
            call = orderService.getOrders(ordersRequestMessage);
        }
//        Log.i("1", "action " + action);
//        Log.i("1", "encrypt(action) " + encrypt(action));
        Response<OrdersResponseMessage> response = sendResponse(call);

        OrdersResponseMessage ordersResponseMessage = response.body();

        if(chekcMAC_MAC(ordersResponseMessage.getKeyMACMAC(),ordersResponseMessage.getKeyMAC())){

            String KeyMAC_real = FirstStep2.decrypt(ordersResponseMessage.getKeyMAC(), ApplicationContext.getKeyAES());
            ApplicationContext.setKeyMAC(KeyMAC_real);

            ApplicationContext.sequensePlus();
            return ordersResponseMessage.getOrders();

        }



        return null;
    }


    static class RequestExecutor extends AsyncTask<Call, Void, Response> {

        @Override
        protected Response doInBackground(Call... calls) {
            try {
                return calls[0].execute();
            } catch (IOException e) {
                MyAlertDialogFragment.createAndShowErrorDialog("Ошибка соединения");
//                e.printStackTrace();
            }
            return null;
        }
    }

    private Response sendResponse(Call call) {
        Response<EncryptionResponseMessage> response = null;

        RequestExecutor requestExecutor = new RequestExecutor();

        try {
            response = requestExecutor.execute(call).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("1", e.toString());
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("1", e.toString());
            return null;
        }


        if (!isResponseSuccessful(response)) {
            try {
                Log.i("1",response.errorBody().string().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return response;
    }

}

package com.professor.traficinspiration.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.professor.traficinspiration.ApplicationContext;
import com.professor.traficinspiration.MyAlertDialogFragment;
import com.professor.traficinspiration.R;
import com.professor.traficinspiration.model.Order;
import com.professor.traficinspiration.model.User;
import com.professor.traficinspiration.utils.NetworkChecker;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationContext.setContext(this);
//        ApplicationContext.notificator.init();

        // проверить не залогинен ли уже пользователь
        User user = ApplicationContext.getUser();
        if (user == null || user.getId() == 0) {

            // востановить активные задачи
            // это происходит до получения данных с сервера
            ApplicationContext.getDatabaseManager().readOrdersFromDB();

            // попытка автоматического входа
            tryAutoSignIn();
        } else {

            // отобразить информацию о пользователе
//            Drawable userPhoto = user.getPhoto();
            ImageView userPhotoView = (ImageView) ApplicationContext.getContext().findViewById(R.id.avatar);

//            if (userPhoto == null) {
            Uri uri = user.getPhotoUrl();
            Picasso.with(ApplicationContext.getContext())
                    .load(uri)
                    .placeholder(R.drawable.default_account_icon)
                    .error(R.drawable.default_account_icon)
                    .into(userPhotoView);

//                user.setPhoto(userPhotoView.getDrawable());
//
//            } else {
//                userPhotoView.setImageDrawable(userPhoto);
//            }

            String balanceString = "Баланс: " + user.getBalance();
            ((TextView) ApplicationContext.getContext().findViewById(R.id.txtName)).setText(user.getName());
            ((TextView) ApplicationContext.getContext().findViewById(R.id.txtMoney)).setText(balanceString);
            ApplicationContext.getContext().findViewById(R.id.accountInfoButton).setVisibility(View.VISIBLE);
        }


        // при текущей реализации следующий код выполняется больше раз чем нужно...

        findViewById(R.id.newButton).setOnClickListener(this);
        findViewById(R.id.activeButton).setOnClickListener(this);
        findViewById(R.id.moneyButton).setOnClickListener(this);

        findViewById(R.id.historyButton).setOnClickListener(this);
        findViewById(R.id.newsButton).setOnClickListener(this);
        findViewById(R.id.referralsButton).setOnClickListener(this);

        findViewById(R.id.networkButton).setOnClickListener(this);
        findViewById(R.id.supportButton).setOnClickListener(this);
        findViewById(R.id.helpButton).setOnClickListener(this);

        findViewById(R.id.accountInfoButton).setOnClickListener(this);



        // test encrypting
//        EncryptingHelperTry encryptingHelper = new EncryptingHelperTry();
//        Toast.makeText(this, "" + Arrays.toString(encryptingHelper.symbols) + " - " + encryptingHelper.symbols.length, Toast.LENGTH_LONG).show();
//        encryptingHelper.generateKey();
//        Toast.makeText(this, "" + Arrays.toString(encryptingHelper.symbols) + " - " + encryptingHelper.symbols.length, Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ApplicationContext.getUser() != null) {
            String balanceString = "Баланс: " + ApplicationContext.getUser().getBalance();
            ((TextView) ApplicationContext.getContext().findViewById(R.id.txtMoney)).setText(balanceString);
        }

    }

    private void tryAutoSignIn() {
        // get google account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("431575198328-53nakug7bt53up5cv77u0o14p4ubc063.apps.googleusercontent.com")
                .requestEmail()
//                .requestIdToken("252203805123-qg4h4omjn5ichi8b951n9k4bsg2i11jd.apps.googleusercontent.com")
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        //... continued in onActivityResult
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newButton:
                Intent toNewOrdersActivity = new Intent(this, NewOrders.class);
                startActivity(toNewOrdersActivity);
                break;

            case R.id.activeButton:
                Intent toActiveOrdersActivity = new Intent(this, ActiveOrders.class);
                startActivity(toActiveOrdersActivity);
                break;

            case R.id.historyButton:
                Intent toOrdersHistoryActivity = new Intent(this, OrdersHistory.class);
                startActivity(toOrdersHistoryActivity);
                break;

            case R.id.moneyButton:
                Intent toMoneyActivity = new Intent(this, PaymentSystemsActivity.class);
                startActivity(toMoneyActivity);
                break;

            case R.id.referralsButton:
                Intent toReferralsActivity = new Intent(this, ReferralsActivity.class);
                startActivity(toReferralsActivity);
                break;

            case R.id.accountInfoButton:
                if (ApplicationContext.getUser().getPhoto() == null) {
                    ApplicationContext.getUser().setPhoto(((ImageView) ApplicationContext.getContext().findViewById(R.id.avatar)).getDrawable());
                }

                Intent toAccountInfoActivity = new Intent(this, UserInfoActivity.class);
                startActivity(toAccountInfoActivity);
                break;

            case R.id.supportButton:
                Intent toSupportActivity = new Intent(this, SupportActivity.class);
                startActivity(toSupportActivity);
                break;

            case R.id.helpButton:
                Intent toHelpActivity = new Intent(this, HelpActivity.class);
                startActivity(toHelpActivity);
                break;

            case R.id.networkButton:
                Intent toNetworkActivity = new Intent(this, NetworkActivity.class);
                startActivity(toNetworkActivity);
                break;

            case R.id.newsButton:
                Intent toNewsActivity = new Intent(this, NewsActivity.class);
                startActivity(toNewsActivity);
                break;

            default:
                Toast.makeText(this, "Данный раздел находится в разработке", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ApplicationContext.getUser() == null) {
            return;
        }

        // попытка отправить сообщение о завершении задач которые сервер не принял до этого или выполнение требует проверки
        // по идее, это будет происходить очень редко
        for (Order order : ApplicationContext.getActiveOrderList()) {
            if (order.isFinished() && !order.isPayed()) {
               ApplicationContext.getMessageService().sendCompleteOrder(order);
            }
        }

        // сохранение информации о состоянии заказов в локальную БД
        ApplicationContext.getDatabaseManager().writeListToDB(ApplicationContext.getActiveOrderList());
        ApplicationContext.getDatabaseManager().closeDB();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Не удалось подключиться к Google api", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully
            final GoogleSignInAccount googleAccount = result.getSignInAccount();
            String email = googleAccount.getEmail();

            // create user and put in ApplicationContext
            User user = new User();
            user.setName(googleAccount.getDisplayName());
            user.setEmail(email);
            user.setPhotoUrl(googleAccount.getPhotoUrl());

            ApplicationContext.setUser(user);

            // get sharedPreferences
            SharedPreferences sPref = getSharedPreferences(email, MODE_PRIVATE);

            // get password for current account
            String accountPassword = sPref.getString("password", "");

            if (accountPassword.equals("")) {
                // account not exists or application storage was deleted
                // show register/sign in form

                Intent toSignInActivity = new Intent(this, SignInActivity.class);
                this.startActivity(toSignInActivity);
//                executeLoginOrRegisterProcess();
            } else {
                // send message to server for login
                loginExistingUser(email, accountPassword);
            }

        } else {
            // Signed out
            MyAlertDialogFragment.createAndShowErrorDialog(String.valueOf("Please check you INTERNET connection"));
        }
    }

    private void loginExistingUser(String email, String password) {
        // it is possible to retrieve id for existing user

        ApplicationContext.getMessageService().executeEnterSequence(email, password, "authorization", 0L);
        // continued in MessageService onResponse

    }

    @Override
    public void startActivity(Intent intent) {
        if (NetworkChecker.isOnline()) {
            super.startActivity(intent);
        } else {
            Toast.makeText(this, "Нет соединения с интернетом", Toast.LENGTH_SHORT).show();
        }
    }
}

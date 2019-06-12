package ru.gb.gba3l6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String EXT_TIME = "ext_time";
    private final static String EXT_COUNT = "ext_count";

    private TextView mInfoTextView;
    private ProgressBar progressBar;

    List<Model> modelList = new ArrayList<>();
    Realm realm;

    SQLiteUserDataSource sqLiteUserDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteUserDataSource = new SQLiteUserDataSource(this);
        initGUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    private void initGUI() {
        mInfoTextView = findViewById(R.id.tvLoad);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.btnLoad).setOnClickListener(this);
        findViewById(R.id.btnSaveAllSugar).setOnClickListener(this);
        findViewById(R.id.btnSelectAllSugar).setOnClickListener(this);
        findViewById(R.id.btnDeleteAllSugar).setOnClickListener(this);
        findViewById(R.id.btnSaveAllRealm).setOnClickListener(this);
        findViewById(R.id.btnSelectAllRealm).setOnClickListener(this);
        findViewById(R.id.btnDeleteAllRealm).setOnClickListener(this);
        findViewById(R.id.btnSaveAllRoom).setOnClickListener(this);
        findViewById(R.id.btnSelectAllRoom).setOnClickListener(this);
        findViewById(R.id.btnDeleteAllRoom).setOnClickListener(this);
        findViewById(R.id.btnSaveAllSQLite).setOnClickListener(this);
        findViewById(R.id.btnSelectAllSQLite).setOnClickListener(this);
        findViewById(R.id.btnDeleteAllSQLite).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoad:
                loadData(v);
                break;
            case R.id.btnSaveAllSugar:
                execute(this::saveSugar);
                break;
            case R.id.btnSelectAllSugar:
                execute(this::getAllSugar);
                break;
            case R.id.btnDeleteAllSugar:
                execute(this::deleteAllSugar);
                break;
            case R.id.btnSaveAllRealm:
                execute(this::saveRealm);
                break;
            case R.id.btnSelectAllRealm:
                execute(this::getAllRealm);
                break;
            case R.id.btnDeleteAllRealm:
                execute(this::deleteAllRealm);
                break;
            case R.id.btnSaveAllRoom:
                execute(this::saveAllRoom);
                break;
            case R.id.btnSelectAllRoom:
                execute(this::getAllRoom);
                break;
            case R.id.btnDeleteAllRoom:
                execute(this::deleteAllRoom);
                break;
            case R.id.btnSaveAllSQLite:
                execute(this::saveSQLite);
                break;
            case R.id.btnSelectAllSQLite:
                execute(this::getAllSQLite);
                break;
            case R.id.btnDeleteAllSQLite:
                execute(this::deleteAllSQLite);
                break;
        }
    }

    private DisposableSingleObserver<Bundle> createObserver() {
        return new DisposableSingleObserver<Bundle>() {

            @Override
            protected void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
                mInfoTextView.setText("");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(@NonNull Bundle bundle) {
                progressBar.setVisibility(View.GONE);
                mInfoTextView.setText("количество = " + bundle.getInt(EXT_COUNT) +
                        "\n милисекунд = " + bundle.getLong(EXT_TIME));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(@NonNull Throwable e) {
                progressBar.setVisibility(View.GONE);
                mInfoTextView.setText("ошибка БД: " + e.getMessage());
            }
        };
    }

    public void loadData(View v) {
        mInfoTextView.setText("");
        // Проверка на наличие интернета
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            //Конфигурация retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Call<List<Model>> call = retrofit.create(Endpoints.class).loadUsers();
            downLoadOneUrl(call);
        } else {
            Snackbar.make(v, "Подключите интернет", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void downLoadOneUrl(Call<List<Model>> call) {
        call.enqueue(new Callback<List<Model>>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<Model>> call, @NonNull Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Model curModel;
                        // вывод на экран лога
                        mInfoTextView.append("\n Size = " + response.body().size() + "\n--------");
                        //Прохождение по пришедшему списку
                        for (int i = 0; i < response.body().size(); i++) {
                            curModel = response.body().get(i);
                            modelList.add(curModel);
                            mInfoTextView.append(
                                    "\nLogin = " + curModel.getLogin() +
                                            "\nId = " + curModel.getUserId() +
                                            "\nUri = " + curModel.getAvatar() +
                                            "\n--------");
                        }
                    }
                } else {
                    System.out.println("onResponse error: " + response.code());
                    mInfoTextView.setText("onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void execute(Callable<Bundle> call) {
        Single<Bundle> singleDeleteAll = Single.create((SingleOnSubscribe<Bundle>) emitter -> {
            try {
                emitter.onSuccess(call.call());
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        singleDeleteAll.subscribeWith(createObserver());
    }

    private Bundle saveRealm() {
        realm = Realm.getDefaultInstance();
        Date first = new Date();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm1) {
                for (Model curItem : modelList) {
                    try {
                        RealmModel realmModel = realm1.createObject(RealmModel.class);
                        realmModel.setLogin(curItem.getLogin());
                        realmModel.setUserId(curItem.getUserId());
                        realmModel.setAvatarUrl(curItem.getAvatar());
                    } catch (Exception e) {
                        realm1.cancelTransaction();
                        realm1.close();
                        throw e;
                    }
                }
            }
        });
        Date second = new Date();
        RealmResults count = realm.where(RealmModel.class).findAll();
        Bundle bundle = new Bundle();
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        bundle.putInt(EXT_COUNT, count.size());
        realm.close();
        return bundle;
    }

    private Bundle saveRealmAssync() {
        realm = Realm.getDefaultInstance();
        Date first = new Date();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm1) {
                for (Model curItem : modelList) {
                    try {
                        RealmModel realmModel = realm1.createObject(RealmModel.class);
                        realmModel.setLogin(curItem.getLogin());
                        realmModel.setUserId(curItem.getUserId());
                        realmModel.setAvatarUrl(curItem.getAvatar());
                    } catch (Exception e) {
                        realm1.cancelTransaction();
                        realm1.close();
                        throw e;
                    }
                }
            }
        });
        Date second = new Date();
        RealmResults count = realm.where(RealmModel.class).findAll();
        Bundle bundle = new Bundle();
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        bundle.putInt(EXT_COUNT, count.size());
        realm.close();
        return bundle;
    }

    private Bundle getAllRealm() {
        realm = Realm.getDefaultInstance();
        Date first = new Date();
        RealmResults<RealmModel> tempList = realm.where(RealmModel.class).findAll();
        Date second = new Date();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle deleteAllRealm() {
        realm = Realm.getDefaultInstance();
        final RealmResults<RealmModel> tempList = realm.where(RealmModel.class).findAll();
        int size = tempList.size();
        Date first = new Date();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                tempList.deleteAllFromRealm();
            }
        });
        Date second = new Date();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, size);
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle saveSugar() {
        Date first = new Date();
        for (Model curItem : modelList) {
            new SugarModel(
                    curItem.getLogin(),
                    curItem.getUserId(),
                    curItem.getAvatar()
            ).save();
        }
        Date second = new Date();
        List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle getAllSugar() {
        Date first = new Date();
        List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
        Date second = new Date();

        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle deleteAllSugar() {
        Date first = new Date();
        int count = SugarModel.deleteAll(SugarModel.class);
        Date second = new Date();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, count);
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle saveAllRoom() {
        UserDao userDao = MainApp.getDb().userDao();
        Date first = new Date();
        for (Model curItem : modelList) {
            userDao.insert(new Users(
                    curItem.getLogin(),
                    curItem.getUserId(),
                    curItem.getAvatar()
            ));
        }
        Date second = new Date();
        List<Users> tempList = userDao.getAll();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle getAllRoom() {
        UserDao userDao = MainApp.getDb().userDao();
        Date first = new Date();
        List<Users> tempList = userDao.getAll();
        Date second = new Date();

        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle deleteAllRoom() {
        UserDao userDao = MainApp.getDb().userDao();
        Date first = new Date();
        int size = userDao.deleteAll();
        Date second = new Date();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, size);
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }


    private Bundle saveSQLite() {
        Date first = new Date();
        sqLiteUserDataSource.open();
        for (Model curItem : modelList) {
            sqLiteUserDataSource.addUser(curItem);
        }
        Date second = new Date();
        List<Model> tempList = sqLiteUserDataSource.getAllUsers();
        sqLiteUserDataSource.close();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle getAllSQLite() {
        Date first = new Date();
        sqLiteUserDataSource.open();
        List<Model> tempList = sqLiteUserDataSource.getAllUsers();
        sqLiteUserDataSource.close();
        Date second = new Date();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, tempList.size());
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;
    }

    private Bundle deleteAllSQLite() {
        Date first = new Date();
        sqLiteUserDataSource.open();
        int count = sqLiteUserDataSource.deleteAll();
        sqLiteUserDataSource.close();
        Date second = new Date();
        Bundle bundle = new Bundle();
        bundle.putInt(EXT_COUNT, count);
        bundle.putLong(EXT_TIME, second.getTime() - first.getTime());
        return bundle;

    }

}

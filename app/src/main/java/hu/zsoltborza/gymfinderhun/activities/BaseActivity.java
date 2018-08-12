package hu.zsoltborza.gymfinderhun.activities;


import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Zsolt Borza on 2018.01.11..
 */

public class BaseActivity extends AppCompatActivity {

    // dialog feldobása és eltüntetése
    private ProgressDialog progressDialog;

    public void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }



}
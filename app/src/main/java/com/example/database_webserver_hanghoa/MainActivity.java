package com.example.database_webserver_hanghoa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database_webserver_hanghoa.adapters.HangHoaAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*****
 Simple database webservice with android
 Create 11/02/2020
 By ff3105.github.io

 Structure database:
 android_hanghoa > hanghoa > id(int) ,ten_sp(String) , gia_sp(int) , anh_sp(String) , mota_sp(String)
 *****/
public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMain;
    private ArrayList<HangHoa> hangHoas = new ArrayList<>();
    private HangHoaAdapter hangHoaAdapter;
    private String messageToast;

    private String URL_DISPLAY_SERVICE = "http://192.168.1.7/Database_HangHoa/display.php";
    private String URL_CREATE_SERVICE = "http://192.168.1.7/Database_HangHoa/create.php";
    private String URL_EDIT_SERVICE = "http://192.168.1.7/Database_HangHoa/edit.php";
    private String URL_DELETE_SERVICE = "http://192.168.1.7/Database_HangHoa/delete.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvMain = findViewById(R.id.rvMain);

        new loadHangHoa().execute();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(layoutManager);
        hangHoaAdapter = new HangHoaAdapter(getApplicationContext(), hangHoas);
        rvMain.setAdapter(hangHoaAdapter);

        hangHoaAdapter.setOnItemClickListener(new HangHoaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showAlertDialog(position);
            }
        });
    }

    private void showAlertDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(hangHoas.get(position).getTenSP() + "");
        builder.setMessage("??? chọn cc");
        builder.setCancelable(true);
        builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new delHangHoa(hangHoas.get(position).getIdSP()).execute();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditHangHoa(position);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void EditHangHoa(final int position) {
        Dialog dialog = new Dialog(MainActivity.this, R.style.DiaLog);
        dialog.setContentView(R.layout.dialog_sanpham_suahang);
        dialog.show();
        /*--------------------------------------------------------------------------------------*/
        final EditText etDialogEditTenSP = dialog.findViewById(R.id.etDialogEditTenSP);
        final EditText etDialogEditGiaSP = dialog.findViewById(R.id.etDialogEditGiaSP);
        final EditText etDialogEditMoTaSP = dialog.findViewById(R.id.etDialogEditMoTaSP);
        Button btnDialogEditClick = dialog.findViewById(R.id.btnDialogEditClick);

        etDialogEditTenSP.setText(hangHoas.get(position).getTenSP() + "");
        etDialogEditGiaSP.setText(hangHoas.get(position).getGiaSP() + "");
        etDialogEditMoTaSP.setText(hangHoas.get(position).getMotaSP() + "");
        btnDialogEditClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDialogEditTenSP.getText().toString().isEmpty() || etDialogEditGiaSP.getText().toString().isEmpty()) {
                    new editHangHoa(hangHoas.get(position).getIdSP(), etDialogEditTenSP.getText().toString() + "",
                            Integer.parseInt(etDialogEditGiaSP.getText().toString()), etDialogEditMoTaSP.getText().toString() + "").execute();
                }
            }
        });
    }

    public void AddHangHoa(View view) {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DiaLog);
        dialog.setContentView(R.layout.dialog_sanpham_themhang);
        dialog.show();
        /*--------------------------------------------------------------------------------------*/
        Button btnAddDialogClick = dialog.findViewById(R.id.btnDialogAddClick);
        final EditText etDialogTenSP = dialog.findViewById(R.id.etDialogTenSP);
        final EditText etDialogGiaSP = dialog.findViewById(R.id.etDialogGiaSP);
        final EditText etDialogMoTaSP = dialog.findViewById(R.id.etDialogMoTaSP);
        final EditText etDialogAnh = dialog.findViewById(R.id.etDialogAnh);
        btnAddDialogClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDialogTenSP.getText().toString().isEmpty() || !etDialogGiaSP.getText().toString().isEmpty()) {
                    new addHangHoa(etDialogTenSP.getText() + "", etDialogGiaSP.getText() + "",
                            etDialogAnh.getText() + "", etDialogMoTaSP.getText() + "").execute();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Input trống!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class loadHangHoa extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            ConnectionService connectionService = new ConnectionService();
            String json = connectionService.callService(URL_DISPLAY_SERVICE, ConnectionService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray hanghoa = jsonObj.getJSONArray("hanghoa");
                        hangHoas.clear();
                        for (int i = 0; i < hanghoa.length(); i++) {
                            JSONObject obj = (JSONObject) hanghoa.get(i);
                            HangHoa hangHoa = new HangHoa(obj.getInt("id"), obj.getString("ten_sp"),
                                    obj.getInt("gia_sp"), obj.getString("anh_sp"), obj.getString("mota_sp"));
                            hangHoas.add(hangHoa);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Error load...", "Didn't receive any data from server!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            hangHoaAdapter.notifyDataSetChanged();
        }
    }

    class addHangHoa extends AsyncTask {
        String tenSP;
        String giaSP;
        String anhSP;
        String motaSP;

        public addHangHoa(String tenSP, String giaSP, String anhSP, String motaSP) {
            this.tenSP = tenSP;
            this.giaSP = giaSP;
            this.anhSP = anhSP;
            this.motaSP = motaSP;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ConnectionService connectionService = new ConnectionService();
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            //args.add(new BasicNameValuePair("id", null));
            args.add(new BasicNameValuePair("ten_sp", tenSP));
            args.add(new BasicNameValuePair("gia_sp", giaSP));
            args.add(new BasicNameValuePair("anh_sp", anhSP));
            args.add(new BasicNameValuePair("mota_sp", motaSP));
            String json = connectionService.callService(URL_CREATE_SERVICE, ConnectionService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String message = jsonObject.getString("message");
                    if (!message.isEmpty()) {
                        messageToast = message;
                        //Log.i("addHangHoa message: ", message);
                    }
                } catch (JSONException e) {
                    Log.e("Error create...: ", e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            new loadHangHoa().execute();
            Toast.makeText(MainActivity.this, "" + messageToast, Toast.LENGTH_SHORT).show();
        }
    }

    class editHangHoa extends AsyncTask {
        int id;
        String tenSP;
        int giaSP;
        String motaSP;

        public editHangHoa(int id, String tenSP, int giaSP, String motaSP) {
            this.id = id;
            this.tenSP = tenSP;
            this.giaSP = giaSP;
            this.motaSP = motaSP;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ConnectionService connectionService = new ConnectionService();
            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("ten_sp", tenSP));
            args.add(new BasicNameValuePair("gia_sp", giaSP + ""));
            args.add(new BasicNameValuePair("mota_sp", motaSP));
            args.add(new BasicNameValuePair("id", id + ""));
            String json = connectionService.callService(URL_EDIT_SERVICE, ConnectionService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String message = jsonObject.getString("message");
                    Log.i("fidai", "doInBackground: " + message);
                    if (!message.isEmpty()) {
                        messageToast = message;
                    }
                } catch (JSONException e) {
                    Log.e("Error create...", e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            new loadHangHoa().execute();
            Toast.makeText(MainActivity.this, "" + messageToast, Toast.LENGTH_SHORT).show();
        }
    }

    class delHangHoa extends AsyncTask {
        int idSP;

        public delHangHoa(int idSP) {
            this.idSP = idSP;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ConnectionService connectionService = new ConnectionService();
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("id_SP", idSP + ""));
            String json = connectionService.callService(URL_DELETE_SERVICE, ConnectionService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String message = jsonObject.getString("message");
                    if (!message.isEmpty()) {
                        messageToast = message;
                    }
                } catch (JSONException e) {
                    Log.d("Error del: ", e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            new loadHangHoa().execute();
            Toast.makeText(MainActivity.this, "" + messageToast, Toast.LENGTH_SHORT).show();
        }
    }
}

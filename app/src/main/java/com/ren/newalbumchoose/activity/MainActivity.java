package com.ren.newalbumchoose.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private RadioGroup choice_mode, show_mode;
    private Button picker_btn;
    private int SHOW_MODE;//是否显示视频
    private boolean isSingChoose = false;//是否单选，默认为false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermission(MainActivity.this, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
        choice_mode = (RadioGroup) findViewById(R.id.choice_mode);
        show_mode = (RadioGroup) findViewById(R.id.show_mode);
        picker_btn = (Button) findViewById(R.id.picker_btn);


        picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show_mode.getCheckedRadioButtonId() == R.id.showAll) {
                    SHOW_MODE = 0;
                } else if (show_mode.getCheckedRadioButtonId() == R.id.showPhoto) {
                    SHOW_MODE = 1;
                } else if (show_mode.getCheckedRadioButtonId() == R.id.showVideo) {
                    SHOW_MODE = 2;
                }
                if (choice_mode.getCheckedRadioButtonId() == R.id.singleChoose) {//单选
                    isSingChoose = true;
                } else {
                    isSingChoose = false;
                }

                Intent intent = new Intent(MainActivity.this, ChooseImageActivity.class);
                intent.putExtra("showVideo", SHOW_MODE);
                intent.putExtra("singleChoose", isSingChoose);
                startActivity(intent);
            }
        });
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

}

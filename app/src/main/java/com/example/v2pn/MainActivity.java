package com.example.v2pn;

public class MainActivity extends android.app.Activity {
    android.widget.Button button_version, button_global, button_connect;
    int button_global_id, button_connect_id, button3_id, button4_id;
    android.widget.TextView text1;
    android.widget.EditText editText;

    @Override
    public void onDestroy() {
        android.util.Log.d("v2pn", this.getClass().getSimpleName() + ": onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        android.graphics.drawable.GradientDrawable gray_drawable = new android.graphics.drawable.GradientDrawable();
        gray_drawable.setCornerRadius(80);
        gray_drawable.setColor(android.graphics.Color.LTGRAY);

        android.graphics.drawable.GradientDrawable green_drawable = new android.graphics.drawable.GradientDrawable();
        green_drawable.setCornerRadius(80);
        green_drawable.setColor(android.graphics.Color.GREEN);

        if ("true".equals(Util.variableRead(this, "running.txt"))
                && new java.io.File("/proc/" + Util.variableRead(this, "v2pnPid.txt")).exists()) {
            android.util.Log.d("v2pn", "onStart() running is true");
            button_connect.setText("Disconnect");
            button_connect.setBackground(green_drawable);
        } else if ("false".equals(Util.variableRead(this, "running.txt"))) {
            android.util.Log.d("v2pn", "onStart() running is false");
            button_connect.setBackground(gray_drawable);
            button_connect.setText("Connect");
        } else {
            android.util.Log.d("v2pn", "onStart() read running error");
            button_connect.setBackground(gray_drawable);
            button_connect.setText("Connect");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        android.graphics.drawable.GradientDrawable gray_drawable = new android.graphics.drawable.GradientDrawable();
        gray_drawable.setCornerRadius(80);
        gray_drawable.setColor(android.graphics.Color.LTGRAY);

        android.graphics.drawable.GradientDrawable green_drawable = new android.graphics.drawable.GradientDrawable();
        green_drawable.setCornerRadius(80);
        green_drawable.setColor(android.graphics.Color.GREEN);

        if ("true".equals(Util.variableRead(this, "running.txt"))
                && new java.io.File("/proc/" + Util.variableRead(this, "v2pnPid.txt")).exists()) {
            android.util.Log.d("v2pn", "onResume() running is true");
            button_connect.setText("Disconnect");
            button_connect.setBackground(green_drawable);
        } else if ("false".equals(Util.variableRead(this, "running.txt"))) {
            android.util.Log.d("v2pn", "onResume() running is false");
            button_connect.setBackground(gray_drawable);
            button_connect.setText("Connect");
        } else {
            android.util.Log.d("v2pn", "onResume() read running error");
            button_connect.setBackground(gray_drawable);
            button_connect.setText("Connect");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(android.graphics.Color.DKGRAY);
        getActionBar()
                .setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.DKGRAY));
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setGravity(android.view.Gravity.NO_GRAVITY);

        android.graphics.drawable.GradientDrawable gray_drawable = new android.graphics.drawable.GradientDrawable();
        gray_drawable.setCornerRadius(80);
        gray_drawable.setColor(android.graphics.Color.LTGRAY);

        android.graphics.drawable.GradientDrawable green_drawable = new android.graphics.drawable.GradientDrawable();
        green_drawable.setCornerRadius(80);
        green_drawable.setColor(android.graphics.Color.GREEN);

        android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

        button_version = new android.widget.Button(this);
        // change version here, because change version in build.gradle is very
        // complicated.
        button_version.setText("version 2022-07-27");
        button_version.setBackground(gray_drawable);
        layoutParams.leftMargin = 500;
        layoutParams.topMargin = 100;
        layoutParams.width = 500;
        layoutParams.height = 200;
        button_version.setLayoutParams(layoutParams);
        layout.addView(button_version);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new MyHttpd(getApplicationContext()).start(5000, false);
                    android.util.Log.d("v2pn", "http server started");
                } catch (Exception ignored) {
                }
            }
        }).start();

        button_connect = new android.widget.Button(this);
        button_connect.setText("Connect");
        button_connect_id = android.view.View.generateViewId();
        button_connect.setId(button_connect_id);
        button_connect.setBackground(gray_drawable);
        button_connect.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                if (button_connect.getText().equals("Connect")) {
                    android.util.Log.d("v2pn", "Connect");
                    android.content.Intent intent =
                    android.net.VpnService.prepare(getApplicationContext());
                    if (intent != null) {
                    android.util.Log.d("v2pn", "intent != null");
                    startActivityForResult(intent, 0);
                    } else {
                    android.util.Log.d("v2pn", "intent == null");
                    onActivityResult(0, RESULT_OK, null);
                    }
                    android.util.Log.d("v2pn", "running is true");
                    button_connect.setText("Disconnect");
                    button_connect.setBackground(green_drawable);
                } else if (button_connect.getText().equals("Disconnect")) {
                    android.util.Log.d("v2pn", "Disconnect");
                    Util.killAll(getApplicationContext());
                    button_connect.setBackground(gray_drawable);
                    button_connect.setText("Connect");
                } else {
                    android.util.Log.d("v2pn", "connect button error");
                }
            }
        });

        layoutParams.leftMargin = 500;
        layoutParams.topMargin = 100;
        layoutParams.width = 500;
        layoutParams.height = 200;
        button_connect.setLayoutParams(layoutParams);
        layout.addView(button_connect);
        setContentView(layout);
    }

    @Override
    protected void onActivityResult(int request, int result, android.content.Intent data) {
        android.util.Log.d("v2pn", "onActivityResult(): result: " + result);
        if (result == RESULT_OK) {
            android.content.Intent intent = new android.content.Intent(this, V2pnService.class);
            startService(intent);
        }

    }
}

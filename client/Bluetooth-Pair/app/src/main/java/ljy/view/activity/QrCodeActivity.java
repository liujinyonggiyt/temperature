package ljy.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.QrCodeGenerator;

import butterknife.BindView;
import ljy.base.activity.BaseActivity;
import ljy.bluetooth.R;
import ljy.utils.Constant;
import ljy.utils.ToastUtil;

public class QrCodeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_qrcode)
    Button btnQrCode; // 扫码
    @BindView(R.id.txt_result)
    TextView tvResult; // 结果
    @BindView(R.id.btn_generate)
    Button btnGenerate; // 生成二维码
    @BindView(R.id.et_content)
    EditText etContent; // 待生成内容
    @BindView(R.id.img_qrcode)
    ImageView imgQrcode; // 二维码图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    private void initView() {
        btnQrCode.setOnClickListener(this);
        btnGenerate.setOnClickListener(this);
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CAMERA)) {
                ToastUtil.shortShow("请至权限中心打开本应用的相机访问权限");
            }
            ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                ToastUtil.shortShow("请至权限中心打开本应用的文件读写权限");
            }
            ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(QrCodeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    /**
     * 生成二维码
     */
    private void generateQrCode() {
        if (etContent.getText().toString().equals("")) {
            ToastUtil.shortShow("请输入二维码内容");
            return;
        }
        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(etContent.getText().toString(), imgQrcode.getWidth(), imgQrcode.getHeight());
        if (bitmap == null) {
            ToastUtil.shortShow("生成二维码出错");
            imgQrcode.setImageBitmap(null);
        } else {
            imgQrcode.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qrcode:
                startQrCode();
                break;
            case R.id.btn_generate:
                generateQrCode();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            tvResult.setText(scanResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    ToastUtil.shortShow("请至权限中心打开本应用的相机访问权限");
                }
                break;
            case Constant.REQ_PERM_EXTERNAL_STORAGE:
                // 文件读写权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    ToastUtil.shortShow("请至权限中心打开本应用的文件读写权限");
                }
                break;
        }
    }

}

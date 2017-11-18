package com.gggd.sunny.testsystem.tools;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.gggd.sunny.testsystem.R;

/**
 * 该自定义Dialog应用在：弹出框居中显示图片，点击其他区域自动关闭Dialog
 *
 */
public class QRcodeDialog extends Dialog {

    public QRcodeDialog(Context context) {
        super(context);
    }

    public QRcodeDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;

        public Builder(Context context) {
            this.context = context;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public QRcodeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final QRcodeDialog dialog = new QRcodeDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_share_qrcode, null);
            dialog.addContentView(layout, new LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            ImageView img = (ImageView)layout.findViewById(R.id.img_qrcode);
            img.setImageBitmap(getImage());
            return dialog;
        }
    }
}

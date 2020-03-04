package com.mhimine.jdk.coordapp.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.R;

public class SaveTxtDialog extends DialogFragment implements View.OnClickListener  {

    private EditText nameEt;
    private Button mConfirm;
    private Button mCancel;
    private TextView tipsTv;
    private String tips;

    private ClickExportTxtListener mClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_save_txt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEt = (EditText) view.findViewById(R.id.input_txt_name);
        mCancel = (Button) view.findViewById(R.id.buttonCancel);
        mConfirm = (Button) view.findViewById(R.id.buttonOk);
        tipsTv = view.findViewById(R.id.tips);
        tipsTv.setText("提示：文件保存至 “" + tips + "” 文件下，卸载该应用会同时删除该目录下所有文件，若需要请自行备份！");

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonOk:
                if (mClickListener != null) {
                    String name = nameEt.getText().toString();
                    if (name.equals("")){
                        Toast.makeText( v.getContext().getApplicationContext(),"请输入文件名！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mClickListener.onConfirmExport(name, getTag());
                }
            case R.id.buttonCancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setClickListener(ClickExportTxtListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickExportTxtListener {
        void onConfirmExport(String txtName, String tag);
    }

    public void setTips(String tips){
        this.tips = tips;
    }
}

package com.mhimine.jdk.coordapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;
import com.mhimine.jdk.coordapp.Coord.Point;

public class CoordEditDialog  extends DialogFragment implements View.OnClickListener{

    private EditText nameEt;
    private EditText xEt;
    private EditText yEt;
    private EditText zEt;
    private Button mConfirm;
    private Button mCancel;
    private Point point;
    private DialogTitle dialogTitle;
    private int operateRowIndex;

    public void setOperateRowIndex(int operateRowIndex) {
        this.operateRowIndex = operateRowIndex;
    }

    private ClickListener mClickListener;

    public void setPoint(Point point) {
        this.point = point;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_coord_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEt = (EditText) view.findViewById(R.id.input_name);
        xEt = (EditText) view.findViewById(R.id.input_x);
        yEt = (EditText) view.findViewById(R.id.input_y);
        zEt = (EditText) view.findViewById(R.id.input_z);
        mCancel = (Button) view.findViewById(android.R.id.button2);
        mConfirm = (Button) view.findViewById(android.R.id.button1);
        dialogTitle = (DialogTitle) view.findViewById(R.id.title);
        if (getTag().equals("SeePoint")) {
            setReadOnly();
            dialogTitle.setText("查看坐标点");
        } else {
            if (point != null) {
                dialogTitle.setText("修改坐标点");
            }else {
                dialogTitle.setText("增加坐标点");
            }
        }
        if (point != null) {
            nameEt.setText(point.getPointName());
            xEt.setText(Convert.parseString(point.getX()));
            yEt.setText(Convert.parseString(point.getY()));
            zEt.setText(Convert.parseString(point.getZ()));
        }

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    public void setReadOnly(){
        nameEt.setTextIsSelectable(true);
        xEt.setTextIsSelectable(true);
        yEt.setTextIsSelectable(true);
        zEt.setTextIsSelectable(true);
        nameEt.setKeyListener(null);
        xEt.setKeyListener(null);
        yEt.setKeyListener(null);
        zEt.setKeyListener(null);
        mConfirm.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Log.e("theme", "onconclick");
        switch (v.getId()) {

            case android.R.id.button1:
                if (mClickListener != null) {
                    String name = nameEt.getText().toString();
                    double x = Convert.parseDouble(xEt.getText().toString());
                    double y = Convert.parseDouble(yEt.getText().toString());
                    double z = Convert.parseDouble(zEt.getText().toString());

                    if (point == null) {
                        mClickListener.onConfirmAdd(name, x, y, z);
                    } else {
                        mClickListener.onConfirmEdit(name, x, y, z,operateRowIndex);
                    }
                }
            case android.R.id.button2:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onConfirmAdd(String name, double x, double y, double z);
        void onConfirmEdit(String name, double x, double y, double z,int operateRowIndex);
    }
}

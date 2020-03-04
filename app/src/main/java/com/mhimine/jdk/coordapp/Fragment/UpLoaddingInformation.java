package com.mhimine.jdk.coordapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Coord.CoordTransform;
import com.mhimine.jdk.coordapp.Coord.Point;
import com.mhimine.jdk.coordapp.Coord.TransParaSeven;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;


/**
 * Created by JDK on 2016/8/4.
 */
public class UpLoaddingInformation extends Fragment {
    private View view;
    private Button transButton;
    private Button clearBtn;
    private EditText sourceX;
    private EditText sourceY;
    private EditText sourceZ;
    private EditText TargetX;
    private EditText TargetY;
    private EditText TargetZ;
    private Spinner spinnerSource;
    private Spinner spinnerTarget;

    public static UpLoaddingInformation newInstance(Context context){
        UpLoaddingInformation singleFragment =new UpLoaddingInformation();
        return singleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_watch_single, container, false);//得到对应的布局文件
        transButton = (Button) view.findViewById(R.id.bt_scanner);
       // clearBtn = (Button) view.findViewById(R.id.point_clearBtn);

        TargetX.setKeyListener(null);
        TargetY.setKeyListener(null);
        TargetZ.setKeyListener(null);

        transButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String strSourceX = sourceX.getText().toString();
                double sourceX = Convert.parseDouble(strSourceX);

                String strSourceY = sourceY.getText().toString();
                double sourceY = Convert.parseDouble(strSourceY);

                String strSourceZ = sourceZ.getText().toString();
                double sourceZ = Convert.parseDouble(strSourceZ);

                Point point = new Point(sourceX, sourceY, sourceZ);
                setPointSourceView(point);

                String curCoordTransType = getCoordType();
                if (curCoordTransType == null) return;
                if (CoordTransform.SevenParames.containsKey(curCoordTransType)){
                    TransParaSeven transParaSeven = CoordTransform.SevenParames.get(curCoordTransType);
                    if (!CoordTransform.isRange(point)) {
                        Toast.makeText(v.getContext().getApplicationContext(),"该坐标超出矿区范围，无法转换！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Point newPoint = CoordTransform.BolsaSevenParameters(transParaSeven, point);
                    setPointTargetView(newPoint);
                }
                else {
                    TargetX.getText().clear();
                    TargetY.getText().clear();
                    TargetZ.getText().clear();
                    Toast.makeText(v.getContext().getApplicationContext(),"未设置该转换的转换参数，请先设置转换参数！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sourceX.getText().clear();
                sourceY.getText().clear();
                sourceZ.getText().clear();
                TargetX.getText().clear();
                TargetY.getText().clear();
                TargetZ.getText().clear();
            }
        });

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //当AdapterView中的item被选中的时候执行的方法。
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String spSourceSel =spinnerSource.getSelectedItem().toString();
//                Resources res =getResources();
//                String[] coords = res.getStringArray(R.array.spinnerCoord);
//                List<String> targetSpinner = new ArrayList<>();
//                for (String coord : coords) {
//                    if (!coord.equals(spSourceSel)){
//                        targetSpinner.add(coord);
//                    }
//                }
//                ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,targetSpinner);
//                spinnerTarget.setAdapter(adapterTarget);

            }

            @Override
            //未选中时的时候执行的方法
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext().getApplicationContext(),"warning",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void setPointTargetView(Point point){
        TargetX.setText(Convert.parseString(point.getX()));
        TargetY.setText(Convert.parseString(point.getY()));
        TargetZ.setText(Convert.parseString(point.getZ()));
    }

    private void setPointSourceView(Point point){
        sourceX.setText(Convert.parseString(point.getX()));
        sourceY.setText(Convert.parseString(point.getY()));
        sourceZ.setText(Convert.parseString(point.getZ()));
    }

    private String getCoordType(){
        String spSouStr = spinnerSource.getSelectedItem().toString();
        if (spinnerTarget.getSelectedItem() == null){
            return null;
        }
        String spTarStr = spinnerTarget.getSelectedItem().toString();
        String coordType = spSouStr + ";" + spTarStr;
        return coordType;
    }

}

package com.mhimine.jdk.coordapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Dialog.SaveTxtDialog;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;
import com.mhimine.jdk.coordapp.Coord.Point;
import com.mhimine.jdk.coordapp.Coord.TransParaSeven;
import com.mhimine.jdk.coordapp.Coord.CoordTransform;
import com.mhimine.jdk.coordapp.FileManage.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by JDK on 2016/8/10.
 */
public class FileFragment extends Fragment implements SaveTxtDialog.ClickExportTxtListener {

    private View view;
    private Spinner spinnerSource;
    private Spinner spinnerTarget;
    private EditText inputfileET;
    private EditText outputfileET;
    private ImageButton openSourceFileBtn;
    private ImageButton selTargetFileBtn;
    private Button transButton;
    private Button clearBtn;

    private String inputPath = ""; //输入文件路径
    private String outputPath = ""; //输出文件路径

    private Context context;

    public static FileFragment newInstance(Context context){
        FileFragment fileFragment =new FileFragment();
        return fileFragment;
    }
    final SaveTxtDialog.ClickExportTxtListener _this = this;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_watch_file, container, false);
        context = view.getContext();
        spinnerSource = (Spinner)view.findViewById(R.id.file_spinner_source);
        spinnerTarget = (Spinner)view.findViewById(R.id.file_spinner_target);
        inputfileET = (EditText)view.findViewById(R.id.input_file_path);
        outputfileET = (EditText)view.findViewById(R.id.output_file_path);
        openSourceFileBtn = (ImageButton) view.findViewById(R.id.open_source_file);
        selTargetFileBtn = (ImageButton) view.findViewById(R.id.open_target_file);
        transButton = (Button) view.findViewById(R.id.file_transBtn);
      //  clearBtn = (Button) view.findViewById(R.id.file_clearBtn);

        inputfileET.setKeyListener(null);
        outputfileET.setKeyListener(null);
        final SaveTxtDialog.ClickExportTxtListener _this = this;
        openSourceFileBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final File file = context.getExternalFilesDir("柠条塔坐标转换/坐标文件/");
                List<String> fileNamelist = FileUtils.getFileName(file,new ArrayList<String>(),"");
                String[] fileNames = new String[fileNamelist.size()];
                fileNamelist.toArray(fileNames);
                final String[] items = fileNames;
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("请选择坐标文件");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        File txtPath = new File(file, items[index]);
                        importTxt(txtPath);
                    }
                });
                alertBuilder.show();
            }
        });
        selTargetFileBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SaveTxtDialog dialog = new SaveTxtDialog();
                dialog.setClickListener(_this);
                dialog.setTips(FileUtils.CoordFilePath);
                dialog.show(getFragmentManager(),"source");
            }
        });


        transButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (inputPath.equals("") && outputPath.equals("")){
                    Toast.makeText(context.getApplicationContext(),"请先选择输入或输出文件路径！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String curCoordTransType = getCoordType();
                if (curCoordTransType == null) return;
                if (CoordTransform.SevenParames.containsKey(curCoordTransType)){
                    List<Point> targetPoints = new ArrayList<>();
                    List<Point> points = FileUtils.getPoints(inputPath,context);
                    if (points == null) {
                        return;
                    }
                    int i = 0;
                    for (Point point : points){
                        i++;
                        if (!CoordTransform.isRange(point)) {
                            Toast.makeText(context.getApplicationContext(),"第" + i + "个点超出该矿区范围，无法转换！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TransParaSeven transParaSeven = CoordTransform.SevenParames.get(curCoordTransType);
                        Point targetPoint = CoordTransform.BolsaSevenParameters(transParaSeven, point);
                        targetPoints.add(targetPoint);
                    }
                    if (FileUtils.saveTargetPoint(outputPath, targetPoints)){
                        Toast.makeText(context.getApplicationContext(),"转换成功！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context.getApplicationContext(),"转换失败！",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context.getApplicationContext(),"未设置该转换的转换参数，请先设置转换参数！",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        clearBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                if (outputPath.equals("")) {
//                    Toast.makeText(context.getApplicationContext(),"请先执行转换再查看！",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent intent = new Intent();
//                File file = new File(outputPath);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置标记
//                intent.setAction(Intent.ACTION_VIEW);//动作，查看
//                intent.setDataAndType(Uri.fromFile(file), FileUtils.getMIMEType(file));//设置类型
//                startActivity(intent);
//
//            }
//        });

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //当AdapterView中的item被选中的时候执行的方法。
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spSourceSel =spinnerSource.getSelectedItem().toString();
                Resources res =getResources();
                String[] coords = res.getStringArray(R.array.spinnerCoord);
                List<String> targetSpinner = new ArrayList<>();
                for (String coord : coords) {
                    if (!coord.equals(spSourceSel)){
                        targetSpinner.add(coord);
                    }
                }
                ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,targetSpinner);
                spinnerTarget.setAdapter(adapterTarget);
            }

            @Override
            //未选中时的时候执行的方法
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context.getApplicationContext(),"warning",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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


    @Override
    public void onConfirmExport(String txtName, String tag) {
        File sdPath = context.getExternalFilesDir("柠条塔坐标转换/坐标文件/");
        final File file = new File(sdPath, txtName + ".txt");
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//内部类
            builder.setTitle("提示");
            builder.setMessage("已存在该文件，是否覆盖?");
            //确定按钮

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //确定删除的代码
                    exportTxt(file);
                }
            });
            //点取消按钮
            builder.setNegativeButton("取消", null);
            builder.show();
        }
        else {
            exportTxt(file);
        }
    }

    private void exportTxt(File file){
        FileUtils.CreateFile(file.toString());
        outputPath = file.toString();
        outputfileET.setText(outputPath);
        outputfileET.setSelection(outputfileET.length());
        Toast.makeText(this.context.getApplicationContext(),"导出成功！",Toast.LENGTH_SHORT).show();
    }
    private void importTxt(File file) {
        if (file.exists()) {
            inputPath = file.toString();
            inputfileET.setText(inputPath);
            inputfileET.setSelection(inputfileET.length());
            Toast.makeText(context.getApplicationContext(),"导入成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context.getApplicationContext(),"文件不存在！",Toast.LENGTH_SHORT).show();
        }
    }

}

